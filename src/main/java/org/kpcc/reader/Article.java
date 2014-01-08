package org.kpcc.reader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Article
{

    private final static String TAG = "Article";

    private String mId;
    private String mTitle;
    private Date mTimestamp;
    private String mBody;


    public static Article buildFromJson(JSONObject jsonArticle)
    {
        Article article = new Article();

        try
        {
            article.setId(jsonArticle.getString("id"));
            article.setTitle(jsonArticle.getString("title"));
            article.setBody(jsonArticle.getString("body"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date timestamp = sdf.parse(jsonArticle.getString("published_at"));
            article.setTimestamp(timestamp);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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


    public Date getTimestamp()
    {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        mTimestamp = timestamp;
    }


    public String getBody()
    {
        return mBody;
    }

    public void setBody(String body)
    {
        mBody = body;
    }

}
