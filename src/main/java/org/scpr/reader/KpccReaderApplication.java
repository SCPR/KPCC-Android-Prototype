package org.scpr.reader;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class KpccReaderApplication extends Application
{
    public static long LIVESTREAM_LAST_PREROLL = 0L;

    @Override
    public void onCreate()
    {
        super.onCreate();

        DisplayImageOptions defaultOptions =
            new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .resetViewBeforeLoading(true)
            .build();

        ImageLoaderConfiguration config =
            new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();

        ImageLoader.getInstance().init(config);
    }
}
