package org.scpr.reader;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.scpr.api.Article;


public class SingleArticleFragment extends SherlockFragment
{

    private static final String TAG = "org.scpr.reader.DEBUG.SingleArticleFragment";

    public static final String EXTRA_ARTICLE_ID = "org.scpr.reader.article_id";
    public static final String EXTRA_QUERY_PARAMS = "org.scpr.reader.query_params";

    private Article mArticle;
    private ImageButton mAudioPlayButton;
    private LinearLayout mArticleLayout;
    private ProgressBar mProgress;
    private TextView mTitle;
    private TextView mBody;
    private TextView mTimestamp;
    private TextView mByline;
    private ImageView mAsset;


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

        mAudioPlayButton = (ImageButton) v.findViewById(R.id.audio_btn_play);
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

        if (mArticle.hasAudio())
        {
//            mAudioPlayButton.setVisibility(View.VISIBLE);
            mAudioPlayButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    mAudioBar.setAudio(mArticle, mArticle.getAudio().get(0));
                }
            });
        }

        return v;
    }


    private boolean hasParentActivity()
    {
        return NavUtils.getParentActivityName(getActivity()) != null;
    }

}
