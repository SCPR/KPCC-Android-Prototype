package org.scpr.reader;

import android.net.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Asset
{

    private String mTitle;
    private String mCaption;
    private String mCredit;
    private AssetSize mSizeThumbnail;
    private AssetSize mSizeSmall;
    private AssetSize mSizeLarge;
    private AssetSize mSizeFull;


    public static Asset buildFromJson(JSONObject jsonAsset)
    {
        Asset asset = new Asset();

        try
        {
            asset.setTitle(jsonAsset.getString("title"));
            asset.setCaption(jsonAsset.getString("caption"));
            asset.setCredit(jsonAsset.getString("owner"));

            asset.setSizeThumbnail(AssetSize.buildFromJson(jsonAsset.getJSONObject("thumbnail")));
            asset.setSizeSmall(AssetSize.buildFromJson(jsonAsset.getJSONObject("small")));
            asset.setSizeLarge(AssetSize.buildFromJson(jsonAsset.getJSONObject("large")));
            asset.setSizeFull(AssetSize.buildFromJson(jsonAsset.getJSONObject("full")));

        } catch(JSONException e) {
            e.printStackTrace();
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return asset;
    }


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }


    public String getCaption()
    {
        return mCaption;
    }

    public void setCaption(String caption)
    {
        mCaption = caption;
    }



    public String getCredit()
    {
        return mCredit;
    }

    public void setCredit(String credit)
    {
        mCredit = credit;
    }


    public AssetSize getSizeThumbnail()
    {
        return mSizeThumbnail;
    }

    public void setSizeThumbnail(AssetSize assetSize)
    {
        mSizeThumbnail = assetSize;
    }


    public AssetSize getSizeSmall()
    {
        return mSizeSmall;
    }

    public void setSizeSmall(AssetSize assetSize)
    {
        mSizeSmall = assetSize;
    }


    public AssetSize getSizeLarge()
    {
        return mSizeLarge;
    }

    public void setSizeLarge(AssetSize assetSize)
    {
        mSizeLarge = assetSize;
    }


    public AssetSize getSizeFull()
    {
        return mSizeFull;
    }

    public void setSizeFull(AssetSize assetSize)
    {
        mSizeFull = assetSize;
    }

}
