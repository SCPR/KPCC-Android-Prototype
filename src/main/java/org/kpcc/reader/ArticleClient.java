package org.scpr.reader;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ArticleClient
{

    public static final String API_ROOT = "http://www.scpr.org/api/v2/";
    public static final String ENDPOINT = "articles";
    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void getCollection(
    RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
      client.get(getAbsoluteUrl(""), params, responseHandler);
    }


    public static void get(
    String id, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        client.get(getAbsoluteUrl(id), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl)
    {
      return API_ROOT + ENDPOINT + relativeUrl;
    }

}
