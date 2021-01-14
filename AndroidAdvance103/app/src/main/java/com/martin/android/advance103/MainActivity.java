package com.martin.android.advance103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int uiThreadId;

    private Handler mChildHandler;
    private Handler mChildHandler2;

    private Thread looperThread;
    private HandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiThreadId = Process.myTid();
        //self create looper in runnable
        //final Thread looperThread = new Thread(new MyRunnable());
        looperThread = new Thread(new MyRunnable());
        looperThread.setName("looper_thread");
        looperThread.start();

        //create looper in HandlerThread
        //final HandlerThread handlerThread = new HandlerThread("work_thread_2", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread = new HandlerThread("handler_thread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mChildHandler2 = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            handlerThread.quitSafely();
            mChildHandler.getLooper().quitSafely();
        } catch (Exception ex) {

        }
    }

    public void showToastOnWorkThread(View view) {
        if(mChildHandler == null) {
            return;
        }
        mChildHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "I am in work thread! [uiThreadId: "
                        + uiThreadId
                        + ", workThreadId: " + Process.myTid(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showToastOnHandlerThread(View view) {
        if(mChildHandler2 == null) {
            return;
        }
        mChildHandler2.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "I am in handler thread! [uiThreadId: "
                        + uiThreadId
                        + ", handlerThreadId: " + Process.myTid(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void toThreadPriorityActivity(View view) {
        this.startActivity(new Intent(this, ThreadPriorityActivity.class));
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Looper.prepare();
            mChildHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                }
            };
            Looper.loop();
        }
    }
}