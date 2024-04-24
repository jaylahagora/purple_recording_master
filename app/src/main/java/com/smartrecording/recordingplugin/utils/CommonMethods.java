package com.smartrecording.recordingplugin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.fof.android.vlcplayer.utils.UtilMethods;
import com.purple.iptv.player.common.CustomDialogs;
import com.purple.iptv.player.common.CustomInterface;
import com.xunison.recordingplugin.R;
import com.smartrecording.recordingplugin.app.MyApplication;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;


public class CommonMethods {
    private static final String TAG = "CommonMethods";
    private static SimpleDateFormat getTimeZoneFormat =
            new SimpleDateFormat("yyyyMMddHHmmss Z", Locale.US);




    public static long getTimeInLocalMilli(String dateString) {
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
    public static SimpleDateFormat getEPGTimeFormat(Context mContext) {
        return new SimpleDateFormat(MyApplication.getInstance().getPrefManager().
                getTimeFormat().contains("24") ? "HH:mm" : "hh:mm aa", Locale.US);
    }


    public static SimpleDateFormat getEPGDateFormat(Context mContext) {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    }
    public static SimpleDateFormat getDateTIMEFormatusingmilisecond(Context mContext) {
        return new SimpleDateFormat("ddMMMyyyyHHmm", Locale.US);
    }

    public static boolean checkIsTelevisionByHardware(Context mContext) {
        boolean isAndroidTV = false;
        int uiMode = mContext.getResources().getConfiguration().uiMode;
        UtilMethods.LogMethod("scanner123_isTV", String.valueOf(isTV()));
        UtilMethods.LogMethod("scanner123_isHdmiSwitchSet", String.valueOf(isHdmiSwitchSet()));
        UtilMethods.LogMethod("tv123_isHdmiSwitchSet", String.valueOf(isHdmiSwitchSet()));
        UtilMethods.LogMethod("tv123_isTV", String.valueOf(isTV()));
        UtilMethods.LogMethod("tv123_FEATURE_TELEPHONY", String.valueOf(mContext.getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_TELEPHONY)));
        UtilMethods.LogMethod("tv123_want_123", String.valueOf(isHdmiSwitchSet()));
        if ((uiMode & Configuration.UI_MODE_TYPE_MASK) == Configuration.UI_MODE_TYPE_TELEVISION ||
                isHdmiSwitchSet() || isTV() || !mContext.getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            isAndroidTV = true;
            MyApplication.getInstance().getPrefManager().wantTVLayout(true);
            return isAndroidTV;
        }
        MyApplication.getInstance().getPrefManager().wantTVLayout(false);
        UtilMethods.LogMethod("tv123_want_2222",
                String.valueOf(MyApplication.getInstance().getPrefManager().getWantTVLayout()));
        return isAndroidTV;
    }

    public static boolean checkIsTelevision(Context mContext) {

        boolean isAndroidTV = false;
        if (MyApplication.getInstance().getPrefManager().getWantTVLayout()) {
            isAndroidTV = true;
        } else {
            isAndroidTV = false;
        }
        return isAndroidTV;

    }

    public static boolean isHdmiSwitchSet() {

        // The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
        // An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
        File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
        UtilMethods.LogMethod("scanner123_", String.valueOf(switchFile));
        if (!switchFile.exists()) {
            switchFile = new File("/sys/class/switch/hdmi/state");
        }
        try {
            UtilMethods.LogMethod("scanner123_", "tryyy");
            Scanner switchFileScanner = new Scanner(switchFile);
            int switchValue = switchFileScanner.nextInt();
            switchFileScanner.close();
            return switchValue > 0;
        } catch (Exception e) {
            UtilMethods.LogMethod("scanner123_eeeee", String.valueOf(e));
            return false;
        }
    }

    public static boolean isTV() {
        String manufactural = Build.MANUFACTURER;
        UtilMethods.LogMethod("scanner123_manufactural", String.valueOf(manufactural));
        if (manufactural.equals("Xunison")) {
            return true;
        }
        return false;
    }

    public static Bitmap getBackgroundBitmap(Activity context) {
        View v = context.getWindow().getDecorView().getRootView();
        return Bitmap.createBitmap(v.getDrawingCache());
    }






    public static boolean appInstalledOrNot(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }






    public static String customFileName(String name) {
        name = name.replaceAll("[^a-zA-Z0-9&.]+", "_");
        String path = MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
        path = path + "/" + name + ".mp4";
        UtilMethods.LogMethod("recording_path", String.valueOf(path));
        UtilMethods.LogMethod("recording_exists", String.valueOf(new File(path).exists()));
        if (new File(path).exists()) {
            return name.trim() + "_" + System.currentTimeMillis() + ".mp4";
        } else {
            return name.trim() + ".mp4";
        }

    }

    public static void openDataTimePickerDialog(final Context mContext, final TextView outputTv) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        UtilMethods.LogMethod("date1234_", "onDateSet");
                        DecimalFormat mFormat = new DecimalFormat("00");
                        final String date_string = mFormat.format(Double.valueOf(dayOfMonth)) + "-"
                                + mFormat.format(Double.valueOf((monthOfYear + 1))) + "-" + year;
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        DecimalFormat mFormat = new DecimalFormat("00");
                                        String time_string = mFormat.format(Double.valueOf(hourOfDay)) +
                                                ":" + mFormat.format(Double.valueOf(minute));
                                        String dateTimeString = date_string + " " + time_string;
                                        outputTv.setText(dateTimeString);
                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public static void triggerStorageAccessFramework(Activity activity) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                activity.startActivityForResult(intent, 3);
            } catch (Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public static void openStorageDialog(final Context mContext,
                                         final TextView tv_set_path, final CustomInterface.
            onRecordingPathChanged listener) {
        final String previous_path = tv_set_path.getText().toString();
        CustomDialogs.showStorageDialog(mContext,
                mContext.getString(R.string.recording_select_recording_directory),
                new CustomInterface.onStorageDialogListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onOkClicked(String path) {
                        Log.e(TAG, "onOkClicked: paths:"+path );
                        tv_set_path.setText(mContext.
                                getString(R.string.recording_save_to) + path);
                        MyApplication.getInstance().getPrefManager().
                                setRecordingStoragePath(path);
                        String currentPath = MyApplication.getInstance().
                                getPrefManager().getRecordingStoragePath();
                        if (!currentPath.equals(previous_path) && listener != null) {
                            listener.onChange();
                        }
                    }
                });
    }






}
