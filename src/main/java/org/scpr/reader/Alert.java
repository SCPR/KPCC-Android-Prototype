package org.scpr.reader;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class Alert extends Entity
{

    private int mId;
    private String mHeadline;
    private String mType;
    private Date mPublishedAt;
    private String mPublicUrl;
    private String mTeaser;
    private boolean mWasMobileNotificationSent;
    private boolean mWasEmailNotificationSent;


    public static Alert buildFromJson(JSONObject jsonAlert)
    {
        Alert alert = new Alert();

        try
        {
            alert.setId(jsonAlert.getInt("id"));
            alert.setHeadline(jsonAlert.getString("headline"));
            alert.setType(jsonAlert.getString("type"));
            alert.setWasMobileNotificationSent(jsonAlert.getBoolean("mobile_notification_sent"));
            alert.setWasEmailNotificationSent(jsonAlert.getBoolean("email_notification_sent"));
            alert.setPublishedAt(parseISODate(jsonAlert.getString("published_at")));

            if (jsonAlert.has("public_url")) alert.setPublicUrl(jsonAlert.getString("public_url"));
            if (jsonAlert.has("teaser")) alert.setTeaser(jsonAlert.getString("teaser"));

        } catch (JSONException e) {
            // TODO: Handle error
            e.printStackTrace();
        }

        return alert;
    }


    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        this.mId = id;
    }


    public String getHeadline()
    {
        return mHeadline;
    }

    public void setHeadline(String headline)
    {
        this.mHeadline = headline;
    }


    public String getType()
    {
        return mType;
    }

    public void setType(String type)
    {
        this.mType = type;
    }


    public Date getPublishedAt()
    {
        return mPublishedAt;
    }

    public void setPublishedAt(Date publishedAt)
    {
        this.mPublishedAt = publishedAt;
    }


    public String getPublicUrl()
    {
        return mPublicUrl;
    }

    public void setPublicUrl(String publicUrl)
    {
        this.mPublicUrl = publicUrl;
    }


    public String getTeaser()
    {
        return mTeaser;
    }

    public void setTeaser(String teaser)
    {
        this.mTeaser = teaser;
    }


    public boolean getWasMobileNotificationSent()
    {
        return mWasMobileNotificationSent;
    }

    public void setWasMobileNotificationSent(boolean wasMobileNotificationSent)
    {
        this.mWasMobileNotificationSent = wasMobileNotificationSent;
    }


    public boolean getWasEmailNotificationSent()
    {
        return mWasEmailNotificationSent;
    }

    public void setWasEmailNotificationSent(boolean wasEmailNotificationSent)
    {
        this.mWasEmailNotificationSent = wasEmailNotificationSent;
    }

}
