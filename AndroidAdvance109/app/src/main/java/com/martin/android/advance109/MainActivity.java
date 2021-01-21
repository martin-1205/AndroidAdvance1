package com.martin.android.advance109;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;

public class MainActivity extends AppCompatActivity {

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "partial_wakelock_" + MainActivity.class.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    private void testNull() {
        String temp = null;
        if(temp.isEmpty()) {
            //
        }
    }
}