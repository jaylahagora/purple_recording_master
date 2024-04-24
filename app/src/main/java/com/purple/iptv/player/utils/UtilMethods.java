package com.purple.iptv.player.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.xunison.recordingplugin.BuildConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class UtilMethods {

    private static final String TAG = "UtilMethods";
    public static boolean isKeyboardOpen = false;

    public static void LogMethod(String a, String b) {
        if (BuildConfig.DEBUG) {
            Log.d(a, b);
        }
    }

    public static void hideKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        isKeyboardOpen = false;
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isKeyboardOpen() {
        return isKeyboardOpen;
    }

    public static void showKeyboard(Context context, View view) {
        UtilMethods.LogMethod("keyboard1234_", String.valueOf(view));
        UtilMethods.LogMethod("keyboard1234_", String.valueOf(view.requestFocus()));
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            UtilMethods.LogMethod("keyboard1234_imm", String.valueOf(imm));
            if (imm != null) {
                UtilMethods.LogMethod("keyboard1234_", String.valueOf("ifff"));
                isKeyboardOpen = true;
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static int dpToPx(int dp) {
        UtilMethods.LogMethod("display123_density", String.valueOf(Resources.getSystem().getDisplayMetrics().density));
        UtilMethods.LogMethod("display123_dp", String.valueOf(dp));
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String convertTime(String dateString, String inputFormat, String outputFormat) {
        String resultTime = null;
        if (dateString != null && inputFormat != null & outputFormat != null) {
            SimpleDateFormat getTimeZoneFormat = new SimpleDateFormat(inputFormat);
            Date date_timezone = null;
            UtilMethods.LogMethod("timeZone1234_dateString", String.valueOf(dateString));
            try {
                date_timezone = getTimeZoneFormat.parse(dateString);
            } catch (ParseException e) {
                UtilMethods.LogMethod("timeZone1234_eeeee", String.valueOf(e));
                e.printStackTrace();
            }
            UtilMethods.LogMethod("timeZone1234_timeZone", String.valueOf(date_timezone));

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

            resultTime = outputDateFormat.format(date_timezone);
            UtilMethods.LogMethod("timeZone1234_outputFormat", String.valueOf(outputFormat));
            UtilMethods.LogMethod("timeZone1234_resultTime", String.valueOf(resultTime));
        }

        return resultTime;
    }

    public static String getCurrentTime(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(c.getTime());
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static String convertMilliToTime(long totalMilli, boolean notation) {
        totalMilli = totalMilli / 1000;
        int hours = (int) (totalMilli / 3600);
        int minutes = (int) ((totalMilli % 3600) / 60);
        int seconds = (int) (totalMilli % 60);

        if (hours == 0) {
            if (notation) {
                return String.format("%02dm %02ds", minutes, seconds);
            } else {
                return String.format("%02d:%02d", minutes, seconds);
            }

        } else {
            if (notation) {
                return String.format("%02dh %02dm", hours, minutes);
            } else {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }

        }

    }

    private static SimpleDateFormat getTimeZoneFormat =
            new SimpleDateFormat("yyyyMMddHHmmss Z");
    private static SimpleDateFormat getDateZoneFormat =
            new SimpleDateFormat("dd-MM-yyyy");

    @SuppressLint("SimpleDateFormat")
    public static long getDateInLocalMilli(String dateString, String inputDateFormat) {
        Date date_timezone = null;
        long milliseconds_new = -1;
        if (dateString != null) {
            try {
                date_timezone = getTimeZoneFormat.parse(dateString);
                milliseconds_new = date_timezone.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return milliseconds_new;
    }

    @SuppressLint("SimpleDateFormat")
    public static long getDateInLocalMilliDynamicFormat(String dateString, String inputDateFormat) {
        Date date_timezone = null;
        long milliseconds_new = -1;
        if (dateString != null) {
            SimpleDateFormat getTimeZoneFormat = new SimpleDateFormat(inputDateFormat);
            try {
                date_timezone = getTimeZoneFormat.parse(dateString);
                milliseconds_new = date_timezone.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                if (BuildConfig.DEBUG)
                    Log.e(TAG, "getDateInLocalMilliDynamicFormat: error" + e.getMessage());
            }
        }
        return milliseconds_new;
    }

    public static String getTimeFromMilli(long milli) {
        Date date = new Date(milli);
        //  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss Z");
        return getTimeZoneFormat.format(date);
    }

    public static String getTimeFromMilli(long milli, String format) {
        if (milli != -1) {
            UtilMethods.LogMethod("expiry1234_milli", String.valueOf(milli));
            Date date = new Date(milli);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        }
        return null;
    }

    public static String getDateFromMilli(long milli) {
        if (milli != -1) {
            UtilMethods.LogMethod("expiry1234_milli", String.valueOf(milli));
            Date date = new Date(milli);
            return getDateZoneFormat.format(date);
        }
        return null;
    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        return width;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Drawable getAppLogoFromPackageName(Context mContext, String package_name) {
        Drawable icon = null;
        PackageManager packageManager = mContext.getPackageManager();
        try {
            icon = packageManager.getApplicationIcon(package_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }


    public static boolean appInstalledOrNot(Context mContext, String uri) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    public static String convertByteToMB(long byteCount) {
        if (byteCount > 0) {
            return (int) byteCount / (1024 * 1024) + " MB";
        }
        return "0 MB";
    }

    public static double getDeviceSizeInInch(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y) * dm.scaledDensity;
        UtilMethods.LogMethod("debug123_", "Screen inches : " + screenInches);
        return screenInches;

    }

    public static String convertTimeToTimezone(long milli, String timezone) {
        String result = "";
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm", Locale.US);
        //sourceFormat.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
        Date date = new Date(milli); // => Date is in UTC now
        try {
            // parsed = sourceFormat.parse(time);
            TimeZone tz = TimeZone.getTimeZone(timezone);
            sourceFormat.setTimeZone(tz);
            result = sourceFormat.format(date);
            UtilMethods.LogMethod("time1234_", String.valueOf(result));
        } catch (Exception e) {
            UtilMethods.LogMethod("time1234_eeee", String.valueOf(e));
            e.printStackTrace();
        }
        return result;
    }

    public static String convertmilisectodattime(String mils) {
        SimpleDateFormat simple = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        simple.setTimeZone(TimeZone.getDefault());
        // Creating date from milliseconds
        // using Date() constructor


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(mils));
        return simple.format(new Date(Long.parseLong(mils) * 1000));
    }

    private static final long B = 1;
    private static final long KB = B * 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;

    public static String parseSpeed(double bytes, boolean inBits) {
        double value = inBits ? bytes * 8 : bytes;
        if (value < KB) {
            return String.format(Locale.getDefault(), "%.1f " + (inBits ? "b" : "B") + "/s", value);
        } else if (value < MB) {
            return String.format(Locale.getDefault(), "%.1f K" + (inBits ? "b" : "B") + "/s", value / KB);
        } else if (value < GB) {
            return String.format(Locale.getDefault(), "%.1f M" + (inBits ? "b" : "B") + "/s", value / MB);
        } else {
            return String.format(Locale.getDefault(), "%.2f G" + (inBits ? "b" : "B") + "/s", value / GB);
        }
    }

    public static boolean betweenExclusive(long x, long min, long max) {
        return x > min && x < max;
    }

    public static String getAppNameFromPackageName(Context mContext, String package_name) {
        String name = "Unknown";
        if (package_name.equalsIgnoreCase(Config.SETTINGS_DEFAULT_PLAYER) ||
                package_name.equalsIgnoreCase(Config.SETTINGS_IJK_PLAYER) ||
                package_name.equalsIgnoreCase(Config.SETTINGS_EXO_PLAYER)) {
            return package_name;
        } else {
            PackageManager packageManager = mContext.getPackageManager();
            try {
                name = packageManager.getApplicationLabel(packageManager.
                        getApplicationInfo(package_name, PackageManager.GET_META_DATA)).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return name;
        }
    }
    public static long gettimewithplus1mins(long endtime) {
        return endtime + TimeUnit.MINUTES.toMillis(1);
    }
    public static long gettimewithless1min(long endtime) {
        return endtime - TimeUnit.MINUTES.toMillis(1);
    }
}
