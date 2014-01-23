package org.scpr.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AudioPlayerFragment extends Fragment
{

    public static String EXTRA_TITLE = "org.scpr.reader.AudioPlayerFragment.EXTRA_TITLE";
    public static String EXTRA_URL = "org.scpr.reader.AudioPlayerFragment.EXTRA_URL";

    private String mTitle;
    private String mUrl;


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
        return v;
    }

}
