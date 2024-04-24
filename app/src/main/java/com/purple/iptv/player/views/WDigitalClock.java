package com.purple.iptv.player.views;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


import com.xunison.recordingplugin.BuildConfig;
import com.smartrecording.recordingplugin.app.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("AppCompatCustomView")
public class WDigitalClock extends TextView {
    private static final String m12 = "h:mm aa";
    private static final String m24 = "k:mm";
    private static final String TAG = "WDigitalClock";
    private int count = 0;
    Calendar mCalendar;
    private Context mContext;
    String mFormat;
    private FormatChangeObserver mFormatChangeObserver;
    private Handler mHandler;
    private Runnable mRefreshRunnable;
    private Runnable mTicker;
    private boolean mTickerStopped = false;
    private final BroadcastReceiver mTimeChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            WDigitalClock.this.runClock();
        }
    };

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        public void onChange(boolean z) {
            WDigitalClock.this.setFormat();
            WDigitalClock.this.runClock();
        }
    }

    public WDigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public WDigitalClock(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initClock(context);
    }

    private void initClock(Context context) {
        if (this.mCalendar == null) {
            this.mCalendar = Calendar.getInstance();
        }
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DATE_CHANGED");
        this.mContext.registerReceiver(this.mTimeChangedReceiver, intentFilter);
        this.mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(System.CONTENT_URI,
                true, this.mFormatChangeObserver);
        setFormat();
    }


    public void onAttachedToWindow() {
        Log.d("Cho", "onAttachedToWindow");
        this.mTickerStopped = false;
        super.onAttachedToWindow();
        this.mHandler = new Handler();
        this.mTicker = new Runnable() {
            public void run() {
                WDigitalClock.this.runClock();
            }
        };
        this.mTicker.run();
    }

    public void refreshLocale(Locale locale) {
        this.count = 0;
        if (locale != null) {
            setTextLocale(locale);
        }
        if (this.mRefreshRunnable == null) {
            this.mRefreshRunnable = new Runnable() {
                public void run() {
                    WDigitalClock.this.refreshClock();
                }
            };
        }
        this.mRefreshRunnable.run();
    }


    public void refreshClock() {
        setText("");
        invalidate();
        String charSequence = getText().toString();
        this.mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());
//        private static final String m12 = "h:mm aa";
//        private static final String m24 = "k:mm";




        setText(DateFormat.format(this.mFormat, this.mCalendar) );
        invalidate();
        String charSequence2 = getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("refreshClock currentText : ");
        sb.append(charSequence);
        sb.append(" / targetText : ");
        sb.append(charSequence2);
        Log.d("Cho", sb.toString());
    }


    public void runClock() {
        setFormat();
        if (!this.mTickerStopped) {
            this.mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(mFormat);
           if (BuildConfig.DEBUG) Log.e(TAG, "runClock: timezone"+ MyApplication.getInstance().getPrefManager().getTimeZone() );
            sdf.setTimeZone(TimeZone.getTimeZone(MyApplication.getInstance().getPrefManager().getTimeZone()));
//            sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
           if (BuildConfig.DEBUG) Log.e(TAG, "onItemSelected: its time:"+sdf.format(calendar.getTime()) );


           // setText(DateFormat.format(this.mFormat, this.mCalendar));
            setText(sdf.format(calendar.getTime()));
            invalidate();
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = uptimeMillis + (60000 - (uptimeMillis % 60000));
            if (this.mHandler != null) {
                this.mHandler.postAtTime(this.mTicker, j);
            }
        }
    }


    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mContext.unregisterReceiver(this.mTimeChangedReceiver);
        this.mTickerStopped = true;
    }

    private boolean get24HourMode() {
        if (MyApplication.getInstance().getPrefManager().getTimeFormat().contains("24")) {
            return true;
        } else {
            return false;
        }
        //return DateFormat.is24HourFormat(getContext());
    }


    public void setFormat() {
        if (get24HourMode()) {
            this.mFormat = m24;
        } else {
            this.mFormat = m12;
        }
    }
}
