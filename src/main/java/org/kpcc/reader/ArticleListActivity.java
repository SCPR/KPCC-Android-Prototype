package org.kpcc.reader;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class ArticleListActivity extends DrawerActivity
{

    private final static String TAG = "org.kpcc.reader.DEBUG.ArticleListActivity";


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
        return R.layout.drawer_with_frame_layout;
    }

}
