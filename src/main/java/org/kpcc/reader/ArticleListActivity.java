package org.kpcc.reader;

import android.support.v4.app.Fragment;


public class ArticleListActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new ArticleListFragment();
    }

}
