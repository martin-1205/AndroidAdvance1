package com.martin.android.advance101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int threadId = Process.myTid();
        final int threadPriority = Process.getThreadPriority(threadId);
        //public static final int THREAD_PRIORITY_VIDEO = -10;
        // D/MainActivity: onCreate threadId: 4628, threadPriority: -10
        Log.d(TAG, "onCreate threadId: " + threadId + ", threadPriority: " + threadPriority);

        this.startService(new Intent(this, MyPushService.class));

        new Thread(new MyRunnable()).start();
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            final int threadId = Process.myTid();
            int threadPriority = Process.getThreadPriority(threadId);
            //public static final int THREAD_PRIORITY_DEFAULT = 0;
            // D/MainActivity: onCreate uiThreadId: 4628, uiThreadPriority: -10
            Log.d(TAG, "MyRunnable run threadId: " + threadId + ", threadPriority: " + threadPriority);

            //THREAD_PRIORITY_BACKGROUND
            Process.setThreadPriority(threadId, Process.THREAD_PRIORITY_BACKGROUND);
            threadPriority = Process.getThreadPriority(threadId);
            //public static final int THREAD_PRIORITY_DEFAULT = 0;
            // D/MainActivity: onCreate uiThreadId: 4628, uiThreadPriority: -10
            Log.d(TAG, "MyRunnable run --2-- threadId: " + threadId + ", threadPriority: " + threadPriority);

            //THREAD_PRIORITY_VIDEO
            Process.setThreadPriority(threadId, Process.THREAD_PRIORITY_VIDEO);
            threadPriority = Process.getThreadPriority(threadId);
            //public static final int THREAD_PRIORITY_DEFAULT = 0;
            // D/MainActivity: onCreate uiThreadId: 4628, uiThreadPriority: -10
            Log.d(TAG, "MyRunnable run --3-- threadId: " + threadId + ", threadPriority: " + threadPriority);
        }
    }
}