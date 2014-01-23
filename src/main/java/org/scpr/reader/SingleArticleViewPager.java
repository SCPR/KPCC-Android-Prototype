package org.scpr.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scpr.api.Article;

import java.util.ArrayList;
import java.util.HashMap;


public class SingleArticleViewPager extends Fragment
{

    private final static String TAG = "org.scpr.reader.DEBUG.SingleArticleViewPager";
    public final static String EXTRA_ARTICLE_ID = "org.scpr.reader.article_id";
    public final static String EXTRA_REQUEST_PARAMS = "org.scpr.reader.request_params";
    public final static String EXTRA_LAST_PAGE = "org.scpr.reader.last_page";

    private ViewPager mViewPager;
    private ArticleCollection mArticles;
    private boolean mLoadingArticles = false;
    private int mLastPage;
    private QueryParams mParams;


    public static SingleArticleViewPager newInstance(
    String initialArticleId, HashMap<String, String> params, int lastPage)
    {
        Bundle args = new Bundle();
        args.putString(EXTRA_ARTICLE_ID, initialArticleId);
        args.putSerializable(EXTRA_REQUEST_PARAMS, params);
        args.putInt(EXTRA_LAST_PAGE, lastPage);

        SingleArticleViewPager fragment = new SingleArticleViewPager();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        HashMap<String, String> params = (HashMap<String, String>) getArguments()
            .getSerializable(ArticleListFragment.EXTRA_REQUEST_PARAMS);

        mParams = QueryParams.buildFromHashMap(params);
        mLastPage = getArguments().getInt(ArticleListFragment.EXTRA_LAST_PAGE, 1);
        mArticles = ArticleCollection.get(getActivity());
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);

        View v = inflater.inflate(R.layout.activity_single_article, parent, false);
        mViewPager = (ViewPager) v.findViewById(R.id.single_article_view_pager);

        FragmentManager fm = getChildFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int pos)
            {
                // If this is the last item in the collection,
                // fetch more!
                if (pos + 1 == getCount() && !mLoadingArticles)
                {
                    fetchArticles(nextPageParams());
                }

                Article article = mArticles.getArticles().get(pos);
                return SingleArticleFragment.newInstance(article.getId());
            }

            @Override
            public int getCount()
            {
                return mArticles.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int pos, float offset, int offsetPx)
            {
            }

            @Override
            public void onPageSelected(int pos)
            {
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });

        String articleId = getArguments().getString(SingleArticleFragment.EXTRA_ARTICLE_ID);
        mViewPager.setCurrentItem(mArticles.findPosition(articleId));

        return v;
    }


    private void fetchArticles(QueryParams params)
    {
        if (mLoadingArticles) return;
        setIsLoading(true);

        mParams.merge(params);
        Article.Client.getCollection(mParams.toParams(), new ArticleJsonResponseHandler());
    }


    private QueryParams nextPageParams()
    {
        QueryParams params = new QueryParams();
        params.put("page", String.valueOf(mLastPage + 1));
        return params;
    }


    private void setIsLoading(boolean isLoading)
    {
        mLoadingArticles = isLoading;
    }


    private class ArticleJsonResponseHandler extends JsonHttpResponseHandler
    {

        @Override
        public void onSuccess(JSONArray articles)
        {
            ArrayList<Article> collection = new ArrayList<Article>();

            try
            {
                for (int i = 0; i < articles.length(); i++)
                {
                    Article article = Article.buildFromJson(articles.getJSONObject(i));
                    collection.add(article);
                }
            } catch (JSONException e) {
                // TODO: Handle this error more nicely.
                e.printStackTrace();
            }

            // Update the ArticleCollection articles.
            for (Article article : collection)
            {
                mArticles.add(article);
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            // TODO: Find a better place to increase the page number.
            mLastPage += 1;

            setIsLoading(false);
        }

        @Override
        public void onFailure(
            int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
        {
            // TODO: Handle errors mo' betta
            setIsLoading(false);
        }
    }

}
