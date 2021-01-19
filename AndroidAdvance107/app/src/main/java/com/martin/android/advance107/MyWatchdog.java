package com.martin.android.advance107;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyWatchdog {
    private static final String TAG = "MyWatchdog";

    private static MyWatchdog sMyWatchdog = null;
    private HandlerThread mCheckThread;
    private Handler mCheckHandler;
    private final List<WeakReference<Handler>> mCheckHandlerRefList = new ArrayList<>();
    private Context mContext;

    private MyWatchdog() {
    }

    private MyWatchdog(Context context) {
        mContext = context;
        mCheckThread = new HandlerThread("watchdog_check", Process.THREAD_PRIORITY_BACKGROUND);
        mCheckThread.start();
        mCheckHandler = new Handler(mCheckThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //Log.d(TAG, "mCheckHandler handleMessage");
                if(msg.what == 0) {
                    try {
                        check();
                    } catch (Exception ex) {
                    }
                    mCheckHandler.sendEmptyMessageDelayed(0, 5000);
                }
            }
        };
        mCheckHandler.sendEmptyMessageDelayed(0, 5000);
    }

    public static MyWatchdog getInstance(Context context) {
        if(sMyWatchdog == null) {
            sMyWatchdog = new MyWatchdog(context);
        }
        return sMyWatchdog;
    }

    public void addThread(Handler handler) {
        synchronized (this) {
            final WeakReference<Handler> handlerWeakReference = new WeakReference<>(handler);
            mCheckHandlerRefList.add(handlerWeakReference);
        }
    }

    private void check() {
        synchronized (this) {
            final Iterator<WeakReference<Handler>> iterator = mCheckHandlerRefList.iterator();
            while (iterator.hasNext()) {
                final WeakReference<Handler> handlerWeakReference = iterator.next();
                final Handler handler = handlerWeakReference.get();
                Log.d(TAG, "run: handler = " + handler);
                if(handler == null) {
                    iterator.remove();
                } else {
                    final Thread thread = handler.getLooper().getThread();
                    Log.d(TAG, "run: " + thread.getName() + ", " + thread.getState().name());
                    if(thread.getState().equals(Thread.State.TERMINATED)) {
                        iterator.remove();
                    } else {
                        if(!handler.hasMessages(-1010)) {
                            handler.sendEmptyMessage(-1010);
                        } else {
                            mCheckHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "thread: \'" + thread.getName() + "\' may be not response!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }
        }
    }

}
