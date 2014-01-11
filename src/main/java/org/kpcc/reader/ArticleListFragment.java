package org.kpcc.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


public class ArticleListFragment extends ListFragment
{

    private final static String TAG = "ArticleListFragment";

    private ArticleCollection mArticles;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        getActivity().setTitle(R.string.app_name);


        mArticles = ArticleCollection.get(getActivity());

        if (mArticles.size() == 0)
        {
            fetchArticles();
        } else {
            setupAdapter();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, parent, savedInstanceState);

//        If we wanted to load the fragment_article_list template...
//        This removes the default spinner behavior (and we'd have to implement it manually).
//
//        View v = inflater.inflate(R.layout.fragment_article_list, parent, false);
//        return v;
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
        if (mArticles.getArticle(article.getId()) == null)
        {
            mArticles.add(article);
        }
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
        Intent i = new Intent(getActivity(), ArticlePagerActivity.class);
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

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm aa");
            String dateTime = sdf.format(a.getTimestamp());
            timestampTextView.setText(dateTime);

            return convertView;
        }

    }

}
