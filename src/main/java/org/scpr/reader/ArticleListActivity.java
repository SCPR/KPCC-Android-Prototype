package org.scpr.reader;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class ArticleListActivity extends MainActivity
{

    private final static String TAG = "org.scpr.reader.DEBUG.ArticleListActivity";


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
        {
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                .replace(R.id.content_frame, ArticleListFragment.newInstance(null))
                .commit();
        }
    }

    @Override
    protected int getMainLayoutId()
    {
        return R.layout.activity_main;
    }

}
