package com.martin.android.advance106;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MyUncaughtExceptionHandler";

    private static HandlerThread handlerThread;
    private static Handler looperHandler;
    private static Context sContext;

    private static MyUncaughtExceptionHandler INSTANCE;
    private static long sUiThreadId;

    private MyUncaughtExceptionHandler() {

    }

    public static MyUncaughtExceptionHandler getInstance(long uiThreadId, Context context) {
        if(INSTANCE == null) {
            sUiThreadId = uiThreadId;
            sContext = context;
            handlerThread = new HandlerThread("uncaught_thread", Process.THREAD_PRIORITY_DEFAULT);
            handlerThread.start();
            looperHandler = new Handler(handlerThread.getLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    Process.killProcess((int)sUiThreadId);
                    System.exit(0);
                }
            };
            INSTANCE = new MyUncaughtExceptionHandler();
        }
        return INSTANCE;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        final long tid = Process.myTid();
        Log.d(TAG, "tid: " + tid + ", sUiThreadId: " + sUiThreadId);
        if(tid == sUiThreadId) {
            Log.d(TAG, "");
            looperHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "sContext: " + sContext);
                    Toast.makeText(sContext, "应用出现异常，即将退出！", Toast.LENGTH_LONG).show();
                }
            });
            looperHandler.sendEmptyMessageDelayed(0, 1500);
        } else {
            Log.d(TAG, "err message: " + throwable.getMessage());
            //可以判断具体是哪个线程，然后尝试重启该线程的业务，而非直接退出该app
            //此处出现异常的线程如果是用线程池来管理，那么虽然该线程异常退出了，最多只会让本次的业务处理没有完成，也不会出现app退出的情况
        }
    }
}
