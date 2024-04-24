package com.purple.iptv.player.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.documentfile.provider.DocumentFile;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.purple.iptv.player.utils.UtilMethods;
import com.smartrecording.recordingplugin.adapter.StorageListAdapter;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.database.DatabaseRoom;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;
import com.smartrecording.recordingplugin.utils.CommonMethods;
import com.xunison.recordingplugin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CustomDialogs {

    private static final String TAG = "CustomDialogs";
    //    private static SearchCastDevices mSearchCastDevices = null;
    private static String date_time_formate = "dd-MM-yyyy HH:mm";
    // its for recording list
    private static ArrayList<RecordingScheduleModel> mList;
    private static Context context = null;

//    public static void showStartRecordingDialog(final Context mContext,
//                                                final LiveChannelModel liveChannelModel, String name,
//                                                final String url) {
//        context = mContext;
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_start_recording);
//        final TextView header_simple_recording =
//                (TextView) dialog.findViewById(R.id.header_simple_recording);
//        final TextView header_schedule_recording =
//                (TextView) dialog.findViewById(R.id.header_schedule_recording);
//        final EditText et_name = (EditText) dialog.findViewById(R.id.et_name);
//        final EditText et_duration = (EditText) dialog.findViewById(R.id.et_duration);
//        final TextView et_start_time = (TextView) dialog.findViewById(R.id.et_start_time);
//        final TextView et_end_time = (TextView) dialog.findViewById(R.id.et_end_time);
//        final LinearLayout ll_schedule_recording_field =
//                (LinearLayout) dialog.findViewById(R.id.ll_schedule_recording_field);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        final TextView btn_change_directory = (TextView) dialog.findViewById(R.id.btn_change_directory);
//        final TextView text_recording_path = (TextView) dialog.findViewById(R.id.text_recording_path);
//        final boolean[] isScheduleRecordingSelected = {false};
//
//        header_simple_recording.requestFocus();
//        header_schedule_recording.setSelected(false);
//        header_simple_recording.setSelected(true);
//        ll_schedule_recording_field.setVisibility(View.GONE);
//        et_duration.setVisibility(View.VISIBLE);
//
//        header_simple_recording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                header_schedule_recording.setSelected(false);
//                view.setSelected(true);
//                ll_schedule_recording_field.setVisibility(View.GONE);
//                et_duration.setVisibility(View.VISIBLE);
//                isScheduleRecordingSelected[0] = false;
//            }
//        });
//
//        header_schedule_recording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                header_simple_recording.setSelected(false);
//                view.setSelected(true);
//                ll_schedule_recording_field.setVisibility(View.VISIBLE);
//                et_duration.setVisibility(View.GONE);
//                isScheduleRecordingSelected[0] = true;
//            }
//        });
//
//        header_simple_recording.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    header_schedule_recording.setSelected(false);
//                    view.setSelected(true);
//                    ll_schedule_recording_field.setVisibility(View.GONE);
//                    et_duration.setVisibility(View.VISIBLE);
//                    isScheduleRecordingSelected[0] = false;
//                }
//            }
//        });
//
//        header_schedule_recording.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    header_simple_recording.setSelected(false);
//                    view.setSelected(true);
//                    ll_schedule_recording_field.setVisibility(View.VISIBLE);
//                    et_duration.setVisibility(View.GONE);
//                    isScheduleRecordingSelected[0] = true;
//                }
//            }
//        });
//        UtilMethods.LogMethod("recording_dialog1212_name", String.valueOf(name));
//        name = CommonMethods.customFileName(name);
//        UtilMethods.LogMethod("recording_dialog1212_name1111", String.valueOf(name));
//        et_name.setText(name);
//        String storage_path =
//                MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
//        UtilMethods.LogMethod("recording_dialog1212_storage_path", String.valueOf(storage_path));
//        text_recording_path.setText(storage_path);
//
//        et_start_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommonMethods.openDataTimePickerDialog(mContext, et_start_time);
//            }
//        });
//
//        et_end_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommonMethods.openDataTimePickerDialog(mContext, et_end_time);
//            }
//        });
//
//        btn_change_directory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommonMethods.openStorageDialog(mContext, text_recording_path, null);
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UtilMethods.hideKeyboardFromFragment(mContext, btn_cancel);
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            public void onClick(View view) {
//
//
//                final String name = et_name.getText().toString().replaceAll("[^a-zA-Z0-9&.]+", "_");
//
//                if (name.length() == 0) {
//                    et_name.setError(mContext.getString(R.string.dialog_enter_name));
//                    if (BuildConfig.DEBUG)
//                        Log.e(TAG, "onClick: " + mContext.getString(R.string.dialog_enter_name));
//                    return;
//                }
//                if (isScheduleRecordingSelected[0]) {
//                    final String start_time = et_start_time.getText().toString();
//                    final String end_time = et_end_time.getText().toString();
//                    if (TextUtils.isEmpty(start_time)) {
//                        et_start_time.setError(mContext.getString(R.string.dialog_enter_start_time));
//                        if (BuildConfig.DEBUG)
//                            Log.e(TAG, "onClick: " + mContext.getString(R.string.dialog_enter_start_time));
//                        return;
//                    }
//                    if (TextUtils.isEmpty(end_time)) {
//                        et_end_time.setError(mContext.getString(R.string.dialog_enter_end_time));
//                        if (BuildConfig.DEBUG)
//                            Log.e(TAG, "onClick: " + mContext.getString(R.string.dialog_enter_end_time));
//                        return;
//                    }
//
//
//                    final long giventimestartMilli = UtilMethods.getDateInLocalMilliDynamicFormat(
//                            et_start_time.getText().toString(), "dd-MM-yyyy HH:mm");
//
//                    if (BuildConfig.DEBUG)
//                        Log.e(TAG, "onClick: 123 giventimestartMilli" + giventimestartMilli + " systemeinmili:" + System.currentTimeMillis());
//                    if (giventimestartMilli < System.currentTimeMillis()) {
//                        et_start_time.setError("Start time should be grater than current time.");
//                        if (BuildConfig.DEBUG)
//                            Log.e(TAG, "onClick: " + "Start time should be grater than current time.");
//                        return;
//                    }
//
//                    final long giventimeendMilli = UtilMethods.getDateInLocalMilliDynamicFormat(
//                            et_end_time.getText().toString(), "dd-MM-yyyy HH:mm");
//                    if (BuildConfig.DEBUG)
//                        Log.e(TAG, "onClick: 123 giventimeendMilli" + giventimeendMilli + " giventimestartMilli:" + giventimestartMilli);
//                    if (giventimeendMilli < giventimestartMilli) {
//                        et_end_time.setError("End time should be grater than start time.");
//                        if (BuildConfig.DEBUG)
//                            Log.e(TAG, "onClick: " + "End time should be grater than start time.");
//                        return;
//                    }
//                    // first get all sc task here
//                    new AsyncTask<String, String, String>() {
//                        @Override
//                        protected String doInBackground(String... strings) {
//                            mList = new ArrayList<>();
//
//                            if (BuildConfig.DEBUG)
//                                Log.e(TAG, "doInBackground: connectionInfoModel.getUid():" + liveChannelModel);
//                            List<RecordingScheduleModel> scheduleList =
//                                    DatabaseRoom.with(mContext).
//                                            getAllScheduleRecording(liveChannelModel.getConnection_id());
//                            for (int i = 0; i < scheduleList.size(); i++) {
//                                RecordingScheduleModel model = scheduleList.get(i);
//                                if (model.getStartTime() < System.currentTimeMillis()) {
//                                    deleteScheduleFromDatabase(model.getUid());
//                                } else {
//                                    mList.add(model);
//                                }
//
//                            }
//
//                            return null;
//                        }
//
//                        @Override
//                        protected void onPostExecute(String s) {
//                            super.onPostExecute(s);
//                            // here we got all sc task now check it one by one
//                            if (mList != null && mList.size() > 0) {
//                                if (BuildConfig.DEBUG) Log.e(TAG, "doInBackground: task  found");
//                                for (int i = 0; i < mList.size(); i++) {
//                                    long starttime = mList.get(i).getStartTime();
//                                    long endtime = mList.get(i).getEndTime();
//                                    // rc
//
//                                    if (BuildConfig.DEBUG)
//                                        Log.e(TAG, "onPostExecute: to do :" + checkingtime(starttime, endtime, giventimestartMilli, giventimeendMilli));
//                                    if (checkingtime(starttime, endtime, giventimestartMilli, giventimeendMilli)) {
//                                        RecordingScheduleModel scheduleModel = new RecordingScheduleModel();
//                                        scheduleModel.setConnection_id(liveChannelModel.getConnection_id());
//                                        scheduleModel.setShowName(name);
//                                        scheduleModel.setChannelName(liveChannelModel.getName());
//                                        scheduleModel.setStartTime(giventimestartMilli);
//                                        scheduleModel.setEndTime(giventimeendMilli);
//                                        scheduleModel.setUrl(url);
//                                        scheduleModel.setStatus(mContext.getString(R.string.recording_panding));
//                                        CommonMethods.setScheduleRecording(mContext, scheduleModel);
//                                        dialog.dismiss();
//                                    } else {
//                                        Toast.makeText(mContext, "Given Time has already Scheduled for another Recording, please change another time", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                    // check first given start time is grater with sc time
//                                    // 19:15 >18:18
////                                    if (starttime > giventimestartMilli) {
////                                        // input start time is small from sc task now check here for end time
////
////                                        //18:25 <19:40
////                                        if (giventimeendMilli < endtime) {
////                                            // input last time is small so add to sc task
////                                            RecordingScheduleModel scheduleModel = new RecordingScheduleModel();
////                                            scheduleModel.setConnection_id(liveChannelModel.getConnection_id());
////                                            scheduleModel.setShowName(name);
////                                            scheduleModel.setChannelName(liveChannelModel.getName());
////                                            scheduleModel.setStartTime(giventimestartMilli);
////                                            scheduleModel.setEndTime(giventimeendMilli);
////                                            scheduleModel.setUrl(url);
////                                            scheduleModel.setStatus(mContext.getString(R.string.recording_panding));
////                                            CommonMethods.setScheduleRecording(mContext, scheduleModel);
////                                            dialog.dismiss();
////                                           if (BuildConfig.DEBUG) Log.e(TAG, "onPostExecute: if 123");
//////                                            Toast.makeText(mContext, "Given Time has already Scheduled for another recording pl set another time 0 ", Toast.LENGTH_LONG).show();
//////                                            return;
////                                        } else {
////                                            // input last time is big from found time
////                                            Toast.makeText(mContext, "Given Time has already Scheduled for another recording pl set another time 0 ", Toast.LENGTH_LONG).show();
////                                           if (BuildConfig.DEBUG) Log.e(TAG, "onPostExecute: else 123");
////                                            return;
//////                                            Toast.makeText(mContext, "Time is available for sc ", Toast.LENGTH_LONG).show();
////
////                                        }
////
////
////                                    } else {
////                                        //find end time is less then given time
////                                        if (giventimeendMilli < endtime) {
////                                           if (BuildConfig.DEBUG) Log.e(TAG, "onPostExecute: if 456");
////                                            Toast.makeText(mContext, "Given Time has already Scheduled for another recording pl set another time  1", Toast.LENGTH_LONG).show();
////                                            return;
////
////                                        } else {
////                                            // check start time again here
////                                            //18:47 > 18:47
////                                            if (giventimestartMilli >= starttime) {
////                                               if (BuildConfig.DEBUG) Log.e(TAG, "onPostExecute: else if 456");
////                                                RecordingScheduleModel scheduleModel = new RecordingScheduleModel();
////                                                scheduleModel.setConnection_id(liveChannelModel.getConnection_id());
////                                                scheduleModel.setShowName(name);
////                                                scheduleModel.setChannelName(liveChannelModel.getName());
////                                                scheduleModel.setStartTime(giventimestartMilli);
////                                                scheduleModel.setEndTime(giventimeendMilli);
////                                                scheduleModel.setUrl(url);
////                                                scheduleModel.setStatus(mContext.getString(R.string.recording_panding));
////                                                CommonMethods.setScheduleRecording(mContext, scheduleModel);
////                                                dialog.dismiss();
////
////                                            } else {
////                                               if (BuildConfig.DEBUG) Log.e(TAG, "onPostExecute: else else 456");
////                                                Toast.makeText(mContext, "Given Time has already Scheduled for another recording pl set another time  1", Toast.LENGTH_LONG).show();
////                                                return;
////                                            }
////
////                                        }
////                                    }
//                                }
//
//                            } else {
//                                if (BuildConfig.DEBUG) Log.e(TAG, "doInBackground: task not found");
//                                RecordingScheduleModel scheduleModel = new RecordingScheduleModel();
//                                scheduleModel.setConnection_id(liveChannelModel.getConnection_id());
//                                scheduleModel.setShowName(name);
//                                scheduleModel.setChannelName(liveChannelModel.getName());
//                                scheduleModel.setStartTime(giventimestartMilli);
//                                scheduleModel.setEndTime(giventimeendMilli);
//                                scheduleModel.setUrl(url);
//                                scheduleModel.setStatus(mContext.getString(R.string.recording_panding));
//                                CommonMethods.setScheduleRecording(mContext, scheduleModel);
//                                dialog.dismiss();
//                            }
//
//                        }
//                    }.execute();
////
//
////                    RecordingScheduleModel scheduleModel = new RecordingScheduleModel();
////                    scheduleModel.setConnection_id(liveChannelModel.getConnection_id());
////                    scheduleModel.setShowName(name);
////                    scheduleModel.setChannelName(liveChannelModel.getName());
////                    scheduleModel.setStartTime(giventimestartMilli);
////                    scheduleModel.setEndTime(giventimeendMilli);
////                    scheduleModel.setUrl(url);
////                    scheduleModel.setStatus(mContext.getString(R.string.recording_panding));
////                    CommonMethods.setScheduleRecording(mContext, scheduleModel);
////                    dialog.dismiss();
//
//                } else {
//                    if (RecordingService.isrunningdownloadtask) {
//                        Toast.makeText(mContext, "You have already Recording started , first complete/stop the recording to do next Recording ", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    String duration = et_duration.getText().toString();
//                    int duration1 = 0;
//                    if (duration.equals("")) {
//                        duration1 = 0;
//                    } else {
//                        duration1 = Integer.parseInt(duration);
//                    }
//                    if (duration1 == 0) {
//                        et_duration.setError(mContext.getString(R.string.dialog_duration_error));
//                        return;
//                    }
//                    Toast.makeText(mContext, "Recording has Started.", Toast.LENGTH_LONG).show();
//                    sendplugintorecordnow(mContext, name, url, duration1);
////                    Intent i = new Intent(mContext, RecordingService.class);
////                    i.putExtra("recoding_file_name", name);
////                    i.putExtra("downloadUrl", url);
////                    i.putExtra("minute", duration1);
//
//                    // mContext.startService(i);
//                    dialog.dismiss();
//                }
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//
//
//    }

//    public static void sendplugintorecordnow(Context mContext, String name, String url, int duration1) {
//        Log.e(TAG, "abc called : sendplugintorecordnow: "  );
//        if (appInstalledOrNot(mContext, PKGFORRECORDING)) {
//            List arrayList = new ArrayList();
//            PackageManager packageManager = mContext.getPackageManager();
//            Intent intent = new Intent("android.intent.action.SEND");
//            intent.setType("text/plain");
//            List queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
//            for (int i = 0; i < queryIntentActivities.size(); i++) {
//                ResolveInfo resolveInfo = (ResolveInfo) queryIntentActivities.get(i);
//                String str3 = resolveInfo.activityInfo.packageName;
//                Log.e("Package Name", str3);
//                if (str3.contains(PKGFORRECORDING)) {
//                    Intent intent2 = new Intent();
//                    intent2.setComponent(new ComponentName(str3, resolveInfo.activityInfo.name));
//                    intent2.setAction("android.intent.action.SEND");
//                    intent2.setType("*/*");
//                    intent2.putExtra("recoding_file_name", name);
//                    intent2.putExtra("downloadUrl", url);
//                    intent2.putExtra("minute", duration1);
//                    intent2.putExtra("pkgname", mContext.getPackageName());
//                    intent2.putExtra("getdirectrecord", "true");
//                    intent2.putExtra("path", MyApplication.getInstance().getPrefManager().
//                            getRecordingStoragePath());
//                    intent2.setPackage(str3);
//                    arrayList.add(intent2);
//                }
//                if (arrayList.isEmpty()) {
//                    System.out.println("Do not Have Intent");
//                } else {
//                    Intent createChooser = Intent.createChooser((Intent) arrayList.remove(0), "Choose app to share");
//                    createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[0]));
//                    mContext.startActivity(createChooser);
//                }
//            }
//
//        } else {
//            Toast.makeText(mContext, "Recording Plugin Not Found", Toast.LENGTH_LONG).show();
//        }
//    }

//    private static boolean checkingtime(long start_time, long end_time, long giventimestartMilli, long giventimeendMilli) {
//        boolean isstarttimeexist = false;
//        boolean isendtimeexist = false;
//        try {
//            if (BuildConfig.DEBUG)
//                Log.e(TAG, "checkingtime: start_time" + start_time + " after " + getDate(start_time));
//            if (BuildConfig.DEBUG)
//                Log.e(TAG, "checkingtime: end_time" + end_time + " after " + getDate(end_time));
//            if (BuildConfig.DEBUG)
//                Log.e(TAG, "checkingtime: giventimestartMilli" + giventimestartMilli + " after " + getDate(giventimestartMilli));
//            if (BuildConfig.DEBUG)
//                Log.e(TAG, "checkingtime: giventimeendMilli" + giventimeendMilli + " after " + getDate(giventimeendMilli));
////
//            //endtime
//            Date time1 = new SimpleDateFormat(date_time_formate).parse(getDate(end_time));
//            Calendar endtimecalendar = Calendar.getInstance();
//            endtimecalendar.setTime(time1);
//            endtimecalendar.add(Calendar.DATE, 1);
//
//
//            //starttime
//            Date time2 = new SimpleDateFormat(date_time_formate).parse(getDate(start_time));
//            Calendar starttime_calendar = Calendar.getInstance();
//            starttime_calendar.setTime(time2);
//            starttime_calendar.add(Calendar.DATE, 1);
//
//            //given start
//            Date d = new SimpleDateFormat(date_time_formate).parse(getDate(giventimestartMilli));
//            Calendar givenstart_calendar = Calendar.getInstance();
//            givenstart_calendar.setTime(d);
//            givenstart_calendar.add(Calendar.DATE, 1);
//
//            // given end time
//            Date d11 = new SimpleDateFormat(date_time_formate).parse(getDate(giventimeendMilli));
//            Calendar givenend_calendar = Calendar.getInstance();
//            givenend_calendar.setTime(d11);
//            givenend_calendar.add(Calendar.DATE, 1);
//
//
//            Date x = givenstart_calendar.getTime();
//            //checking for start time
//            if (x.after(starttime_calendar.getTime()) && x.before(endtimecalendar.getTime())) {
//                isstarttimeexist = true;
//                if (BuildConfig.DEBUG) Log.e(TAG, "checkingtime: given start time btwn times");
//            } else {
//                isstarttimeexist = false;
//                if (BuildConfig.DEBUG) Log.e(TAG, "checkingtime: given start time not btwn times");
//            }
//            Date x1 = givenend_calendar.getTime();
//            // checking for end time
//            if (x1.after(starttime_calendar.getTime()) && x1.before(endtimecalendar.getTime())) {
//                isendtimeexist = true;
//                if (BuildConfig.DEBUG) Log.e(TAG, "checkingtime: given end time btwn times");
//            } else {
//                if (BuildConfig.DEBUG) Log.e(TAG, "checkingtime: given end time not btwn times");
//                isendtimeexist = false;
//            }
//            // checccking between
//
//
//            if (BuildConfig.DEBUG)
//                Log.e(TAG, "checkingtime: return :" + (!isendtimeexist && !isstarttimeexist));
//            return !isendtimeexist && !isstarttimeexist;
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(date_time_formate);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    @SuppressLint("StaticFieldLeak")
    private static void deleteScheduleFromDatabase(final long uid) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseRoom.with(context).deleteScheduleRecording(uid);
                return null;
            }
        }.execute();
    }


    public static void showSDPermissionDialog(final Context mContext, String path) {
        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
        dialog.setContentView(R.layout.dialog_sd_permission);
        final LinearLayout ll_dialog_sdpermissionbg = dialog.findViewById(R.id.ll_dialog_sdpermissionbg);
        final TextView text_instruction = (TextView) dialog.findViewById(R.id.text_instruction);
        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        ColorModel colorModel = new Gson().fromJson(MyApplication.getInstance().getPrefManager().getColormodel(), ColorModel.class);
        ll_dialog_sdpermissionbg.setBackground(get_bg_dialog(colorModel.getColor_dialog_bg()));
        btn_cancel.setBackground(getSelectorDrawablewithbottomleftradious(mContext.getResources().getColor(R.color.black), colorModel.getSelected_color()));
        btn_ok.setBackground(getSelectorDrawablewithbottomrightradious(mContext.getResources().getColor(R.color.black), colorModel.getSelected_color()));
        text_instruction.setText(String.format(mContext.getString(R.string.dialog_ins_write_access_required_sd_card), path));


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CommonMethods.triggerStorageAccessFramework((Activity) mContext);
            }
        });


        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    public static StateListDrawable getSelectorDrawableRound(int normalcolor, int focusecolor) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{-android.R.attr.state_focused}, createNormalDrawablewithcircle(normalcolor));
        out.addState(new int[]{android.R.attr.state_focused}, createNormalDrawablewithcircle(focusecolor));

        return out;
    }

    public static GradientDrawable get_bg_dialog(int normalcolor) {
        GradientDrawable out = new GradientDrawable();
        out.setCornerRadius(5);
        out.setShape(GradientDrawable.RECTANGLE);

        out.setColor(normalcolor);
        return out;

    }

    public static StateListDrawable getSelectorDrawableforcover(int normalcolor, int focusecolor, int strokecolor1) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{-android.R.attr.state_focused}, createNormalDrawablewithstroke(normalcolor, strokecolor1));
        out.addState(new int[]{android.R.attr.state_focused}, createNormalDrawablewithstroke(focusecolor, strokecolor1));

        return out;
    }

    public static StateListDrawable getSelectorDrawable(int normalcolor, int focusecolor) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{-android.R.attr.state_focused}, createNormalDrawable(normalcolor));
        out.addState(new int[]{android.R.attr.state_focused}, createNormalDrawable(focusecolor));

        return out;
    }

    public static StateListDrawable getSelectorDrawablewithbottomleftradious(int normalcolor, int focusecolor) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{-android.R.attr.state_focused}, createNormalDrawablewithbottomleftradious(normalcolor));
        out.addState(new int[]{android.R.attr.state_focused}, createNormalDrawablewithbottomleftradious(focusecolor));

        return out;
    }

    public static StateListDrawable getSelectorDrawablewithbottomrightradious(int normalcolor, int focusecolor) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{-android.R.attr.state_focused}, createNormalDrawablewithbottomrightradious(normalcolor));
        out.addState(new int[]{android.R.attr.state_focused}, createNormalDrawablewithbottomrightradious(focusecolor));

        return out;
    }

    public static GradientDrawable createNormalDrawable(int color) {
        GradientDrawable out = new GradientDrawable();
        out.setCornerRadius(5);

        out.setColor(color);
        return out;
    }

    public static GradientDrawable createNormalDrawablewithbottomleftradious(int color) {
        GradientDrawable out = new GradientDrawable();
        // out.setCornerRadius(5);
        float mRadius = 5f;
        out.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, mRadius, mRadius});

        out.setColor(color);
        return out;
    }

    public static GradientDrawable createNormalDrawablewithbottomrightradious(int color) {
        GradientDrawable out = new GradientDrawable();
        //out.setCornerRadius(5);
        float mRadius = 5f;
        out.setCornerRadii(new float[]{0, 0, 0, 0, mRadius, mRadius, 0, 0});

        out.setColor(color);
        return out;
    }

    public static GradientDrawable createNormalDrawablewithstroke(int color, int strokecolor) {
        GradientDrawable out = new GradientDrawable();
        out.setCornerRadius(5);
        out.setColor(color);
        out.setStroke(0, strokecolor);
        return out;
    }

    public static GradientDrawable createNormalDrawablewithcircle(int color) {
        GradientDrawable out = new GradientDrawable();
        //   out.setCornerRadius(5);
        out.setColor(color);
        out.setShape(GradientDrawable.OVAL);
        //  out.setStroke(0, strokecolor);
        return out;
    }


    private static StorageListAdapter adapter = null;

    public static void showStorageDialog(final Context mContext, String title,
                                         final CustomInterface.onStorageDialogListener listener) {
        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
        dialog.setContentView(R.layout.dialog_storage);
        VerticalGridView recycler_storage = (VerticalGridView) dialog.findViewById(R.id.recycler_storage);
        TextView text_title = (TextView) dialog.findViewById(R.id.text_title);
        LinearLayout ll_dialog_storagebg = dialog.findViewById(R.id.ll_dialog_storagebg);
        final TextView text_current_folder = (TextView) dialog.findViewById(R.id.text_current_folder);
        final View view_dialogstorage =   dialog.findViewById(R.id.view_dialogstorage);
        //ImageView btn_new_folder = (ImageView) dialog.findViewById(R.id.btn_new_folder);
        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        ColorModel colorModel = new Gson().fromJson(MyApplication.getInstance().getPrefManager().getColormodel(), ColorModel.class);
        ll_dialog_storagebg.setBackground(get_bg_dialog(colorModel.getColor_dialog_bg()));
        btn_cancel.setBackground(getSelectorDrawablewithbottomleftradious(mContext.getResources().getColor(R.color.black), colorModel.getSelected_color()));
        btn_ok.setBackground(getSelectorDrawablewithbottomrightradious(mContext.getResources().getColor(R.color.black), colorModel.getSelected_color()));
        view_dialogstorage.setBackgroundColor(colorModel.getSelected_color());
        text_title.setText(title);
        text_current_folder.setText("/storage");
        final StorageListClass storageListClass = new StorageListClass(mContext);
        String path = "//storage";
        final ArrayList<StorageListClass.StorageFileModel> mList =
                storageListClass.getRootStorageList();

        btn_ok.setVisibility(View.GONE);

        adapter = new StorageListAdapter(mContext, mList,
                new StorageListAdapter.adapterInterface() {
                    @Override
                    public void onClick(StorageListAdapter.StorageViewHolder holder, int position) {
                        StorageListClass.StorageFileModel model = mList.get(position);
                        if (model.getFile_path().equals(Environment.
                                getExternalStorageDirectory().getAbsolutePath())) {
                            Log.e(TAG, "onClick: if");
                            String path = Environment.
                                    getExternalStorageDirectory().getAbsolutePath();
                            text_current_folder.setText(path);
                            mList.clear();
                            ArrayList<StorageListClass.StorageFileModel> mListNew =
                                    storageListClass.findFileList(path, false);
                            mList.addAll(mListNew);
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                            btn_ok.setVisibility(View.VISIBLE);
                        } else if (model.getFile_path().equals("root")) {
                            Log.e(TAG, "onClick: else if");
                            btn_ok.setVisibility(View.GONE);
                            text_current_folder.setText("/storage");
                            mList.clear();
                            ArrayList<StorageListClass.StorageFileModel> mListNew =
                                    storageListClass.getRootStorageList();
                            mList.addAll(mListNew);
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "onClick: else ");
                            String external_uri = MyApplication.getInstance().getPrefManager().
                                    getExternalStorageUri();
                            UtilMethods.LogMethod("per123_external_uri", String.valueOf(external_uri));
                            boolean ask_permission = true;
                            if (external_uri != null) {
                                DocumentFile document = DocumentFile.fromTreeUri(mContext,
                                        Uri.parse(external_uri));
                                UtilMethods.LogMethod("per123_document", String.valueOf(document));
                                if (document != null) {
                                    ask_permission = false;
                                }
                            }
                            UtilMethods.LogMethod("per123_", String.valueOf(ask_permission));
                            UtilMethods.LogMethod("per123_model.getFile_path()",
                                    String.valueOf(model.getFile_path()));
                            if (!model.getFile_path().contains(Environment.
                                    getExternalStorageDirectory().getAbsolutePath()) &&
                                    ask_permission) {
                                UtilMethods.LogMethod("per123_", String.valueOf("ifff"));
                                showSDPermissionDialog(mContext, model.getFile_path());
                            } else {
                                btn_ok.setVisibility(View.VISIBLE);
                                String path = model.getFile_path();
                                text_current_folder.setText(path);
                                mList.clear();
                                ArrayList<StorageListClass.StorageFileModel> mListNew =
                                        storageListClass.findFileList(path, false);
                                mList.addAll(mListNew);
                                if (adapter != null)
                                    adapter.notifyDataSetChanged();
                            }

                        }
                    }

                });
