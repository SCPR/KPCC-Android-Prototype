package org.kpcc.reader;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ArticleFragment extends Fragment
{

    public static final String EXTRA_ARTICLE_ID = "org.kpcc.reader.article_id";
    private static final String TAG = "ArticleFragment";

    private Article mArticle;
    private TextView mTitle;
    private TextView mBody;
    private TextView mTimestamp;
    private TextView mByline;
    private ImageView mAsset;


    public static ArticleFragment newInstance(String articleId)
    {
        Bundle args = new Bundle();
        args.putString(EXTRA_ARTICLE_ID, articleId);

        ArticleFragment fragment = new ArticleFragment();
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

        mTitle      = (TextView)v.findViewById(R.id.article_title_textView);
        mBody       = (TextView)v.findViewById(R.id.article_body_textView);
        mTimestamp  = (TextView)v.findViewById(R.id.article_timestamp_textView);
        mByline     = (TextView)v.findViewById(R.id.article_byline_TextView);
        mAsset      = (ImageView)v.findViewById(R.id.article_asset_ImageView);

        mTitle.setText(mArticle.getTitle());
        mByline.setText(mArticle.getByline());

        mBody.setText(Html.fromHtml(mArticle.getBody()));
        mBody.setMovementMethod(LinkMovementMethod.getInstance());

        java.text.DateFormat dateFormat =
            DateFormat.getLongDateFormat(getActivity().getApplicationContext());
        java.text.DateFormat timeFormat =
            DateFormat.getTimeFormat(getActivity().getApplicationContext());

        String date = dateFormat.format(mArticle.getTimestamp());
        String time = timeFormat.format(mArticle.getTimestamp());
        mTimestamp.setText(date + ", " + time);

        if (mArticle.hasAssets())
        {
            AssetSize assetSize = mArticle.getAssets().get(0).getSizeFull();
           new MediaDownload(assetSize, mAsset).execute(assetSize);
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
