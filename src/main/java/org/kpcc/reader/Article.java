package org.kpcc.reader;

import org.json.*;

import java.util.Date;


public class Article
{

    private String mId;
    private String mTitle;
    private String mDate;


    public static Article buildFromJson(JSONObject jsonArticle)
    {
        Article article = new Article();

        try
        {
            article.setId(jsonArticle.getString("id"));
            article.setTitle(jsonArticle.getString("title"));
            article.setDate(jsonArticle.getString("published_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return article;
    }


    @Override
    public String toString()
    {
        return mTitle;
    }


    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        mId = id;
    }


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }


    public String getDate()
    {
        return mDate;
    }

    public void setDate(String date)
    {
        mDate = date;
    }
}