//        if (CommonMethods.checkIsTelevision(mContext)) {
//            recycler_storage.setNumColumns(1);
//        } else {
        recycler_storage.setLayoutManager(new LinearLayoutManager(mContext));
        //       }

        recycler_storage.setAdapter(adapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onOkClicked(text_current_folder.getText().toString());
                }
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    public static void showDeleteAlertDialog(final Context mContext, String name,
                                             final CustomInterface.deleteAlertInterface listener) {
        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
        dialog.setContentView(R.layout.dialog_delete_alert);
        final TextView text_instruction = (TextView) dialog.findViewById(R.id.text_instruction);
        final LinearLayout dialogdeletebg =   dialog.findViewById(R.id.dialogdeletebg);
        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        ColorModel colorModel = new Gson().fromJson(MyApplication.getInstance().getPrefManager().getColormodel(), ColorModel.class);
        dialogdeletebg.setBackground(get_bg_dialog(colorModel.getColor_dialog_bg()));
        btn_cancel.setBackground(getSelectorDrawablewithbottomleftradious(mContext.getResources().getColor(R.color.black), colorModel.getSelected_color()));
        btn_ok.setBackground(getSelectorDrawablewithbottomrightradious(mContext.getResources().getColor(R.color.black), colorModel.getSelected_color()));

        text_instruction.setText(String.
                format(mContext.getString(R.string.are_you_sure_want_to_delete_s), name));


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onYes(dialog);
                dialog.dismiss();

            }
        });


        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

