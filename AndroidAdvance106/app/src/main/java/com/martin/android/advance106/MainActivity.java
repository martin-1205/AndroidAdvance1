package com.martin.android.advance106;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Handler uiHandler;

    private HandlerThread handlerThread;
    private long handlerThreadId;
    private Handler workHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //MainActivity: uncaughtExceptionHandler: com.android.internal.os.RuntimeInit$KillApplicationHandler@fb32c81
        Log.d(TAG, "uncaughtExceptionHandler: " + uncaughtExceptionHandler);
        uiHandler = new Handler(Looper.myLooper());

        handlerThread = new HandlerThread("bg_handler_thread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        handlerThread.quitSafely();
        super.onDestroy();
    }

    public void uiThreadCrash(View view) {
        Toast.makeText(this, "uiThreadId: " + Process.myTid(), Toast.LENGTH_SHORT).show();

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                String msg = null;
                if(msg.equals("")) {
                }
            }
        });
    }

    public void workThreadCrash(View view) {
        workHandler.post(new Runnable() {
            @Override
            public void run() {
                handlerThreadId = Process.myTid();
                String msg = null;
                if(msg.equals("")) {
                }
            }
        });

        try {
            if(handlerThreadId == 0) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "workThreadId: " + handlerThreadId + ": " + handlerThread.getState().name(), Toast.LENGTH_SHORT).show();
    }

    public void executorThreadCrash(View view) {
        AppExecutors.getInstance().executor().execute(new Runnable() {
            @Override
            public void run() {
                final int tid = Process.myTid();
                final String state = Thread.currentThread().getState().name();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "executorThreadId: " + tid + ": " + state, Toast.LENGTH_SHORT).show();
                    }
                });

                String msg = null;
                if(msg.equals("")) {

                }
            }
        });


    }
}