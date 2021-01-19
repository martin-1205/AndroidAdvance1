package com.martin.android.advance107;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private HandlerThread workHandlerThread;
    private Handler workHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workHandlerThread = new HandlerThread("work_handler_thread", Process.THREAD_PRIORITY_DEFAULT);
        workHandlerThread.start();
        workHandler = new Handler(workHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //此处传入的handler应使用局部变量
        MyWatchdog.getInstance(this).addThread(new Handler(workHandlerThread.getLooper()));
    }

    public void uiThreadSleep(View view) {
        try {
            Thread.sleep(90 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void workThreadSleep(View view) {
        workHandler.sendEmptyMessage(0);
    }
}