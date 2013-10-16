package com.kpcc.android;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.kpcc.android.MESSAGE";


    public void sendMessage(View view) {
        Intent intent      = new Intent(this, DisplayMessageActivity.class);
        EditText editText  = (EditText) findViewById(R.id.edit_message);
        String message     = editText.getText().toString();

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;

            case R.id.action_settings:
                openSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArticles();
        setContentView(R.layout.activity_main);
    }


    private void getArticles() {
        KPCCRestClient.get("articles", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                System.out.println(response);
            }
        });
    }


    private boolean openSearch() {
        return true;
    }


    private boolean openSettings() {
        return true;
    }

}
