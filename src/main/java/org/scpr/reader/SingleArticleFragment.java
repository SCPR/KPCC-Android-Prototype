package org.scpr.reader;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.scpr.api.Article;

import java.io.IOException;


public class SingleArticleFragment extends Fragment
{

    private static final String TAG = "org.scpr.reader.DEBUG.SingleArticleFragment";

    public static final String EXTRA_ARTICLE_ID = "org.scpr.reader.article_id";
    public static final String EXTRA_QUERY_PARAMS = "org.scpr.reader.query_params";

    private Article mArticle;
    private LinearLayout mArticleLayout;
    private RelativeLayout mAudioBar;
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
        Log.d(TAG, "in onCreate()");
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

        mAudioBar = (RelativeLayout) getActivity().findViewById(R.id.audio_player);

        mProgress = (ProgressBar) v.findViewById(R.id.article_body_progress);
        mArticleLayout = (LinearLayout) v.findViewById(R.id.single_article);
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

        if (mArticle.hasAudio() && mAudioPlayer == null)
        {
            mAudioPlayer = new MediaPlayer();

            mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    TextView title = (TextView) mAudioBar.findViewById(R.id.audio_title);
                    title.setText("Audio: " + mArticle.getAudio().get(0).getDescription());
                } // onPrepared
            });

            try
            {
                mAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mAudioPlayer.setDataSource(mArticle.getAudio().get(0).getUrl());
                mAudioPlayer.prepareAsync();
            } catch(IOException e) {
                // TODO: Handle error
                e.printStackTrace();
            }
        }

        return v;
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mAudioPlayer != null)
        {
            Log.d(TAG, "releasing audio player");
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
