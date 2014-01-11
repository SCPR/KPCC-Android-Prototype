package org.kpcc.reader;

import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class AssetSize
{

    private String mUrl;
    private int mWidth;
    private int mHeight;
    private Drawable mDrawable;


    public static AssetSize buildFromJson(JSONObject jsonAssetSize)
    {
        AssetSize assetSize = new AssetSize();

        try
        {
            assetSize.setUrl(jsonAssetSize.getString("url"));
            assetSize.setWidth(jsonAssetSize.getInt("width"));
            assetSize.setHeight(jsonAssetSize.getInt("height"));

        } catch(JSONException e) {
            e.printStackTrace();
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return assetSize;
    }


    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(String url)
    {
        mUrl = url;
    }


    public int getWidth()
    {
        return mWidth;
    }

    public void setWidth(int width)
    {
        mWidth = width;
    }


    public int getHeight()
    {
        return mHeight;
    }

    public void setHeight(int height)
    {
        mHeight = height;
    }

    public void setDrawable(Drawable drawable)
    {
        mDrawable = drawable;
    }

    public void insertDrawable(ImageView imageView)
    {
        if (mDrawable != null)
        {
            imageView.setImageDrawable(mDrawable);
        } else {
            new MediaDownload(this, imageView).execute(this);
        }
    }

}
