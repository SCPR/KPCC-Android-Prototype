package org.scpr.reader;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public abstract class MainActivity extends SherlockFragmentActivity
{

    private final static String TAG = "org.scpr.reader.DEBUG.MainActivity";
    private final static String PREFS_NAME = "org.scpr.reader.Preferences";
    private final static long PREROLL_THRESHOLD = 600L;

    public final static String LIVESTREAM_URL = "http://live.scpr.org/aac";
    public final static String LIVESTREAM_NOPREROLL_URL = LIVESTREAM_URL + "?preskip=true";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NavDrawerItem[] mMenuItems;
    private AudioPlayer mAudioPlayer;
    private ImageButton mAudioPlayBtn;
    private ImageButton mAudioStopBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_menu);

        mTitle = getTitle();
        mDrawerTitle = getTitle();

        // Setup the Menu items.
        // An array of abstract NavDrawerItem objects,
        // filled with NavMenuSection and NavMenuItem objects.
        mMenuItems = new NavDrawerItem[]
        {
            NavMenuSection.create(300, "KPCC Reader"),
            NavMenuItem.create(302, "All Articles", "ic_action_next_item", this),

            NavMenuSection.create(100, "Categories"),
            NavMenuItem.create(101, "Politics", "ic_action_next_item", this),
            NavMenuItem.create(102, "Education", "ic_action_next_item", this),
            NavMenuItem.create(103, "Environment", "ic_action_next_item", this),
            NavMenuItem.create(104, "Music", "ic_action_next_item", this),
            NavMenuItem.create(105, "Health", "ic_action_next_item", this),
        };

        mDrawerList.setAdapter(new NavDrawerAdapter(this, R.layout.drawer_item, mMenuItems));
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                selectItem(position);
            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            R.drawable.ic_drawer,
            R.string.drawer_open,
            R.string.drawer_close
        )
        {
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mAudioPlayer = new AudioPlayer();
        mAudioPlayBtn = (ImageButton) findViewById(R.id.audio_btn_play);
        mAudioStopBtn = (ImageButton) findViewById(R.id.audio_btn_stop);

        mAudioPlayBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // First thing, give the user some immediate feedback
                mAudioPlayBtn.setVisibility(View.GONE);
                mAudioStopBtn.setVisibility(View.VISIBLE);

                String url;

                long unixTime = System.currentTimeMillis() / 1000L;

                // If the last stream was more than PREROLL_THRESHOLD seconds ago, then
                // skip the preroll.
                // Otherwise, set the last preroll to right now and send them preroll stream.
                if (KpccReaderApplication.LIVESTREAM_LAST_PREROLL < (unixTime - PREROLL_THRESHOLD))
                {
                    KpccReaderApplication.LIVESTREAM_LAST_PREROLL = unixTime;
                    url = LIVESTREAM_URL;
                } else {
                    url = LIVESTREAM_NOPREROLL_URL;
                }

                mAudioPlayer.play(getApplicationContext(), url);
            }
        });

        mAudioStopBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAudioStopBtn.setVisibility(View.GONE);
                mAudioPlayBtn.setVisibility(View.VISIBLE);

                mAudioPlayer.stop();
            }
        });


        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.audio_slider);
        layout.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
        layout.setAnchorPoint(0.3f);
        layout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener()
        {
            @Override
            public void onPanelSlide(View panel, float slideOffset)
            {
                if (slideOffset < 0.2)
                {
                    if (getSupportActionBar().isShowing())
                    {
                        getSupportActionBar().hide();
                    }
                } else {
                    if (!getSupportActionBar().isShowing())
                    {
                        getSupportActionBar().show();
                    }
                }
            }

            @Override
            public void onPanelExpanded(View panel)
            {
            }

            @Override
            public void onPanelCollapsed(View panel)
            {
            }

            @Override
            public void onPanelAnchored(View panel)
            {
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    protected void onNavItemSelected(int id)
    {
        QueryParams params = new QueryParams();

        switch (id)
        {

            // All Articles
            case 302:
                buildListFragment(null);
                break;

            // Politics
            case 101:
                params.put("categories", "politics");
                buildListFragment(params);

                break;

            // Education
            case 102:
                params.put("categories", "education");
                buildListFragment(params);

                break;

            // Environment
            case 103:
                params.put("categories", "environment");
                buildListFragment(params);

                break;

            // Music
            case 104:
                params.put("categories", "music");
                buildListFragment(params);

                break;

            // Health
            case 105:
                params.put("categories", "health");
                buildListFragment(params);

                break;

        }
    }


    private void buildListFragment(QueryParams params)
    {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content_frame, ArticleListFragment.newInstance(params))
            .commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            if (mDrawerLayout.isDrawerOpen(mDrawerList))
            {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Workaround for ActionBarSherlock not supporting ActionBarDrawer
        // The onOptionsItemSelected method from Sherlock expects its own MenuItem,
        // but the one from ActionBarDrawer expects android.view.MenuItem, so we can't
        // chain these calls.
        if (item.getItemId() == android.R.id.home)
        {
            if (mDrawerLayout.isDrawerOpen(mDrawerList))
            {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void selectItem(int position)
    {
        NavDrawerItem selectedItem = mMenuItems[position];
        onNavItemSelected(selectedItem.getId());
        mDrawerList.setItemChecked(position, true);

        if (mDrawerLayout.isDrawerOpen(mDrawerList))
        {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

}
