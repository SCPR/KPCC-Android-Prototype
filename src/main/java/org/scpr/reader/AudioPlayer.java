package org.scpr.reader;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayer
{
    private final static String TAG = "org.scpr.reader.DEBUG.AudioPlayer";

    private MediaPlayer mAudioPlayer;


    public void play(Context c, String url)
    {
        stop();
        mAudioPlayer = new MediaPlayer();

        mAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                start();
            }
        });

        mAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                stop();
            }
        });

        setAudio(url);

    }


    public void pause()
    {
        if (mAudioPlayer != null) mAudioPlayer.pause();
    }


    public void start()
    {
        if (mAudioPlayer != null) mAudioPlayer.start();
    }


    public void stop()
    {
        if (mAudioPlayer != null)
        {
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
    }

    public boolean isPlaying()
    {
        return mAudioPlayer != null && mAudioPlayer.isPlaying();
    }

    public void setAudio(String url)
    {
        if (mAudioPlayer == null) return;

        try
        {
            mAudioPlayer.setDataSource(url);
            mAudioPlayer.prepareAsync();
        } catch(IOException e) {
            // TODO: Handle error
            e.printStackTrace();
        }
    }

}