//    public static void showMultiScreenInfoDialog(Context mContext, String instruction,
//                                                 final CustomInterface.MultiScreenInfoDialog listener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_xstream_multi);
//        dialog.setCancelable(false);
//        final TextView text_instruction = (TextView) dialog.findViewById(R.id.text_instruction);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        btn_ok.requestFocus();
//        text_instruction.setText(instruction);
//
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null) {
//                    listener.onOkClick();
//                }
//                dialog.dismiss();
//
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }

//    public static void showMultiScreenLayoutDialog(Context mContext,
//                                                   final CustomInterface.MultiScreenLayoutDialog listener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_xstream_multi_layout);
//        dialog.setCancelable(false);
//        final ImageView btn_type_1 = (ImageView) dialog.findViewById(R.id.type_1);
//        final ImageView btn_type_2 = (ImageView) dialog.findViewById(R.id.type_2);
//        final ImageView btn_type_3 = (ImageView) dialog.findViewById(R.id.type_3);
//        final ImageView btn_type_4 = (ImageView) dialog.findViewById(R.id.type_4);
//        final ImageView btn_type_5 = (ImageView) dialog.findViewById(R.id.type_5);
//        final ImageView btn_type_6 = (ImageView) dialog.findViewById(R.id.type_6);
//
//
//        btn_type_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onClick(FlowLayout.SCREEN_OF_2);
//                dialog.dismiss();
//            }
//        });
//
//        btn_type_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onClick(FlowLayout.SCREEN_OF_4);
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_type_3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onClick(FlowLayout.SCREEN_OF_4_1);
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_type_4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onClick(FlowLayout.SCREEN_OF_3_COLUMN);
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_type_5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onClick(FlowLayout.SCREEN_OF_3_HORIZONTAL);
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_type_6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onClick(FlowLayout.SCREEN_OF_6);
//                dialog.dismiss();
//
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }

