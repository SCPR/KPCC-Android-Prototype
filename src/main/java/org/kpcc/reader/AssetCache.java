package org.kpcc.reader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

public class AssetCache
{

    private static AssetCache sAssetCache;

    private Context mAppContext;
    private LruCache<String, Drawable> mCache;


    private AssetCache(ActivityManager am, Context appContext)
    {
        mAppContext = appContext;

        int cacheSize = (am.getMemoryClass() * 1024 * 1024) / 4;

        mCache = new LruCache<String, Drawable>(cacheSize)
        {
            @Override
            protected int sizeOf(String key, Drawable drawable)
            {
                Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
                // API 11+ has `getByteCount()`, but since we're supporting
                // down to API 8, we need to do it this way.
                return bm.getRowBytes() * bm.getHeight();
            }
        };
    }


    public static AssetCache getInstance(Context c)
    {
        if (sAssetCache == null)
        {
            ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
            sAssetCache = new AssetCache(am, c.getApplicationContext());
        }

        return sAssetCache;
    }


    public Drawable get(String key)
    {
        return mCache.get(key);
    }

    public void set(String key, Drawable drawable)
    {
        mCache.put(key, drawable);
    }

}
