package org.kpcc.reader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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


public class ArticleListActivity extends Activity
{

    private final static String TAG = "org.kpcc.reader.DEBUG.ArticleListActivity";
    private final static int LOAD_THRESHOLD = 0;

    private ArticleCollection mArticles;
    private GridView mGridView;
    private ArticleAdapter mAdapter;
    private int mLastPage = 0;
    private boolean mLoadingArticles = false;
    private RelativeLayout mLoadingIndicator;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        mGridView = (GridView)findViewById(R.id.articles_GridView);
        mLoadingIndicator = (RelativeLayout)findViewById(R.id.article_list_loading_footer);

        mArticles = ArticleCollection.get(this);
        setupAdapter();

        if (mArticles.size() == 0)
        {
            fetchNextPage();
        }

        setTitle(R.string.app_name);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Article a = ((ArticleAdapter) mGridView.getAdapter()).getItem(position);
                Intent i = new Intent(ArticleListActivity.this, ArticleActivity.class);
                i.putExtra(ArticleFragment.EXTRA_ARTICLE_ID, a.getId());

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
    }


    private void fetchNextPage()
    {
        if (mLoadingArticles) return;
        mLoadingIndicator.setVisibility(View.VISIBLE);

        mLastPage += 1;

        Log.d(TAG, "Fetching Articles Page: " + mLastPage);

        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(mLastPage));
        params.put("limit", "20");
        params.put("types", "news,blogs,segments");

        // Add a loading mutex to prevent loading too much.
        // The lock gets released in the onSuccess callback.
        mLoadingArticles = true;

        ArticleClient.getCollection(params, new JsonHttpResponseHandler()
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
                        ArticleListActivity.this.addArticle(article);
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

        private Drawable[] imageCache;

        public ArticleAdapter(ArticleCollection articleCollection)
        {
            super(ArticleListActivity.this, 0, articleCollection.getArticles());
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = ArticleListActivity.this.getLayoutInflater()
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