//    public static void showSortingDialog(Context mContext,
//                                         final CustomInterface.SortByListener sortByListener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_sort_by);
//        final LinearLayout ll_default = (LinearLayout) dialog.findViewById(R.id.ll_default);
//        final LinearLayout ll_az = (LinearLayout) dialog.findViewById(R.id.ll_az);
//        final LinearLayout ll_za = (LinearLayout) dialog.findViewById(R.id.ll_za);
//        final LinearLayout ll_latest = (LinearLayout) dialog.findViewById(R.id.ll_latest);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        final TextView txtlatestrecentlyadded = (TextView) dialog.findViewById(R.id.txtlatestrecentlyadded);
//        if (mContext instanceof MovieSeriesActivity) {
//            ll_latest.setVisibility(View.VISIBLE);
//            txtlatestrecentlyadded.setText("Recently Added");
//
//        }
//
//        UtilMethods.LogMethod("currently_selecte", String.valueOf(UtilConstant.currently_selected_sort));
//        if (UtilConstant.currently_selected_sort == Config.SORT_BY_A_Z) {
//            ll_az.setSelected(true);
//        } else if (UtilConstant.currently_selected_sort == Config.SORT_BY_Z_A) {
//            ll_za.setSelected(true);
//        } else if (UtilConstant.currently_selected_sort == Config.SORT_BY_DEFAULT) {
//            ll_default.setSelected(true);
//        } else if (UtilConstant.currently_selected_sort == Config.SORT_BY_LATEST) {
//            ll_latest.setSelected(true);
//        }
//        final int[] selected_sort_type = new int[1];
//        ll_default.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_default.setSelected(true);
//                ll_az.setSelected(false);
//                ll_za.setSelected(false);
//                ll_latest.setSelected(false);
//                selected_sort_type[0] = Config.SORT_BY_DEFAULT;
//                UtilConstant.currently_selected_sort = Config.SORT_BY_DEFAULT;
//            }
//        });
//
//        ll_az.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_default.setSelected(false);
//                ll_az.setSelected(true);
//                ll_za.setSelected(false);
//                ll_latest.setSelected(false);
//                selected_sort_type[0] = Config.SORT_BY_A_Z;
//                UtilConstant.currently_selected_sort = Config.SORT_BY_A_Z;
//            }
//        });
//
//        ll_za.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_default.setSelected(false);
//                ll_az.setSelected(false);
//                ll_za.setSelected(true);
//                ll_latest.setSelected(false);
//                selected_sort_type[0] = Config.SORT_BY_Z_A;
//                UtilConstant.currently_selected_sort = Config.SORT_BY_Z_A;
//
//            }
//        });
//
//        ll_latest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_default.setSelected(false);
//                ll_az.setSelected(false);
//                ll_za.setSelected(false);
//                ll_latest.setSelected(true);
//                selected_sort_type[0] = Config.SORT_BY_LATEST;
//                UtilConstant.currently_selected_sort = Config.SORT_BY_LATEST;
//
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (sortByListener != null) {
//                    sortByListener.onSortData(dialog, selected_sort_type[0]);
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void showParentalDialog(final Context mContext,
//                                          final CustomInterface.onParentalListener listener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_parental_control);
//        final EditText et_password = (EditText) dialog.findViewById(R.id.et_password);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        et_password.requestFocus();
//
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UtilMethods.hideKeyboardFromFragment(mContext, btn_cancel);
//                if (listener != null) {
//                    listener.onCancel(dialog);
//                }
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String password = et_password.getText().toString();
//                if (!TextUtils.isEmpty(password)) {
//                    String storePassword =
//                            MyApplication.getInstance().getPrefManager().getParentalControlPassword();
//                    if (password.equals(storePassword)) {
//                        if (listener != null) {
//                            listener.onSubmit(dialog);
//                        }
//                        dialog.dismiss();
//                    } else {
//                        et_password.setError(mContext.getString(R.string.str_error_incorrect_parental_password));
//                    }
//                } else {
//                    et_password.setError(mContext.getString(R.string.str_enter_parental_password));
//                }
//
//            }
//        });
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void showRestartDialog(final Context mContext) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_restart);
//        final TextView text_exit = (TextView) dialog.findViewById(R.id.text_exit);
//        final TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_yes);
//        final TextView btn_no = (TextView) dialog.findViewById(R.id.btn_no);
//
//        // text_instruction.setText(UtilConstant.startupMsg);
//
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });
//
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommonMethods.onRestart(mContext);
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//
//        dialog.show();
//    }
//
//    public static void showCastDialog(Context mContext,
//                                      final CustomInterface.dialogCastDevicesListener listener) {
//        if (UtilMethods.isWifiConnected(mContext)) {
//            final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.dialog_cast);
//            final RecyclerView stop_casting_recycler =
//                    (RecyclerView) dialog.findViewById(R.id.stop_casting_recycler);
//            TextView casting_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//            TextView casting_stop = (TextView) dialog.findViewById(R.id.btn_stop_casting);
//            final TextView tv_searching_devices = (TextView) dialog.findViewById(R.id.tv_searching_devices);
//            final TextView tv_no_device_found = (TextView) dialog.findViewById(R.id.tv_no_device_found);
//
//
//            final ArrayList<ConnectableDevice> mList = new ArrayList<>();
//            if (UtilConstant.connected_device != null) {
//                casting_stop.setVisibility(View.VISIBLE);
//                mList.add(UtilConstant.connected_device);
//            } else {
//                casting_stop.setVisibility(View.GONE);
//                casting_cancel.setVisibility(View.VISIBLE);
//            }
//
//            if (mList.size() > 0) {
//                tv_searching_devices.setVisibility(View.GONE);
//                stop_casting_recycler.setVisibility(View.VISIBLE);
//                tv_no_device_found.setVisibility(View.GONE);
//            } else {
//                tv_searching_devices.setVisibility(View.VISIBLE);
//                stop_casting_recycler.setVisibility(View.GONE);
//                tv_no_device_found.setVisibility(View.GONE);
//            }
//            stop_casting_recycler.setLayoutManager(new LinearLayoutManager(mContext));
//            final CastingDeviceAdapter adapter = new CastingDeviceAdapter(mContext, mList,
//                    new CastingDeviceAdapter.DeviceListInterface() {
//                        @Override
//                        public void onCastDeviceClick(CastingDeviceAdapter.
//                                                              DialogDeviceViewHolder holder,
//                                                      int position) {
//                            ConnectableDevice device = mList.get(position);
//                            if (mSearchCastDevices != null)
//                                mSearchCastDevices.connectDevice(device);
//                        }
//                    });
//
//            stop_casting_recycler.setAdapter(adapter);
//
//            mSearchCastDevices = new SearchCastDevices(mContext,
//                    new CustomInterface.searchCastDevicesListener() {
//                        @Override
//                        public void onDeviceAdded(DiscoveryManager manager,
//                                                  ConnectableDevice device) {
//                            boolean hasAlready = false;
//                            for (int i = 0; i < mList.size(); i++) {
//                                ConnectableDevice con_device = mList.get(i);
//                                if (con_device.getFriendlyName().
//                                        equalsIgnoreCase(device.getFriendlyName())) {
//                                    hasAlready = true;
//                                }
//                            }
//                            if (!hasAlready) {
//                                mList.add(device);
//                                adapter.notifyDataSetChanged();
//                            }
//                            // mList.add(device);
//                            if (mList.size() > 0) {
//                                tv_searching_devices.setVisibility(View.GONE);
//                                stop_casting_recycler.setVisibility(View.VISIBLE);
//                                tv_no_device_found.setVisibility(View.GONE);
//                            }
//                        }
//
//                        @Override
//                        public void onDeviceRemoved(DiscoveryManager manager,
//                                                    ConnectableDevice device) {
//                            mList.remove(device);
//                            adapter.notifyDataSetChanged();
//                            if (mList.size() > 0) {
//                                tv_searching_devices.setVisibility(View.VISIBLE);
//                                stop_casting_recycler.setVisibility(View.GONE);
//                                tv_no_device_found.setVisibility(View.GONE);
//                            }
//                        }
//
//                        @Override
//                        public void onDiscoveryFailed(DiscoveryManager manager,
//                                                      ServiceCommandError error) {
//                            if (mList.size() > 0) {
//                                tv_searching_devices.setVisibility(View.GONE);
//                                stop_casting_recycler.setVisibility(View.GONE);
//                                tv_no_device_found.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                        @Override
//                        public void onConnectionFailed(ConnectableDevice device,
//                                                       ServiceCommandError error) {
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onDeviceReady(ConnectableDevice device) {
//                            dialog.dismiss();
//                            if (listener != null) {
//                                listener.onDeviceReady(dialog, device);
//                            }
//                        }
//
//                        @Override
//                        public void onDeviceDisconnected(ConnectableDevice device) {
//                            dialog.dismiss();
//                        }
//                    });
//
//            mSearchCastDevices.onStartSearch();
//
//
//            casting_cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mSearchCastDevices != null)
//                        mSearchCastDevices.onStopSearch();
//                    if (listener != null) {
//                        listener.onCancel(dialog);
//                    }
//                    dialog.dismiss();
//                }
//            });
//
//            casting_stop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    UtilConstant.connected_device = null;
//                    if (mSearchCastDevices != null)
//                        mSearchCastDevices.onStopSearch();
//                    if (listener != null) {
//                        listener.onStopCasting(dialog);
//                    }
//                    dialog.dismiss();
//                }
//            });
//            Window window = dialog.getWindow();
//            assert window != null;
//            window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                    FrameLayout.LayoutParams.MATCH_PARENT);
//            dialog.show();
//        } else {
//            Toast.makeText(mContext, mContext.getString(R.string.str_connect_to_wifi),
//                    Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//    public static void showVersionUpdateDialog
//            (Context mContext, final CustomInterface.versionUpdateInterface versionUpdatelistener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_version);
//        final TextView text_msg = (TextView) dialog.findViewById(R.id.text_msg);
//        final TextView btn_remind_me = (TextView) dialog.findViewById(R.id.btn_remind_me);
//        final TextView btn_update = (TextView) dialog.findViewById(R.id.btn_update);
//        RemoteConfigModel remoteConfigModel = MyApplication.getInstance().
//                getPrefManager().getRemoteConfig();
//        if (remoteConfigModel != null) {
//            text_msg.setText(remoteConfigModel.getVersion_message());
//        }
//
//        if (remoteConfigModel != null && remoteConfigModel.isVersion_force_update()) {
//            btn_remind_me.setVisibility(View.GONE);
//        } else {
//            btn_remind_me.setVisibility(View.VISIBLE);
//        }
//
//
//        btn_remind_me.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (versionUpdatelistener != null) {
//                    versionUpdatelistener.onRemindMe(dialog);
//                }
//                dialog.dismiss();
//            }
//        });
//
//        btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (versionUpdatelistener != null) {
//                    versionUpdatelistener.onUpdate(dialog);
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void showUpdateByDialog(final Context mContext,
//                                          final CustomInterface.
//                                                  updateByListener updateByListener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_update_by);
//        final LinearLayout ll_by_playstore = (LinearLayout) dialog.findViewById(R.id.ll_by_playstore);
//        final LinearLayout ll_by_apk = (LinearLayout) dialog.findViewById(R.id.ll_by_apk);
//        final TextView btn_update = (TextView) dialog.findViewById(R.id.btn_update);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        final int[] selected_update_type = {-1};
//        ll_by_playstore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_by_playstore.setSelected(true);
//                ll_by_apk.setSelected(false);
//                selected_update_type[0] = Config.UPDATE_BY_PLAYSTORE;
//            }
//        });
//
//        ll_by_apk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_by_playstore.setSelected(false);
//                ll_by_apk.setSelected(true);
//                selected_update_type[0] = Config.UPDATE_BY_APK;
//            }
//        });
//
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (updateByListener != null) {
//                    updateByListener.onCancelClick(dialog);
//                }
//            }
//        });
//
//        btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (selected_update_type[0] != -1) {
//                    dialog.dismiss();
//                    if (updateByListener != null) {
//                        updateByListener.UpdateBy(dialog, selected_update_type[0]);
//                    }
//
//                } else {
//                    Toast.makeText(mContext, mContext.getString(R.string.str_select_any_method),
//                            Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static Dialog percentageProgressDialog
//            (Context mContext,
//             final CustomInterface.percentageProgressBarListener percentageProgressBarListener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_percentage_progress);
//        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.percentage_progressBar);
//        final TextView text_percentage = (TextView) dialog.findViewById(R.id.text_progress_percentage);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//
//        if (percentageProgressBarListener != null) {
//            percentageProgressBarListener.onDialogShown(dialog, progressBar, text_percentage);
//        }
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (percentageProgressBarListener != null) {
//                    percentageProgressBarListener.onCancel(dialog);
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//        return dialog;
//    }
//
//    public static void onDeclinePermissionDialog
//            (Context mContext,
//             final CustomInterface.permissionDeclineListener permissionDeclineListener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_decline_permission);
//        final TextView text_msg = (TextView) dialog.findViewById(R.id.text_msg);
//        final TextView btn_no = (TextView) dialog.findViewById(R.id.btn_no);
//        final TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_yes);
//
//        //text_msg.setText(UtilConstant.version_message);
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (permissionDeclineListener != null) {
//                    permissionDeclineListener.onNo(dialog);
//                }
//                dialog.dismiss();
//            }
//        });
//
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (permissionDeclineListener != null) {
//                    permissionDeclineListener.onYes(dialog);
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void onMultiScreenExitDialog
//            (Context mContext,
//             final CustomInterface.MultiScreenExitDialog exitListener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_exit_multiscreen);
//        final TextView btn_exit = (TextView) dialog.findViewById(R.id.btn_exit);
//        final TextView btn_change_layout = (TextView) dialog.findViewById(R.id.btn_change_layout);
//
//        //text_msg.setText(UtilConstant.version_message);
//
//        btn_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (exitListener != null) {
//                    exitListener.onExit();
//                }
//                dialog.dismiss();
//            }
//        });
//
//        btn_change_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (exitListener != null) {
//                    exitListener.onChangeLayout();
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void watchRewardedVideoDialog(Context mContext, String reward_header,
//                                                String reward_text,
//                                                final RewardedVideoAd rewardedVideoAd) {
//        if (mContext != null) {
//            final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//            dialog.setContentView(R.layout.dialog_watch_rewarded_video);
//            final TextView text_reward = (TextView) dialog.findViewById(R.id.text_reward);
//            final TextView text_header = (TextView) dialog.findViewById(R.id.text_header);
//            final TextView btn_watch = (TextView) dialog.findViewById(R.id.btn_watch);
//            final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//
//            text_reward.setText(reward_text);
//            text_header.setText(reward_header);
//            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//
//                }
//            });
//
//            btn_watch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    CommonMethods.showRewardedAd(rewardedVideoAd);
//                }
//            });
//            Window window = dialog.getWindow();
//            assert window != null;
//            window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                    FrameLayout.LayoutParams.MATCH_PARENT);
//            dialog.show();
//        }
//    }
//
//    public static void showExitDialog(final Context mContext) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_exit);
//        final TextView text_exit = (TextView) dialog.findViewById(R.id.text_exit);
//        final TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_yes);
//        final TextView btn_no = (TextView) dialog.findViewById(R.id.btn_no);
//
//        final String code = MyApplication.getInstance().getPrefManager().getUniversalCode();
//        if (code != null) {
//            btn_no.setText(mContext.getString(R.string.str_change_provider));
//        }
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (code != null) {
//                    MyApplication.getInstance().getPrefManager().setUniversalCode(null);
//                    Intent i = new Intent(mContext, SplashActivity.class);
//                    mContext.startActivity(i);
//
//                }
//            }
//        });
//
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MyApplication.getInstance().getPrefManager().setShowInstructionDialog(true);
//                dialog.dismiss();
//                ((Activity) mContext).finishAffinity();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//
//        dialog.show();
//    }
//
//    public static void showM3uEpgDialog(final Context mContext,
//                                        final CustomInterface.epgDialogInterface epgDialoglistener) {
//        UtilMethods.LogMethod("m3uepg123_", "showM3uEpgDialog");
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_epg);
//        final EditText et_epg_url = (EditText) dialog.findViewById(R.id.et_epg_url);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//
//
//        if (BuildConfig.DEBUG) {
//            //https://env-6294890.mj.milesweb.cloud/jio/epg.php"
//            // https://streamstrom.b-cdn.net/epg.php?u=darshan&p=darshan
//            et_epg_url.setText("https://streamstrom.b-cdn.net/epg.php?u=darshan&p=darshan");
//        }
//        UtilMethods.showKeyboard(mContext, et_epg_url);
//        UtilMethods.LogMethod("m3uepg123_", et_epg_url.getText().toString());
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UtilMethods.hideKeyboardFromFragment(mContext, et_epg_url);
//                dialog.dismiss();
//                if (epgDialoglistener != null) {
//                    epgDialoglistener.onCancel(dialog);
//                }
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String epg_url = et_epg_url.getText().toString();
//                if (epg_url.length() > 0) {
//                    UtilMethods.hideKeyboardFromFragment(mContext, et_epg_url);
//                    if (epgDialoglistener != null) {
//                        epgDialoglistener.onAddClick(dialog, epg_url);
//                    }
//                    dialog.dismiss();
//                } else {
//                    et_epg_url.setError(mContext.getString(R.string.str_enter_epg_url));
//                }
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void showDeviceTypeDialog(Context mContext,
//                                            final CustomInterface.deviceTypeListener deviceTypeListener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_device_type);
//        final LinearLayout ll_mobile = (LinearLayout) dialog.findViewById(R.id.ll_mobile);
//        final LinearLayout ll_tv = (LinearLayout) dialog.findViewById(R.id.ll_tv);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        final TextView text_ins = (TextView) dialog.findViewById(R.id.text_ins);
//        String deviceType = "";
//        if (CommonMethods.checkIsTelevisionByHardware(mContext)) {
//            MyApplication.getInstance().getPrefManager().wantTVLayout(true);
//            ll_mobile.setSelected(false);
//            ll_tv.setSelected(true);
//            deviceType = mContext.getString(R.string.setting_tv_layout);
//        } else {
//            MyApplication.getInstance().getPrefManager().wantTVLayout(false);
//            ll_mobile.setSelected(true);
//            ll_tv.setSelected(false);
//            deviceType = mContext.getString(R.string.setting_mobile_layout);
//        }
//        text_ins.setText(String.format(Locale.US,
//                mContext.getString(R.string.str_device_type_dialog), deviceType));
//        ll_mobile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyApplication.getInstance().getPrefManager().wantTVLayout(false);
//                ll_mobile.setSelected(true);
//                ll_tv.setSelected(false);
//            }
//        });
//
//        ll_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyApplication.getInstance().getPrefManager().wantTVLayout(true);
//                ll_mobile.setSelected(false);
//                ll_tv.setSelected(true);
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (deviceTypeListener != null)
//                    deviceTypeListener.onDone(dialog);
//                dialog.dismiss();
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void showSmartTVCodeDialog(final Context mContext,
//                                             final CustomInterface.smartTVCodeListener listener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_smarttvprocode);
//        final EditText text_code = (EditText) dialog.findViewById(R.id.et_code);
//        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_submit);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_close_app);
//
//        if (BuildConfig.DEBUG) {
//            text_code.setText("CA9181");
//        }
//
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (listener != null)
//                    listener.onCloseApp(dialog);
//            }
//        });
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String code = text_code.getText().toString().toUpperCase();
//                if (code.length() > 0) {
//                    if (listener != null)
//                        listener.onSubmit(dialog, code);
//                    dialog.dismiss();
//                } else {
//                    text_code.setError(mContext.getString(R.string.str_enter_code));
//                }
//
//            }
//        });
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void autoBootDialog(final Context mContext,
//                                      final CustomInterface.dialogBootOnStartup listener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_auto_boot);
//        final TextView text_exit = (TextView) dialog.findViewById(R.id.text_exit);
//        final TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_yes);
//        final TextView btn_no = (TextView) dialog.findViewById(R.id.btn_no);
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyApplication.getInstance().getPrefManager().showStartOnBootUp(false);
//                dialog.dismiss();
//                if (listener != null)
//                    listener.onDone(dialog);
//            }
//        });
//
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MyApplication.getInstance().getPrefManager().showStartOnBootUp(true);
//                dialog.dismiss();
//                if (listener != null)
//                    listener.onDone(dialog);
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//
//        dialog.show();
//    }
//
//    public static void showpermissiondiaolog(final Context mContext,
//                                             final CustomInterface.dialogPermission listener) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_askpermission);
//        final TextView text_exit = (TextView) dialog.findViewById(R.id.text_exit);
//        final TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_yes);
//        final TextView btn_no = (TextView) dialog.findViewById(R.id.btn_no);
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                if (listener != null)
//                    listener.onlater(dialog);
//            }
//        });
//
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog.dismiss();
//                if (listener != null)
//                    listener.onYes(dialog);
//            }
//        });
//
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//
//        dialog.show();
//    }
//
//    public static void ShowWarningDialog(final Context mContext, String text, final CustomInterface.dialogWarningonLogout dialogWarningonLogout) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_refreshdatawarning);
//
//
//        TextView text_msg = dialog.findViewById(R.id.text_msg);
//        TextView btn_no = dialog.findViewById(R.id.btn_no);
//        TextView btn_yes = dialog.findViewById(R.id.btn_yes);
//        text_msg.setText(text);
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                dialogWarningonLogout.onNo(dialog);
//            }
//        });
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                dialogWarningonLogout.onYes(dialog);
//
//            }
//        });
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        dialog.show();
//    }
//
//    public static void ShowAlertForNOInternet(final Context mContext, String text, final CustomInterface.dialogWarningNoInternet dialogWarningNoInternet) {
//        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
//        dialog.setContentView(R.layout.dialog_recording_no_internet_);
//
//
//        TextView text_msg = dialog.findViewById(R.id.text_instruction);
//        TextView btn_no = dialog.findViewById(R.id.btn_cancel);
//        TextView btn_yes = dialog.findViewById(R.id.btn_ok);
//        text_msg.setText(text);
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                dialogWarningNoInternet.onNo(dialog);
//            }
//        });
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                dialogWarningNoInternet.onYes(dialog);
//
//            }
//        });
//
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.setType(WindowManager.LayoutParams.TYPE_TOAST);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        }
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        }
//
//        dialog.show();
//    }
}
