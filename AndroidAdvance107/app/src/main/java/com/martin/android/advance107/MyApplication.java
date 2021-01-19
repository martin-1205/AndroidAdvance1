package com.martin.android.advance107;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MyWatchdog.getInstance(base).addThread(new Handler(Looper.getMainLooper()));
    }
}
