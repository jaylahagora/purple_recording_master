package com.smartrecording.recordingplugin.utils;

import android.content.Context;
import android.util.Log;

import com.fof.android.vlcplayer.utils.UtilMethods;


public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MyExceptionHandler";
    private Context mContext;

    public MyExceptionHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        UtilMethods.LogMethod("aaas1234_thread", String.valueOf(thread.getName()));
        UtilMethods.LogMethod("aaas1234_throwable", String.valueOf(throwable.getMessage()));
        Log.e(TAG, "uncaughtException: "+throwable.getMessage() );
       // CommonMethods.onCrash(mContext);
    }
}
