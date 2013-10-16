package com.kpcc.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        Intent intent      = getIntent();
        String message     = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView  = new TextView(this);

        textView.setTextSize(40);
        textView.setText(message);

        setContentView(textView);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
