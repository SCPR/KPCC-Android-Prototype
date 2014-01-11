package org.kpcc.reader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Article
{

    private final static String TAG = "Article";

    private String mId;
    private String mTitle;
    private String mShortTitle;
    private String mPublicUrl;
    private String mByline;
    private Date mTimestamp;
    private String mTeaser;
    private String mBody;
    private ArrayList<Asset> mAssets = new ArrayList<Asset>();


    public static Article buildFromJson(JSONObject jsonArticle)
    {
        Article article = new Article();

        try
        {
            article.setId(jsonArticle.getString("id"));
            article.setTitle(jsonArticle.getString("title"));
            article.setShortTitle(jsonArticle.getString("short_title"));
            article.setPublicUrl(jsonArticle.getString("public_url"));
            article.setByline(jsonArticle.getString("byline"));
            article.setTeaser(jsonArticle.getString("teaser"));
            article.setBody(jsonArticle.getString("body"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date timestamp = sdf.parse(jsonArticle.getString("published_at"));
            article.setTimestamp(timestamp);

            JSONArray assets = jsonArticle.getJSONArray("assets");
            for (int i=0; i < assets.length(); i++)
            {
                article.addAsset(Asset.buildFromJson(assets.getJSONObject(i)));
            }

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


    public String getShortTitle()
    {
        return mShortTitle;
    }

    public void setShortTitle(String shortTitle)
    {
        mShortTitle = shortTitle;
    }


    public String getPublicUrl()
    {
        return mPublicUrl;
    }

    public void setPublicUrl(String publicUrl)
    {
        mPublicUrl = publicUrl;
    }


    public String getByline()
    {
        return mByline;
    }

    public void setByline(String byline)
    {
        mByline = byline;
    }


    public Date getTimestamp()
    {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        mTimestamp = timestamp;
    }


    public String getTeaser()
    {
        return mTeaser;
    }

    public void setTeaser(String teaser)
    {
        mTeaser = teaser;
    }


    public String getBody()
    {
        return mBody;
    }

    public void setBody(String body)
    {
        mBody = body;
    }


    public ArrayList<Asset> getAssets()
    {
        return mAssets;
    }

    public void setAssets(ArrayList<Asset> assets)
    {
        mAssets = assets;
    }

    public void addAsset(Asset asset)
    {
        mAssets.add(asset);
    }

}
