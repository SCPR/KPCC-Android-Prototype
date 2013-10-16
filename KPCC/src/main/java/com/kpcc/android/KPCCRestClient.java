package com.kpcc.android;

import com.loopj.android.http.*;


public class KPCCRestClient {

    public static final String      BASE_URI  = "http://www.scpr.org/api/v3/";
    private static AsyncHttpClient  client    = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URI + relativeUrl;
    }
}
