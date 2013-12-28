package com.kpcc.android;

import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ArticlesActivity extends Activity {
    public final String DEBUG_TAG = "Articles";
    public final String API_ROOT = "http://scpr.org/api/v3/";
    public final String ENDPOINT = "articles";

    private TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        textView = (TextView) findViewById(R.id.articles);

        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchArticlesTask().execute(apiUrl(ENDPOINT));
        } else {
            textView.setText("No network connection available.");
        }
    }



    private String apiUrl(String endpoint) {
        return API_ROOT + endpoint;
    }


    private class FetchArticlesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return fetchArticles(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }


        public String readIt(InputStream stream, int len)
        throws IOException, UnsupportedEncodingException {
           Reader reader = null;
           reader = new InputStreamReader(stream, "UTF-8");

           char[] buffer = new char[len];
           reader.read(buffer);
           return new String(buffer);
       }


       private String fetchArticles(String strUrl) throws IOException {
           InputStream is = null;

           try {
               URL url = new URL(strUrl);

               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setReadTimeout(10000);
               conn.setConnectTimeout(15000);
               conn.setRequestMethod("GET");
               conn.setDoInput(true);

               conn.connect();
               int response = conn.getResponseCode();
               Log.d(DEBUG_TAG, "The response is: " + response);
               is = conn.getInputStream();

               String contentAsString = readIt(is, 999);
               return contentAsString;

           } finally {
               if (is != null) {
                   is.close();
               } 
           }
       }

    }
}
