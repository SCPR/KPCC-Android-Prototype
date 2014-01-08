package org.kpcc.reader;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.json.*;
import com.loopj.android.http.*;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.util.Log;


public class ArticleListFragment extends ListFragment
{

    private final static String TAG = "ArticleListFragment";

    private ArticleCollection mArticles;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);

        if (mArticles == null)
        {
            fetchArticles();
        } else {
            setupAdapter();
        }
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
                        ArticleListFragment.this.addArticle(article);
                    }

                    ArticleListFragment.this.setupAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void addArticle(Article article)
    {
        if (mArticles == null)
        {
            mArticles = ArticleCollection.get(getActivity());
        }

        mArticles.add(article);
    }


    public void setupAdapter()
    {
        ArticleAdapter adapter = new ArticleAdapter(mArticles);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Article a = ((ArticleAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), ArticleActivity.class);
        i.putExtra(ArticleFragment.EXTRA_ARTICLE_ID, a.getId());
        startActivity(i);
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

            Article a = getItem(position);

            TextView titleTextView =
                (TextView)convertView.findViewById(
                R.id.article_list_item_title_textView);

            TextView timestampTextView =
                (TextView)convertView.findViewById(
                R.id.article_list_item_timestamp_textView);

            titleTextView.setText(a.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
            timestampTextView.setText(sdf.format(a.getTimestamp()));

            return convertView;
        }

    }

}
