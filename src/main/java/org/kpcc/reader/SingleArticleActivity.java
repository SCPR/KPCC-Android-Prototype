package org.kpcc.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;


public class SingleArticleActivity extends DrawerActivity
{

    private final static String TAG = "org.kpcc.reader.DEBUG.SingleArticleActivity";

    private ViewPager mViewPager;
    private ArrayList<Article> mArticles;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "Got onCreate()");

        super.onCreate(savedInstanceState);

        mArticles = ArticleCollection.get(this).getArticles();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int pos)
            {
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

        Log.d(TAG, "Exiting onCreate");

    }


    @Override
    protected int getMainLayoutId()
    {
        return R.layout.drawer_with_view_pager;
    }

}
