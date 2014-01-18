package org.scpr.reader;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class LiveStreamActivity extends DrawerActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
        {
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                .replace(R.id.content_frame, LiveStreamFragment.newInstance())
                .commit();
        }
    }


    @Override
    protected int getMainLayoutId()
    {
        return R.layout.drawer_with_frame_layout;
    }

}
