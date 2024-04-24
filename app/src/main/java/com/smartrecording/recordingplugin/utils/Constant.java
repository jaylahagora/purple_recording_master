package com.smartrecording.recordingplugin.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.xunison.recordingplugin.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constant {
    private static final String TAG = "Constant";
    public static String BUNDLE_REQTYPE = "bundlereqtype";
    public static String INTENT_KEY_INSERT = "intentkeyinsert";
    public static String INTENT_REQTYPE = "intentreqtype";
    public static String REQTYPE_INSERTANDSCHDULE = "insertandsc";
    public static String REQTYPE_DIRECTSTARTRECORD = "directrecord";


    // for broadcast receiver speed
    public static String _downloadstatus = "downloadstatus";
    public static String _REMAININGTIIME = "remainingtime";
    public static String _REMAININGTIIMESTR = "remainingtimestr";
    public static String _downloadspeed = "downloadspeed";
    public static String MY_TRIGGER = "trigger";
    public static String MY_TRIGGER1 = "trigger1";
    public static String RECORDING_FILENAME = "r_filename";

    public static String[] pkgname = new String[]{"com.purple.iptv.player",
            "com.xunison.x.player",
            "com.choice.iptv.player",
            "com.streamforus.iptv.player",
            "com.playermove.iptv.player",
            "com.stelevision.iptv.player",
            "com.demo.iptv.player",
            "com.mastermind.iptv.player",
            "com.cloud.q.player",
            "com.maxconnect.iptv.player",
            "com.teqiqtv.iptv.box",
            "com.utvgo.iptv.player",
            "com.connect.plus.iptv",
            "com.viper.iptv1010.player",
            "com.fullhd.iptv.player",
            "com.mediacloud.tv.player",
            "com.evenc.iptv.player",
            "com.vortex.v.play",
            "com.supremacy.plex.player",
            "com.all.star.player",
            "com.super.media.player",
            "com.tvfor.everyone.player",
            "com.yugo.fourfive.player",
            "com.vue.media.player",
            "com.darknet.tv.player",
            "com.tv.two.player",
            "com.alpha.omega.player",
            "com.ontv.turbo.player",
            "com.king.cable.player",
            "com.hd.iptv.player",
            "com.hd.iptv.player",
            "com.blacklabel.stream.player",
            "com.choice.one.player",
            "com.mustard.media.player",
            "com.threettv.iptv.player",
            "com.pstv.iptv.player",
            "com.bullseye.cable.player"};


    public static String printDifference(long startDate, long endDate, String s) {
        try {


            //String string1 = String.valueOf(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.US);
            Date date = new Date(startDate);
            sdf.format(date);


            Date date1 = new Date(endDate);
            sdf.format(date1);


            //milliseconds
            long different = date.getTime() - date1.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;
            long elapsedSeconds = different / secondsInMilli;
//            Log.e(TAG, "printDifference: -----------------------");
//            Log.e(TAG, "printDifference: elapsedDays:" + elapsedDays);
//            Log.e(TAG, "printDifference: elapsedHours:" + elapsedHours);
//            Log.e(TAG, "printDifference: elapsedMinutes:" + elapsedMinutes);
//            Log.e(TAG, "printDifference: elapsedSeconds:" + elapsedSeconds);
            String edays, emins, ehours, esec;
            if (elapsedDays == 0) {
                edays = "";
            } else {

                if (elapsedDays > 1) {
                    edays = elapsedDays + " Days ";
                } else {
                    edays = elapsedDays + " Day ";
                }
            }
            if (elapsedMinutes == 0) {
                emins = "";
            } else {
                emins = elapsedMinutes + " Min ";
            }
            if (elapsedSeconds == 0) {
                esec = "";
            } else {
                esec = elapsedSeconds + " Sec ";
            }
            if (elapsedHours == 0) {
                ehours = "";
            } else {
                ehours = elapsedHours + " Hour ";
            }
            //    Log.e(TAG, "printDifference: is:" + edays + ehours + emins + esec);
            String returnstr = s + edays + ehours + emins + esec;
            return returnstr.replace("-", " ");
//            System.out.printf(
//                    "%d days, %d hours, %d minutes, %d seconds%n",
//                    elapsedDays,
//                    elapsedHours, elapsedMinutes, elapsedSeconds);

        } catch (Exception e) {
            Log.e(TAG, "printDifference: catch:" + e.getMessage());
            return "";
        }
    }

    public static boolean isAndroid10_or_Above() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.Q;
    }

    public static String getAndroid10AbovePath(Context context) {
        String path = getAndroid11Dir(context);
        if (path != null && !path.contains(context.getResources().getString(R.string.defaultrecordpath))) {
            path = path + "/" + context.getResources().getString(R.string.defaultrecordpath);
        }
        return path;
    }

    public static String getAndroid11Dir(Context context) {
        Log.e(TAG, "getAndroid11Dir: " + ContextCompat.getExternalFilesDirs(context, null)[0].getAbsolutePath() + "->  instance of " + context.getClass());
        return ContextCompat.getExternalFilesDirs(context, null)[0].getAbsolutePath();
    }
}
