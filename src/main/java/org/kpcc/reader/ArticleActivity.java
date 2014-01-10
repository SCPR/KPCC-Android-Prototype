package org.kpcc.reader;

import android.support.v4.app.Fragment;


public class ArticleActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        String articleId = getIntent().getStringExtra(ArticleFragment.EXTRA_ARTICLE_ID);
        return ArticleFragment.newInstance(articleId);
    }

}
