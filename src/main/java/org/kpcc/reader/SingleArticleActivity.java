package org.kpcc.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SingleArticleActivity extends FragmentActivity
{

    private final static String TAG = "org.kpcc.reader.DEBUG.SingleArticleActivity";

    private ViewPager mViewPager;
    private ArrayList<Article> mArticles;
    private boolean mLoadingArticles = false;
    private int mLastPage;
    private HashMap<String, String> mParams;    // We want to keep track of params so we can load
                                                // the appropriate content when the
                                                // users reaches the last item in the view pager.



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mParams = (HashMap<String,String>) getIntent()
            .getSerializableExtra(ArticleListFragment.EXTRA_REQUEST_PARAMS);
        mLastPage = getIntent().getIntExtra(ArticleListFragment.EXTRA_LAST_PAGE, 1);

        mArticles = ArticleCollection.get(this).getArticles();
        setContentView(R.layout.activity_single_article);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.single_article_view_pager);

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int pos)
            {
                // If this is the last item in the collection,
                // fetch more!
                if (pos + 1 == getCount() && !mLoadingArticles)
                {
                    mLoadingArticles = true;
                    mParams.put("page", String.valueOf(mLastPage + 1));
                    ArticleClient.getCollection(hashToParams(mParams), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONArray articles) {
                            ArrayList<Article> collection = new ArrayList<Article>();

                            try {
                                for (int i = 0; i < articles.length(); i++) {
                                    Article article = Article.buildFromJson(articles.getJSONObject(i));
                                    collection.add(article);
                                }
                            } catch (JSONException e) {
                                // TODO: Handle this error more nicely.
                                e.printStackTrace();
                            }

                            for (Article article : collection) mArticles.add(article);
                            mLastPage += 1;
                            mLoadingArticles = false;
                        }
                    });
                }

                Article article = mArticles.get(pos);
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

        String articleId = getIntent().getStringExtra(SingleArticleFragment.EXTRA_ARTICLE_ID);

        for (int i=0; i < mArticles.size(); i++)
        {
            if (mArticles.get(i).getId().equals(articleId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


    private RequestParams hashToParams(HashMap<String, String> hash)
    {
        RequestParams params = new RequestParams();

        for (Map.Entry<String, String> entry : hash.entrySet())
        {
            params.put(entry.getKey(), entry.getValue());
        }

        return params;
    }

}
