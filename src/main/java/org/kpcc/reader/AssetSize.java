package org.kpcc.reader;

import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class AssetSize
{

    private final static String TAG = "org.kpcc.reader.DEBUG.AssetSize";

    private String mUrl;
    private int mWidth;
    private int mHeight;
    private boolean mIsDownloading = false;

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


    public void setIsDownloading(boolean isDownloading)
    {
        mIsDownloading = isDownloading;
    }

    public boolean getIsDownloading()
    {
        return mIsDownloading;
    }


    public void insertDrawable(ImageView imageView)
    {
        if (mIsDownloading) return;

        imageView.setImageDrawable(null);

        AssetCache cache = AssetCache.getInstance(imageView.getContext());
        Drawable image;

        synchronized (cache)
        {
            image = cache.get(getUrl());
        }

        if (image == null)
        {
            Log.d(TAG, "[CACHE MISS] " + getUrl());
            new MediaDownload(this, imageView).execute(this);
        } else {
            Log.d(TAG, "[CACHE HIT] " + getUrl());
            imageView.setImageDrawable(image);
        }

    }

}
