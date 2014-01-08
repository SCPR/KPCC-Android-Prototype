package org.kpcc.reader;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;


public class ArticleCollection
{

    private final static String TAG = "ArticleCollection";
    private static ArticleCollection sArticleCollection;

    private Context mAppContext;
    private ArrayList<Article> mArticles;


    private ArticleCollection(Context appContext)
    {
        mAppContext = appContext;
        mArticles = new ArrayList<Article>();
    }


    public static ArticleCollection get(Context c)
    {
        if (sArticleCollection == null)
        {
            sArticleCollection = new ArticleCollection(c.getApplicationContext());
        }

        return sArticleCollection;
    }


    public ArrayList<Article> getArticles()
    {
        return mArticles;
    }


    public Article getArticle(String id)
    {
        for (Article article : mArticles)
        {
            if (article.getId().equals(id))
            {
                return article;
            }
        }

        return null;
    }


    public void add(Article article)
    {
        mArticles.add(article);
    }
}
