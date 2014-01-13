package org.kpcc.reader;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;


public class SingleArticleFragment extends Fragment
{

    public static final String EXTRA_ARTICLE_ID = "org.kpcc.reader.article_id";
    private static final String TAG = "org.kpcc.reader.DEBUG.SingleArticleFragment";

    private Article mArticle;
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

        mProgress = (ProgressBar)v.findViewById(R.id.article_body_progress);

        mTitle      = (TextView)v.findViewById(R.id.article_title_textView);
        mBody       = (TextView)v.findViewById(R.id.article_body_textView);
        mTimestamp  = (TextView)v.findViewById(R.id.article_timestamp_textView);
        mByline     = (TextView)v.findViewById(R.id.article_byline_TextView);
        mAsset      = (ImageView)v.findViewById(R.id.article_asset_ImageView);

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

        return v;
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