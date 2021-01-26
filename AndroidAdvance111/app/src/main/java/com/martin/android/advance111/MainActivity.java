package com.martin.android.advance111;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static {
        System.loadLibrary("native-lib");
    }

    private HandlerThread mHandlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandlerThread = new HandlerThread("handler_thread", Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();

        //start java sleep test
        //startJavaSleepTest();

        //start native sleep test
        startNativeSleepTest();
    }

    public void startFgService(View view) {
        //start fg service for data sync
        this.startService(new Intent(this, FgService.class));
    }

    public void stopFgService(View view) {
        //stop fg service for data sync
        this.stopService(new Intent(this, FgService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quitSafely();
    }

    private void startJavaSleepTest() {
        final Handler handler = new Handler(mHandlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //start java sleep test
                while(true) {
                    final long start = System.currentTimeMillis();
                    try {
                        Thread.sleep(5);
                    } catch (Exception ex) {

                    }
                    final long end = System.currentTimeMillis();
                    Log.d(TAG, "delay: " + (end - start) + "ms");
                }
            }
        });
    }

    public native String startNativeSleepTest();
}