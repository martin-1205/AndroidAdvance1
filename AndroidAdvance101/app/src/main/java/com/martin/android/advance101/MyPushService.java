package com.martin.android.advance101;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class MyPushService extends Service {
    private static final String TAG = "MyPushService";

    public MyPushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final int threadId = Process.myTid();
        final int threadPriority = Process.getThreadPriority(threadId);
        // D/MyPushService: onCreate threadId: 5192, threadPriority: 0
        Log.d(TAG, "onCreate threadId: " + threadId + ", threadPriority: " + threadPriority);
    }
}