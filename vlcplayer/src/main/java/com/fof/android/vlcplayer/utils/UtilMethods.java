package com.fof.android.vlcplayer.utils;

import android.content.Context;

import androidx.annotation.AttrRes;

import com.fof.android.vlcplayer.BuildConfig;

public class UtilMethods {

    public static void LogMethod(String key, String value) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(key, value);
        }
    }

    public static android.graphics.drawable.Drawable resolveDrawable(Context context, @AttrRes int attr) {
        return resolveDrawable(context, attr);
    }

    public static String convertMiliToTime(long totalSecs) {
        totalSecs = totalSecs / 1000;
        int hours = (int) (totalSecs / 3600);
        int minutes = (int) ((totalSecs % 3600) / 60);
        int seconds = (int) (totalSecs % 60);
        if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);

        }

    }
}
