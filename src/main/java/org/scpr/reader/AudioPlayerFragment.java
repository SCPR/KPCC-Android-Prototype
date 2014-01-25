package org.scpr.reader;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.scpr.api.Article;
import org.scpr.api.Audio;

import java.io.IOException;

public class AudioPlayerFragment extends Fragment
{

    public static String EXTRA_TITLE = "org.scpr.reader.AudioPlayerFragment.EXTRA_TITLE";
    public static String EXTRA_URL = "org.scpr.reader.AudioPlayerFragment.EXTRA_URL";

    private TextView mTitle;
    private MediaPlayer mAudioPlayer;
    private boolean mAudioPrepared;


    public AudioPlayerFragment newInstance(String title, String url)
    {
        Bundle args = new Bundle();
        args.putString(EXTRA_TITLE, title);
        args.putString(EXTRA_URL, url);

        AudioPlayerFragment fragment = new AudioPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_audio_player, parent, false);

        mTitle = (TextView) v.findViewById(R.id.audio_title);

        mAudioPlayer = new MediaPlayer();
        mAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mAudioPrepared = true;
            }
        });

        return v;
    }


    public void setAudio(Article article, Audio audio)
    {
        mAudioPlayer.stop();
        mTitle.setText(article.getTitle());

        try
        {
            mAudioPlayer.setDataSource(audio.getUrl());
            mAudioPlayer.prepareAsync();
        } catch(IOException e) {
            // TODO: Handle error
            e.printStackTrace();
        }
    }
}
