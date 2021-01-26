package com.martin.android.advance110;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = BuildConfig.LOG_DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logd("onCreate: " + Constants.sChannel);
    }

    private void logd(String msg) {
        if(DEBUG) {
            Log.d(TAG, msg);
        }
    }
}