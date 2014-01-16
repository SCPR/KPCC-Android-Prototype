package org.kpcc.reader;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;


public class SingleArticleFragment extends Fragment
{

    private static final String TAG = "org.kpcc.reader.DEBUG.SingleArticleFragment";

    public static final String EXTRA_ARTICLE_ID = "org.kpcc.reader.article_id";
    public static final String EXTRA_QUERY_PARAMS = "org.kpcc.reader.query_params";

    private Article mArticle;
    private ScrollView mScrollView;
    private ProgressBar mProgress;
    private TextView mTitle;
    private TextView mBody;
    private TextView mTimestamp;
    private TextView mByline;
    private ImageView mAsset;
    private MediaPlayer mAudioPlayer;
    private MediaController mAudioController;


    public static SingleArticleFragment newInstance(String articleId)
    {
        Bundle args = new Bundle();
        args.putString(EXTRA_ARTICLE_ID, articleId);

        SingleArticleFragment fragment = new SingleArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String articleId = getArguments().getString(EXTRA_ARTICLE_ID);
        mArticle = ArticleCollection.get(getActivity()).getArticle(articleId);
    }


    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_article, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if (hasParentActivity())
            {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mProgress = (ProgressBar)v.findViewById(R.id.article_body_progress);

        mScrollView = (ScrollView) v.findViewById(R.id.single_article);
        mTitle      = (TextView) v.findViewById(R.id.article_title_textView);
        mBody       = (TextView) v.findViewById(R.id.article_body_textView);
        mTimestamp  = (TextView) v.findViewById(R.id.article_timestamp_textView);
        mByline     = (TextView) v.findViewById(R.id.article_byline_TextView);
        mAsset      = (ImageView) v.findViewById(R.id.article_asset_ImageView);

        mTitle.setText(mArticle.getTitle());
        mByline.setText(mArticle.getByline());

        if (mArticle.getParsedBody() == null)
        {
            new HtmlParser(mBody, mArticle, mProgress).execute(mArticle.getBody());
        } else {
            mBody.setText(mArticle.getParsedBody());
        }

        mBody.setMovementMethod(LinkMovementMethod.getInstance());

        Typeface serifFont =
            Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSerif.ttf");

        Typeface sansLightFont =
            Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        mTitle.setTypeface(sansLightFont);
        mBody.setTypeface(serifFont);

        java.text.DateFormat dateFormat =
            DateFormat.getLongDateFormat(getActivity().getApplicationContext());
        java.text.DateFormat timeFormat =
            DateFormat.getTimeFormat(getActivity().getApplicationContext());

        String date = dateFormat.format(mArticle.getTimestamp());
        String time = timeFormat.format(mArticle.getTimestamp());
        mTimestamp.setText(date + ", " + time);

        if (mArticle.hasAssets())
        {
            String url = mArticle.getAssets().get(0).getSizeFull().getUrl();
            ImageLoader.getInstance().displayImage(url, mAsset);
        }

        if (mArticle.hasAudio())
        {
            mAudioPlayer = new MediaPlayer();
            mAudioController = new MediaController(getActivity());

            mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mAudioController.setMediaPlayer(new MediaController.MediaPlayerControl()
                    {
                        @Override
                        public void start()
                        {
                            mAudioPlayer.start();
                        }

                        @Override
                        public void pause()
                        {
                            mAudioPlayer.pause();
                        }

                        @Override
                        public int getDuration()
                        {
                            return mAudioPlayer.getDuration();
                        }

                        @Override
                        public int getCurrentPosition()
                        {
                            return mAudioPlayer.getCurrentPosition();
                        }

                        @Override
                        public void seekTo(int pos)
                        {
                            mAudioPlayer.seekTo(pos);
                        }

                        @Override
                        public boolean isPlaying()
                        {
                            return mAudioPlayer.isPlaying();
                        }

                        @Override
                        public int getBufferPercentage()
                        {
                            return 0;
                        }

                        @Override
                        public boolean canPause()
                        {
                            return true;
                        }

                        @Override
                        public boolean canSeekBackward()
                        {
                            return true;
                        }

                        @Override
                        public boolean canSeekForward()
                        {
                            return true;
                        }

                        @Override
                        public int getAudioSessionId()
                        {
                            return mAudioPlayer.getAudioSessionId();
                        }
                    });

                    mAudioController.setAnchorView(mScrollView);

                    Handler handler = new Handler();
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            mAudioController.setEnabled(true);
                            mAudioController.show(0);
                        }
                    });
                }
            });

            try
            {
                mAudioPlayer.setDataSource(mArticle.getAudio().get(0).getUrl());
                mAudioPlayer.prepareAsync();
            } catch(IOException e) {
                // TODO: Handle error
                e.printStackTrace();
            }

            mScrollView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    mAudioController.show();
                    return false;
                }
            });
        }

        return v;
    }


    @Override
    public void onPause()
    {
        super.onPause();

        if (mAudioPlayer != null)
        {
            mAudioPlayer.pause();
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mAudioPlayer != null)
        {
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (hasParentActivity())
                {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean hasParentActivity()
    {
        return NavUtils.getParentActivityName(getActivity()) != null;
    }

}
