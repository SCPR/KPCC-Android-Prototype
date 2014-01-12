package org.kpcc.reader;

import android.net.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class AssetSize
{

    private final static String TAG = "org.kpcc.reader.DEBUG.AssetSize";

    private String mUrl;
    private int mWidth;
    private int mHeight;


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

}
