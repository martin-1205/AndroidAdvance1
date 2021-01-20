package com.martin.android.advance108;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    private HandlerThread mFlushThread;
    private Handler mFlushHandler;

    private Handler mUiHandler;
    private int count = 0;
    private int countMax = 1000;

    private static final String TAG = "MainActivity";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        tvTest = findViewById(R.id.tv_test);

        mFlushThread = new HandlerThread("flush_thread", Process.THREAD_PRIORITY_DEFAULT);
        mFlushThread.start();
        mFlushHandler = new Handler(mFlushThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "mFlushHandler handleMessage");
                mUiHandler.sendEmptyMessage(count);
                if(count < countMax) {
                    count++;
                    if(count == countMax) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            Trace.endAsyncSection("systrace_testAsyncSysTrace", 100);
                        }
                    }
                    mFlushHandler.sendEmptyMessageDelayed(0, 10);
                }
            }
        };

        mUiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what%2) {
                    case 0:
                        tvTest.setText("testSysTrace" + sdf.format(new Date()));
                        break;
                    case 1:
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mFlushThread.quitSafely();
    }

    public void testTraceView(View view) {
        try {
            //Debug.startNativeTracing();
            Debug.startMethodTracing("traceview_testTraceView_" + sdf.format(new Date()));//buffer size default 8MB
            //Debug.startMethodTracingSampling();
            Log.d(TAG, "testTraceView: start " + view.toString());
            final Random random = new Random();
            view.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            blockTest();
            bizTest();
            Log.d(TAG, "testTraceView: end");
        } catch (Exception ex) {
        } finally {
            //Debug.stopNativeTracing();
            Debug.stopMethodTracing();
        }
    }

    public void testSysTrace(View view) {
        try {
            Trace.beginSection("systrace_testSysTrace_" + sdf.format(new Date()));
            Log.d(TAG, "testSysTrace: start " + view.toString());
            tvTest.setText("testSysTrace" + sdf.format(new Date()));
            final Random random = new Random();
            view.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            blockTest();
            tvTest.setText("testSysTrace" + sdf.format(new Date()));
            bizTest();
            Log.d(TAG, "testSysTrace: end");
        } catch (Exception ex) {

        } finally {
            Trace.endSection();
        }
    }

    public void testAsyncSysTrace(View view) {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Trace.beginAsyncSection("systrace_testAsyncSysTrace", 100);
            }
            Log.d(TAG, "testAsyncSysTrace: start " + view.toString());
            final Random random = new Random();
            view.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            mFlushHandler.sendEmptyMessage(0);
            Log.d(TAG, "testAsyncSysTrace: end");
        } catch (Exception ex) {
            Log.d(TAG, "testAsyncSysTrace: ex " + ex.toString());
        }
    }

    private void blockTest() {
        Log.d(TAG, "blockTest: start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "blockTest: end");
    }

    private void bizTest() {
        Log.d(TAG, "bizTest: start");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "bizTest: end");
    }
}