package com.purple.iptv.player.utils;


import android.content.Context;

import com.valdesekamdem.library.mdtoast.MDToast;
import com.xunison.recordingplugin.BuildConfig;

public class UtilConstant {
    public static int LIVE_AD_MARGIN = 0;
    public static int selected_multi_screen = -1;
    public static String connectedServerKey = null;
    public static int currently_selected_sort = Config.SORT_BY_DEFAULT;
    public static String currently_selected_background_image = null;
    public static boolean islogshown = BuildConfig.DEBUG;
    //pkg name
    //for recordingpugin
    public static String PKGFORRECORDING = "com.purple.recording.plugin";

    //plugin send constant
    public static String BUNDLE_REQTYPE = "bundlereqtype";
    public static String INTENT_KEY_INSERT = "intentkeyinsert";
    public static String INTENT_REQTYPE = "intentreqtype";
    public static String REQTYPE_INSERTANDSCHDULE = "insertandsc";
    public static String REQTYPE_DIRECTSTARTRECORD = "directrecord";

    public static void ToastS(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public static void ToastE(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
        mdToast.show();
    }

    public static void ToastW(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        mdToast.show();
    }



    public static void ToastI(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
        mdToast.show();
    }
}
