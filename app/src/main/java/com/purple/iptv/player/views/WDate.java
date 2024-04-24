package com.purple.iptv.player.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.smartrecording.recordingplugin.app.MyApplication;
import com.xunison.recordingplugin.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


@SuppressLint("AppCompatCustomView")
public class WDate extends TextView {
    private static final String TAG = "WDate";
    private static final String def = "dd-MMM-yyyy";
    Calendar mCalendar;
    String mFormat;
    private FormatChangeObserver mFormatChangeObserver;

    public Handler mHandler;

    public Runnable mTicker;

    public boolean mTickerStopped = false;

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        public void onChange(boolean z) {
            WDate.this.syncSystemFormat();
        }
    }

    public WDate(Context context) {
        super(context);
        initClock(context);
    }

    public WDate(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initClock(context);
    }

    private void initClock(Context context) {
        if (this.mCalendar == null) {
            this.mCalendar = Calendar.getInstance();
        }
        this.mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(System.CONTENT_URI, true, this.mFormatChangeObserver);
    }


    public void onAttachedToWindow() {
        this.mTickerStopped = false;
        super.onAttachedToWindow();
        this.mHandler = new Handler();
        this.mTicker = new Runnable() {
            public void run() {
                if (!WDate.this.mTickerStopped) {
                    WDate.this.mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());
                    if (WDate.this.mFormat == null) {
                        WDate.this.syncSystemFormat();
                    }


                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat(mFormat);
                    sdf.setTimeZone(TimeZone.getTimeZone(MyApplication.getInstance().getPrefManager().getTimeZone()));
                    //if (islogshown)
                       if (BuildConfig.DEBUG) Log.e(TAG, "onItemSelected: its date:" + sdf.format(calendar.getTime()));
                    // setText(DateFormat.format(this.mFormat, this.mCalendar));
                    //  setText(sdf.format(calendar.getTime()));
//                     WDate.this.setText(DateFormat.format(WDate.this.mFormat, WDate.this.mCalendar));
                    WDate.this.setText(sdf.format(calendar.getTime()));
                    WDate.this.invalidate();
                    long uptimeMillis = SystemClock.uptimeMillis();
                    WDate.this.mHandler.postAtTime(WDate.this.mTicker,
                            uptimeMillis + (60000 - (uptimeMillis % 60000)));
                }
            }
        };
        this.mTicker.run();
    }


    public void syncSystemFormat() {
        setFormat(def);
    }

    public void refreshLocale(Locale locale) {
        if (locale != null) {
            setTextLocale(locale);
        }
        this.mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());
        if (this.mFormat == null) {
            syncSystemFormat();
        }
        setText(DateFormat.format(this.mFormat, this.mCalendar));
    }


    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mTickerStopped = true;
    }

    public void setFormat(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFormat format : ");
        sb.append(str);
        Log.d(str2, sb.toString());
//        if (str.equals("MM-dd-yyyy") || str.equals("dd-MM-yyyy") || str.equals("yyyy-MM-dd")) {
//            String[] split = str.split("-");
//            for (int i = 0; i < split.length; i++) {
//                if (split[i].equals("dd")) {
//                    split[i] = "d";
//                } else if (split[i].equals("MM")) {
//                    split[i] = "M";
//                }
//            }
//            StringBuilder sb2 = new StringBuilder();
//            sb2.append(split[0]);
//            sb2.append(". ");
//            sb2.append(split[1]);
//            sb2.append(". ");
//            sb2.append(split[2]);
//            this.mFormat = sb2.toString();
//            return;
//        }
        this.mFormat = def;
    }
}
