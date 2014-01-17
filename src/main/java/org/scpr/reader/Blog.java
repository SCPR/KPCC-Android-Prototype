package org.scpr.reader;

import org.json.JSONException;
import org.json.JSONObject;

public class Blog extends Entity
{

    private String mTitle;
    private String mId;
    private String mTagline;
    private String mDescription;
    private String mRssUrl;
    private String mPublicUrl;


    public static Blog buildFromJson(JSONObject jsonBlog)
    {
        Blog blog = new Blog();

        try
        {
            blog.setTitle(jsonBlog.getString("title"));
            blog.setId(jsonBlog.getString("slug"));
            blog.setTagline(jsonBlog.getString("tagline"));
            blog.setDescription(jsonBlog.getString("description"));
            blog.setPublicUrl(jsonBlog.getString("public_url"));

            if (jsonBlog.has("rss_url")) blog.setRssUrl(jsonBlog.getString("rss_url"));

        } catch (JSONException e) {
            // TODO: Handle exception
            e.printStackTrace();
        }

        return blog;
    }


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        this.mTitle = title;
    }


    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        this.mId = id;
    }


    public String getTagline()
    {
        return mTagline;
    }

    public void setTagline(String tagline)
    {
        this.mTagline = tagline;
    }


    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        this.mDescription = description;
    }


    public String getRssUrl()
    {
        return mRssUrl;
    }

    public void setRssUrl(String rssUrl)
    {
        this.mRssUrl = rssUrl;
    }


    public String getPublicUrl()
    {
        return mPublicUrl;
    }

    public void setPublicUrl(String publicUrl)
    {
        this.mPublicUrl = publicUrl;
    }

}
