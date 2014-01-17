package org.scpr.reader;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by bricker on 1/16/14.
 */
public class LiveStreamFragment extends Fragment
{

    private MediaPlayer mPlayer;
    private MediaController mController;
    private View mView;


    public static LiveStreamFragment newInstance()
    {
        LiveStreamFragment liveStreamFragment = new LiveStreamFragment();
        return liveStreamFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.activity_live_stream, parent, false);

        mPlayer = new MediaPlayer();
        mController = new MediaController(getActivity());

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mController.setMediaPlayer(new MediaController.MediaPlayerControl()
                {
                    @Override
                    public void start()
                    {
                        mPlayer.start();
                    }

                    @Override
                    public void pause()
                    {
                        mPlayer.pause();
                    }

                    @Override
                    public int getDuration()
                    {
                        return mPlayer.getDuration();
                    }

                    @Override
                    public int getCurrentPosition()
                    {
                        return mPlayer.getCurrentPosition();
                    }

                    @Override
                    public void seekTo(int pos)
                    {
                        mPlayer.seekTo(pos);
                    }

                    @Override
                    public boolean isPlaying()
                    {
                        return mPlayer.isPlaying();
                    }

                    @Override
                    public int getBufferPercentage()
                    {
                        return 0;
                    }

                    @Override
                    public boolean canPause()
                    {
                        return true;
                    }

                    @Override
                    public boolean canSeekBackward()
                    {
                        return true;
                    }

                    @Override
                    public boolean canSeekForward()
                    {
                        return true;
                    }

                    @Override
                    public int getAudioSessionId()
                    {
                        return mPlayer.getAudioSessionId();
                    }
                });

                mController.setAnchorView(mView);
                mController.setEnabled(true);
                mController.show(0);
            } // onPrepared
        });

        try
        {
            mPlayer.setDataSource("http://live.scpr.org/");
            mPlayer.prepareAsync();
        } catch(IOException e) {
            // TODO: Handle error
            e.printStackTrace();
        }

        return mView;
    }
}
