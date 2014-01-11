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
        mAssetSize.setDrawable(image);
        mAssetSize.insertDrawable(mView);
    }

}
