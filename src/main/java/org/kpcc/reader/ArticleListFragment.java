package org.kpcc.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ArticleListFragment extends Fragment
{
    private final static String TAG = "org.kpcc.reader.DEBUG.ArticleListFragment";

    private final static int LOAD_THRESHOLD = 0;
    private final static String EXTRA_REQUEST_PARAMS = "org.kpcc.reader.request_params";

    private ArticleCollection mArticles;
    private GridView mGridView;
    private ArticleAdapter mAdapter;
    private int mLastPage = 0;
    private boolean mLoadingArticles = false;
    private RelativeLayout mLoadingIndicator;
    private RequestParams mParams = new RequestParams();


    public static ArticleListFragment newInstance(HashMap<String, String> params)
    {
        Bundle args = new Bundle();

        if (params != null)
        {
            args.putSerializable(EXTRA_REQUEST_PARAMS, params);
        }

        ArticleListFragment fragment = new ArticleListFragment();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Setup default params
        mParams.put("types", "news,blogs,segments");
        mParams.put("limit", "20");

        // Fill in params from passed-in arguments
        HashMap<String, String> params =
            (HashMap<String, String>) getArguments().getSerializable(EXTRA_REQUEST_PARAMS);

        if (params != null)
        {
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                mParams.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_article_list, parent, false);

        mGridView = (GridView)v.findViewById(R.id.articles_GridView);
        mLoadingIndicator = (RelativeLayout)v.findViewById(R.id.article_list_loading_footer);

        mArticles = ArticleCollection.get(getActivity());
        setupAdapter();

        if (mArticles.size() == 0)
        {
            fetchNextPage();
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Article a = ((ArticleAdapter) mGridView.getAdapter()).getItem(position);
                Intent i = new Intent(getActivity(), SingleArticleActivity.class);
                i.putExtra(SingleArticleFragment.EXTRA_ARTICLE_ID, a.getId());

                Log.d(TAG, "Starting SingleArticleActivity...");
                startActivity(i);
            }
        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == SCROLL_STATE_IDLE)
                {
                    if (view.getLastVisiblePosition() >= view.getCount() - 1 - LOAD_THRESHOLD)
                    {
                        fetchNextPage();
                    }
                }
            }

            @Override
            public void onScroll(
            AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
            }
        });

        return v;
    }

    private void fetchNextPage()
    {
        if (mLoadingArticles) return;
        mLoadingIndicator.setVisibility(View.VISIBLE);

        mLastPage += 1;

        mParams.put("page", String.valueOf(mLastPage));

        // Add a loading mutex to prevent loading too much.
        // The lock gets released in the onSuccess callback.
        mLoadingArticles = true;

        ArticleClient.getCollection(mParams, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONArray articles)
            {
                try
                {
                    for (int i = 0; i < articles.length(); i++)
                    {
                        JSONObject a = articles.getJSONObject(i);
                        Article article = Article.buildFromJson(a);
                        ArticleListFragment.this.addArticle(article);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mLoadingIndicator.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                mLoadingArticles = false;
            }

            @Override
            public void onFailure(
            int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO: Handle errors mo' betta
                mLoadingArticles = false;
            }
        });
    }


    public void addArticle(Article article)
    {
        if (mArticles.getArticle(article.getId()) == null)
        {
            mAdapter.add(article);
        }
    }


    private void setupAdapter()
    {
        mAdapter = new ArticleAdapter(mArticles);
        mGridView.setAdapter(mAdapter);
    }



    private class ArticleAdapter extends ArrayAdapter<Article>
    {

        public ArticleAdapter(ArticleCollection articleCollection)
        {
            super(getActivity(), 0, articleCollection.getArticles());
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_article, null);
            }

            Article article = getItem(position);

            TextView titleTextView =
                (TextView)convertView.findViewById(
                    R.id.article_list_item_title_textView);

            ImageView assetImageView =
                    (ImageView) convertView.findViewById(
                            R.id.article_list_item_asset_ImageView);

            titleTextView.setText(article.getShortTitle());

            if (article.hasAssets())
            {
                String url = article.getAssets().get(0).getSizeSmall().getUrl();
                ImageLoader.getInstance().displayImage(url, assetImageView);
            }

            return convertView;
        }

    }

}
