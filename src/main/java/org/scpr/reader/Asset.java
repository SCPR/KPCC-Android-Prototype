package org.scpr.reader;

import org.json.JSONException;
import org.json.JSONObject;

public class Asset extends Entity
{

    private String mTitle;
    private String mCaption;
    private String mCredit;
    private AssetNativeType mNativeType;
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

            if (jsonAsset.has("native"))
            {
                JSONObject nativeType = jsonAsset.getJSONObject("native");
                asset.setNativeType(AssetNativeType.buildFromJson(nativeType));
            }

        } catch(JSONException e) {
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
        this.mTitle = title;
    }


    public String getCaption()
    {
        return mCaption;
    }

    public void setCaption(String caption)
    {
        this.mCaption = caption;
    }



    public String getCredit()
    {
        return mCredit;
    }

    public void setCredit(String credit)
    {
        this.mCredit = credit;
    }


    public AssetSize getSizeThumbnail()
    {
        return mSizeThumbnail;
    }

    public void setSizeThumbnail(AssetSize assetSize)
    {
        this.mSizeThumbnail = assetSize;
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
        this.mSizeLarge = assetSize;
    }


    public AssetSize getSizeFull()
    {
        return mSizeFull;
    }

    public void setSizeFull(AssetSize assetSize)
    {
        this.mSizeFull = assetSize;
    }


    public AssetNativeType getNativeType()
    {
        return mNativeType;
    }

    public void setNativeType(AssetNativeType nativeType)
    {
        this.mNativeType = nativeType;
    }

}
