package org.scpr.reader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Audio extends Entity
{
    private int mId;
    private String mDescription;
    private String mUrl;
    private String mByline;
    private Date mUploadedAt;
    private int mPosition;
    private int mDurationSeconds;
    private int mFilesizeBytes;
    private String mArticleId;


    public static Audio buildFromJson(JSONObject jsonAudio)
    {
        Audio audio = new Audio();

        try
        {
            audio.setId(jsonAudio.getInt("id"));
            audio.setDescription(jsonAudio.getString("description"));
            audio.setUrl(jsonAudio.getString("url"));
            audio.setByline(jsonAudio.getString("byline"));
            audio.setPosition(jsonAudio.getInt("position"));
            audio.setDurationSeconds(jsonAudio.getInt("duration"));
            audio.setFilesizeBytes(jsonAudio.getInt("filesize"));
            audio.setArticleId(jsonAudio.getString("article_obj_key"));
            audio.setUploadedAt(parseISODate(jsonAudio.getString("uploaded_at")));

        } catch(JSONException e) {
            // TODO: Handle error
            e.printStackTrace();
        }

        return audio;
    }


    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        this.mId = id;
    }


    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        this.mDescription = description;
    }


    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(String url)
    {
        this.mUrl = url;
    }


    public String getByline()
    {
        return mByline;
    }

    public void setByline(String byline)
    {
        this.mByline = byline;
    }


    public Date getUploadedAt()
    {
        return mUploadedAt;
    }

    public void setUploadedAt(Date uploadedAt)
    {
        this.mUploadedAt = uploadedAt;
    }


    public int getPosition()
    {
        return mPosition;
    }

    public void setPosition(int position)
    {
        this.mPosition = position;
    }


    public int getDurationSeconds()
    {
        return mDurationSeconds;
    }

    public void setDurationSeconds(int durationSeconds)
    {
        this.mDurationSeconds = durationSeconds;
    }


    public int getFilesizeBytes()
    {
        return mFilesizeBytes;
    }

    public void setFilesizeBytes(int filesizeBytes)
    {
        this.mFilesizeBytes = filesizeBytes;
    }


    public String getArticleId()
    {
        return mArticleId;
    }

    public void setArticleId(String articleId)
    {
        this.mArticleId = articleId;
    }

}
