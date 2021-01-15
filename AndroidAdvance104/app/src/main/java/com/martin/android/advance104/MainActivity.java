package com.martin.android.advance104;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("crash-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testAnr(View view) {
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testJavaCrash(View view) {
        String test = null;
        test.toLowerCase();
    }

    public void testNativeCrash(View view) {
        crash();
    }

    public native void crash();
}