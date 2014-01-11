package org.kpcc.reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ArticleListActivity extends Activity
{

    private final static String TAG = "ArticleListActivity";

    private ArticleCollection mArticles;
    private GridView mGridView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mGridView = (GridView)findViewById(R.id.articles_GridView);

        // Start to fetch the articles ASAP.
        // This needs to be after the mGridView is setup,
        // because setupAdapter() depends on its presence.
        mArticles = ArticleCollection.get(this);
        if (mArticles.size() == 0)
        {
            fetchArticles();
        } else {
            setupAdapter();
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
    }


    private void fetchArticles()
    {
        ArticleClient.getCollection(null, new JsonHttpResponseHandler()
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

                    ArticleListActivity.this.setupAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void addArticle(Article article)
    {
        if (mArticles.getArticle(article.getId()) == null)
        {
            mArticles.add(article);
        }
    }


    public void setupAdapter()
    {
        ArticleAdapter adapter = new ArticleAdapter(mArticles);
        mGridView.setAdapter(adapter);
    }


    private class ArticleAdapter extends ArrayAdapter<Article>
    {

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

            AssetSize assetSize = article.getAssets().get(0).getSizeThumbnail();
            assetSize.insertDrawable(assetImageView);

            return convertView;
        }

    }

}
