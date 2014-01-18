package org.scpr.reader;

import android.content.Context;

import org.scpr.api.Article;

import java.util.ArrayList;


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


    public void setArticles(ArrayList<Article> articles)
    {
        mArticles = articles;
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

    public void addAll(ArrayList<Article> articles)
    {
        mArticles.addAll(articles);
    }


    public int size()
    {
        return mArticles.size();
    }

}
