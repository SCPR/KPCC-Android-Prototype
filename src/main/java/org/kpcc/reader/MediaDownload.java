package org.kpcc.reader;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MediaDownload extends AsyncTask<AssetSize, Void, Drawable>
{
    private ImageView mView;
    private AssetSize mAssetSize;


    public MediaDownload(AssetSize assetSize, ImageView view)
    {
        mAssetSize = assetSize;
        mView = view;
    }


    protected void onPreExecute()
    {
        // Make sure the system only attempts to download this image once
        // at a time.
        if (mAssetSize.getIsDownloading())
        {
            cancel(true);
        } else {
            mAssetSize.setIsDownloading(true);
        }
    }


    protected Drawable doInBackground(AssetSize... sizes)
    {
        Drawable image = null;

        try
        {
            URL url = new URL(sizes[0].getUrl());
            InputStream is = (InputStream)url.getContent();
            image = Drawable.createFromStream(is, "src");

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return image;
    }


    protected void onPostExecute(Drawable image)
    {
        mAssetSize.setIsDownloading(false);
        mView.setImageDrawable(image);
        AssetCache cache = AssetCache.getInstance(mView.getContext());

        synchronized (cache)
        {
            // We'll use the asset's URL as the key, since we know that will be unique.
            // That also has the benefit that if the asset changes on the server,
            // the cache will be invalidated.
            cache.set(mAssetSize.getUrl(), image);
        }
    }

}
