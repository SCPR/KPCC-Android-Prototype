package org.scpr.reader;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public abstract class MainActivity extends FragmentActivity
{

    private final static String TAG = "org.scpr.reader.DEBUG.MainActivity";

    protected abstract int getMainLayoutId();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NavDrawerItem[] mMenuItems;
    private AudioPlayerFragment mAudioPlayerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getMainLayoutId());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_menu);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
            .add(R.id.main_frame, new AudioPlayerFragment(), AudioPlayerFragment.FRAGMENT_ID)
            .commit();

        mAudioPlayerFragment = (AudioPlayerFragment) fm.findFragmentById(R.layout.fragment_audio_player);

        mTitle = getTitle();
        mDrawerTitle = getTitle();

        // Setup the Menu items.
        // An array of abstract NavDrawerItem objects,
        // filled with NavMenuSection and NavMenuItem objects.
        mMenuItems = new NavDrawerItem[]
        {
            NavMenuSection.create(300, "KPCC Reader"),
            NavMenuItem.create(301, "Live Stream", "ic_action_next_item", this),
            NavMenuItem.create(302, "All Articles", "ic_action_next_item", this),

            NavMenuSection.create(100, "Categories"),
            NavMenuItem.create(101, "Politics", "ic_action_next_item", this),
            NavMenuItem.create(102, "Education", "ic_action_next_item", this),
            NavMenuItem.create(103, "Environment", "ic_action_next_item", this),
            NavMenuItem.create(104, "Music", "ic_action_next_item", this),
            NavMenuItem.create(105, "Health", "ic_action_next_item", this),

            NavMenuSection.create(200, "Programs"),
            NavMenuItem.create(201, "AirTalk", "ic_action_next_item", this),
            NavMenuItem.create(202, "Take Two", "ic_action_next_item", this),
            NavMenuItem.create(203, "Off-Ramp", "ic_action_next_item", this),
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
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }


    protected void onNavItemSelected(int id)
    {
        QueryParams params = new QueryParams();

        switch (id)
        {
            // Live Stream
            case 301:
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, LiveStreamFragment.newInstance())
                    .commit();

                break;

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

            case 201:
                break;

            case 202:
                break;

            case 203:
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
