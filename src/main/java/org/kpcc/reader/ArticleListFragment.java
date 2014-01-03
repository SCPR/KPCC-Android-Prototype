package org.kpcc.reader;

import java.util.ArrayList;
import org.json.*;
import com.loopj.android.http.*;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
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

    private ArrayList<Article> mArticles;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);

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

                    ArticleAdapter adapter = new ArticleAdapter(mArticles);
                    setListAdapter(adapter);
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
            mArticles = new ArrayList<Article>();
        }

        mArticles.add(article);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Article a = ((ArticleAdapter)getListAdapter()).getItem(position);
    }


    private class ArticleAdapter extends ArrayAdapter<Article>
    {

        public ArticleAdapter(ArrayList<Article> articles)
        {
            super(getActivity(), 0, articles);
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
                R.id.article_list_item_titleTextView);

            TextView dateTextView =
                (TextView)convertView.findViewById(
                R.id.article_list_item_dateTextView);

            titleTextView.setText(a.getTitle());
            dateTextView.setText(a.getDate());

            return convertView;
        }

    }

}
