package com.martin.android.advance106;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "onCreate: " + Process.myTid());

        uncaughtExceptionHandler = MyUncaughtExceptionHandler.getInstance(Process.myTid(), base);
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
