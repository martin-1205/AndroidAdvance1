package com.martin.android.advance103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreadPriorityActivity extends AppCompatActivity {
    private static final String TAG = "ThreadPriorityActivity";

    private Button btStart;

    private TextView tvUI;
    private TextView tvWorkDefault;
    private TextView tvWorkBg;
    private TextView tvWorkFg;

    private Handler mHandler;
    private Handler mChildHandlerBg;
    private Handler mChildHandlerDefault;
    private Handler mChildHandlerFg;

    private boolean sDestroyed = false;

    private int uiCount , workBgCount, workDefaultCount, workFgCount = 0;

    private HandlerThread handlerThreadBg;
    private HandlerThread handlerThreadDef;
    private HandlerThread handlerThreadFg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_priority);

        sDestroyed = false;
        btStart = this.findViewById(R.id.bt_start_priority_test);

        tvUI = this.findViewById(R.id.tv_ui_thread);
        tvWorkBg = this.findViewById(R.id.tv_work_thread_bg);
        tvWorkDefault = this.findViewById(R.id.tv_work_thread_default);
        tvWorkFg = this.findViewById(R.id.tv_work_thread_fg);

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                uiCount++;

                tvUI.setText("count: " + uiCount);
                tvWorkBg.setText("PRIORITY_BACKGROUND: " + workBgCount);
                tvWorkDefault.setText("PRIORITY_DEFAULT: " + workDefaultCount);
                tvWorkFg.setText("PRIORITY_FOREGROUND: " + workFgCount);

                mHandler.sendEmptyMessageDelayed(0, 10);
            }
        };

        //final HandlerThread handlerThreadBg = new HandlerThread("priority_bg", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThreadBg = new HandlerThread("priority_bg", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThreadBg.start();
        mChildHandlerBg = new Handler(handlerThreadBg.getLooper());

        //final HandlerThread handlerThreadDef = new HandlerThread("priority_def", Process.THREAD_PRIORITY_DEFAULT);
        handlerThreadDef = new HandlerThread("priority_def", Process.THREAD_PRIORITY_DEFAULT);
        handlerThreadDef.start();
        mChildHandlerDefault = new Handler(handlerThreadDef.getLooper());

        //final HandlerThread handlerThreadFg = new HandlerThread("priority_fg", Process.THREAD_PRIORITY_FOREGROUND);
        handlerThreadFg = new HandlerThread("priority_fg", Process.THREAD_PRIORITY_FOREGROUND);
        handlerThreadFg.start();
        mChildHandlerFg = new Handler(handlerThreadFg.getLooper());
    }

    @Override
    protected void onDestroy() {
        sDestroyed = true;
        try {
            //Quits the handler thread's looper safely.
            handlerThreadFg.quitSafely();
            handlerThreadDef.quitSafely();
            handlerThreadBg.quitSafely();
        } catch (Exception ex) {
        }

        super.onDestroy();
    }

    public void startPriorityTest(View view) {
        //btStart.setVisibility(View.INVISIBLE);
        btStart.setClickable(false);

        //The 'for loop code' is only for cpu load average test, close default.
        //you can see the cpu load result by 'uptime' in shell
        //for(int i=0; i<10; i++) {
        for(int i=0; i<0; i++) {
            final Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                    int c = 0;
                    while(!sDestroyed && c < Integer.MAX_VALUE) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //Log.d(TAG, Process.myTid() + " - " + c);
                        c++;
                    }
                }
            };
            final Thread thread = new Thread(runnable);
            thread.setName("multi_thread_" + i);
            thread.start();
        }

        mChildHandlerBg.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mChildHandlerBg " + Process.myTid() + " : " + Process.getThreadPriority(Process.myTid()));
                while(!sDestroyed && workBgCount <= Integer.MAX_VALUE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, Process.myTid() + " - " + workBgCount);
                    workBgCount++;
                }
            }
        });


        mChildHandlerDefault.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mChildHandlerDefault " + Process.myTid() + " : " + Process.getThreadPriority(Process.myTid()));
                while(!sDestroyed && workDefaultCount <= Integer.MAX_VALUE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, Process.myTid() + " - " + workDefaultCount);
                    workDefaultCount++;
                }
            }
        });

        mChildHandlerFg.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mChildHandlerFg " + Process.myTid() + " : " + Process.getThreadPriority(Process.myTid()));
                while(!sDestroyed && workFgCount <= Integer.MAX_VALUE) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, Process.myTid() + " - " + workFgCount + );
                    workFgCount++;
                }
            }
        });

        mHandler.sendEmptyMessage(0);
    }

}