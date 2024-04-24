package com.smartrecording.recordingplugin.activity;

import static com.purple.iptv.player.utils.UtilConstant.ToastE;
import static com.purple.iptv.player.utils.UtilConstant.ToastS;
import static com.purple.iptv.player.utils.UtilConstant.currently_selected_background_image;
import static com.purple.iptv.player.utils.UtilMethods.betweenExclusive;
import static com.purple.iptv.player.utils.UtilMethods.convertmilisectodattime;
import static com.purple.iptv.player.utils.UtilMethods.gettimewithplus1mins;
import static com.smartrecording.recordingplugin.service.RecordingService.isrunningdownloadtask_s1;
import static com.smartrecording.recordingplugin.service.RecordingService.runningtaskendtime_s1;
import static com.smartrecording.recordingplugin.service.RecordingService2.isrunningdownloadtask_s2;
import static com.smartrecording.recordingplugin.service.RecordingService2.runningtaskendtime_s2;
import static com.smartrecording.recordingplugin.utils.Constant.getAndroid10AbovePath;
import static com.smartrecording.recordingplugin.utils.Constant.isAndroid10_or_Above;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.fof.android.vlcplayer.utils.UtilMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purple.iptv.player.models.ConnectionInfoModel;
import com.purple.iptv.player.utils.UtilConstant;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.database.DatabaseRoom;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;
import com.smartrecording.recordingplugin.model.RemoteConfigModel;
import com.smartrecording.recordingplugin.receiver.MyBroadcastReceiver;
import com.smartrecording.recordingplugin.service.RecordingService;
import com.smartrecording.recordingplugin.service.RecordingService2;
import com.smartrecording.recordingplugin.utils.Constant;
import com.smartrecording.recordingplugin.utils.FileUtils;
import com.smartrecording.recordingplugin.utils.HybridFile;
import com.smartrecording.recordingplugin.utils.OpenMode;
import com.smartrecording.recordingplugin.utils.PreferencesConstants;
import com.xunison.recordingplugin.BuildConfig;
import com.xunison.recordingplugin.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Context context;
    Intent intent;
    String action;
    String type;
    String pkgname = "";
    //ProgressBar progress_circular;
    private MyBroadcastReceiver MyReceiver;
    ColorModel colorModel;
    String whichtosend = "";
    // public String oppathe = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
//        ToastE(this,"Tyson");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // setContentView(R.layout.activity_main);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            startprogress();
            MyReceiver = new MyBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter("com.purple.iptv.player");
            if (intentFilter != null) {
                registerReceiver(MyReceiver, intentFilter);
            }
            // Log.e(TAG, "onCreate: " + FileUtils.get_external_sd_card_directory());
            Receiveintentdata();
            checkpermission();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: catch1:" + e.getMessage());
            ToastE(this, "Something Went Wrong !!!");
            // Toast.makeText(this, "Something Went Wrong !!!", Toast.LENGTH_SHORT).show();
            stopprogress();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  stopService(mServiceIntent);
        if (MyReceiver != null)
            unregisterReceiver(MyReceiver);
        /* this to prevent leaked activity error*/
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    Dialog dialog;

    private void startprogress() {
        dialog = new Dialog(MainActivity.this, R.style.Theme_D1NoTitleDim) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                finish();
            }
        };
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_notification);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = -2;
        layoutParams.gravity = 17;
        // dialog.requestWindowFeature(1);
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }

    private void stopprogress() {
        Log.e(TAG, "stopprogress: called");
        if (dialog != null) {
            dialog.dismiss();
        }
        finish();
    }


    private void Receiveintentdata() {
        Log.e(TAG, "Receiveintentdata: 1");
        intent = getIntent();
        Log.e(TAG, "Receiveintentdata: 2");
        action = intent.getAction();
        Log.e(TAG, "Receiveintentdata: 3");
        type = intent.getType();
        Log.e(TAG, "Receiveintentdata: 4");
    }

    private void checkpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                    FileUtils.getPermission());
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //showing dialog to select image
                Log.e(TAG, "permission granted");
                gotoNext(action, type, intent);
            } else {
                Log.e(TAG, "permission not granted");
                ActivityCompat.requestPermissions(MainActivity.this,
                        FileUtils.getPermissionList(), 1);
            }
        } else {
            gotoNext(action, type, intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        {

            if (requestCode == 1) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "onRequestPermissionsResult: permission done");
                    gotoNext(action, type, intent);

                } else {
                    Log.e(TAG, "onRequestPermissionsResult: permission not  done");
                    //gotoNext();
                    //    Toast.makeText(this, "Oops Permission not granted so we can't Record !!!", Toast.LENGTH_SHORT).show();
                    ToastE(this, "Oops Permission not granted so we can't Record !!!");
                    stopprogress();
                }


            }
        }
    }

    private void gotoNext(String action, String type, Intent intent) {
        Log.e(TAG, "gotoNext: action:" + action);
        Log.e(TAG, "gotoNext: type:" + type);
        Log.e(TAG, "gotoNext: intent:" + intent.getAction());
        Log.e(TAG, "gotoNext: intent getextra:" + intent.getExtras());
        Log.e(TAG, "gotoNext: intent getdirectrecord:" + intent.hasExtra("getdirectrecord"));
        Log.e(TAG, "gotoNext: intent pkgname:" + intent.getStringExtra("pkgname"));

        try {
            if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_VIEW.equals(action) && type != null && !type.equals("")) {

                //    if (intent.hasExtra("pkgname") && haspackge(intent.getStringExtra("pkgname"))) {
                if (intent.hasExtra("pkgname")) {
                    pkgname = intent.getStringExtra("pkgname");
                    // giving external permission
                    if (!isAndroid10_or_Above()) {
                        if (intent.hasExtra("path") && !intent.getStringExtra("path").contains("emulated") && MyApplication.getInstance().getPrefManager().getExternalStorageUri() == null) {
                            //  showSDPermissionDialog(this, intent.getStringExtra("path"));
                            Log.e(TAG, "gotoNext: 555......1");
                            OpenMode openMode = OpenMode.FILE;
                            Log.e(TAG, "gotoNext: 555......2");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                FileUtils.mkfile(openMode, intent.getStringExtra("path"), intent.getStringExtra("recoding_file_name"), MainActivity.this);
                                Log.e(TAG, "gotoNext: 555......3");
                                return;
                            }
                        }
                        //checking external path is given and even its  writable or not

                        if (intent.hasExtra("path") && !intent.getStringExtra("path").contains("emulated") && MyApplication.getInstance().getPrefManager().getExternalStorageUri() != null && !checkExternalStoragewritebleornot(MainActivity.this)) {
                            // not writble
                            Log.e(TAG, "gotoNext: 555......4");
                            stopprogress();
                            return;

                        }
                    }


                    if (type.contains("*/*") && intent.hasExtra("getinsertdata")) {
                        insertintodbandschdule(intent);
                    } else if (type.contains("*/*") && intent.hasExtra("insertconnectionmodel")) {
                        // not needed now
                        insertintoconnectionmodel(intent);
                    } else if ((type.contains("*/*") || type.contains("video/*")) && (intent.hasExtra("getdirectrecord"))) {
                        handleSendText(intent); // Handle text being sent
                    } else if (type.contains("*/*") && intent.hasExtra("deletedataandinsertagain")) {
                        deletealldataandrescagain(intent);
                    } else if (type.contains("*/*") && intent.hasExtra("showrecordinglist")) {
                        getbundledatawithremotemodel(intent);
                        Gson gson = new Gson();
                        if (colorModel == null) {
                            ColorModel colorModel = new ColorModel();
                            colorModel.setUnselected_btn_color(getResources().getColor(R.color.unselected_btn_color));
                            colorModel.setUnselected_categoryList(getResources().getColor(R.color.unselected_categoryList));
                            colorModel.setSelected_color(getResources().getColor(R.color.selected_color));
                            colorModel.setFocused_selected_color(getResources().getColor(R.color.focused_selected_color));
                            colorModel.setSelected_categoryList(getResources().getColor(R.color.selected_categoryList));
                            colorModel.setSecondary_text_color(getResources().getColor(R.color.secondary_text_color));
                            colorModel.setColor_dialog_bg(getResources().getColor(R.color.color_dialog_bg));
                            colorModel.setTab_selected(getResources().getColor(R.color.tab_selected));
                            colorModel.setFocused_color(getResources().getColor(R.color.focused_color));
                            this.colorModel = colorModel;
                        }
                        String colorModelforrecording = gson.toJson(colorModel);
                        Log.e(TAG, "gotoNext: colorModelforrecording:" + colorModelforrecording);
                        if (colorModelforrecording != null)
                            startActivity(new Intent(MainActivity.this, RecordingActivity.class).putExtra("pkgname", pkgname).putExtra("colorModelforrecording", colorModelforrecording).putExtra("path", intent.getStringExtra("path")));
                        finish();

                    } else {
//                        startActivity(new Intent(MainActivity.this, RecordingActivity.class).putExtra("pkgname", pkgname));
//                        finish();
                        Log.e(TAG, "gotoNext: no intent to be handle now");
                        stopprogress();
                    }
                } else {
                    stopprogress();
                }
            } else {
                // its from boot receiver
                Log.e(TAG, "gotoNext: else ");
                ReScheduleAllTaskonDeviceRestart();
                // startActivity(new Intent(MainActivity.this, RecordingActivity.class).putExtra("pkgname", pkgname));


            }
        } catch (Exception e) {
            ToastE(this, "Something Went Wrong !!!");
            // Toast.makeText(this, "Something Went Wrong !!!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: catch:" + e.getMessage());
            stopprogress();
        }

    }

    List<RecordingScheduleModel> restartmodel = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private void ReScheduleAllTaskonDeviceRestart() {
        Log.e(TAG, "ReScheduleAllTaskonDeviceRestart: ");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground: ReScheduleAllTaskonDeviceRestart ");
                restartmodel = DatabaseRoom.with(MainActivity.this).getAllScheduleRecording(-1, "");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (restartmodel != null && !restartmodel.isEmpty()) {
                    Log.e(TAG, "onPostExecute:restartmodel is not null");
                    Log.e(TAG, "onPostExecute:restartmodel size:" + restartmodel.size());
                    deletealldataandinsertagain(MainActivity.this, restartmodel);
                } else {
                    Log.e(TAG, "onPostExecute:restartmodel is null or blank");
                    stopprogress();

                }
            }
        }.execute();
    }

    private void getbundledatawithremotemodel(Intent intent) {
        Gson gson = new Gson();
        Bundle todoBundle = intent.getBundleExtra("showrecordinglist");
        String reqtype = todoBundle != null ? todoBundle.getString("reqtype") : "";

        if (todoBundle != null && todoBundle.containsKey("remoteConfigModelforrecording") && reqtype != null && !reqtype.equals("")) {
            currently_selected_background_image = todoBundle.getString("currently_selected_background_image");
            MyApplication.getInstance().getPrefManager().setbgimage(UtilConstant.currently_selected_background_image);
            RemoteConfigModel remoteConfigModel = gson.fromJson(todoBundle.getString("remoteConfigModelforrecording"), RemoteConfigModel.class);
            colorModel = gson.fromJson(todoBundle.getString("colorModelforrecording"), ColorModel.class);
            MyApplication.getInstance().getPrefManager().storeRemoteConfig(remoteConfigModel);

        }
    }

    private boolean haspackge(String pkgname) {
        int size = Constant.pkgname.length;
        for (int i = 0; i < size; i++) {
            if (Constant.pkgname[i].equalsIgnoreCase(pkgname)) {
                return true;

            }
        }
        return false;

    }

    List<RecordingScheduleModel> mList = new ArrayList<>();

    private void deletealldataandrescagain(Intent intent) {
        Gson gson = new Gson();
        Bundle todoBundle = intent.getBundleExtra("deletedataandinsertagain");
        String reqtype = todoBundle != null ? todoBundle.getString("reqtype") : "";
        if (todoBundle != null && todoBundle.containsKey("recordingScheduleModellist") && reqtype != null && !reqtype.equals("")) {
            mList = gson.fromJson(todoBundle.getString("recordingScheduleModellist"), new TypeToken<ArrayList<RecordingScheduleModel>>() {
            }.getType());
            if (mList != null) {
                deletealldataandinsertagain(this, mList);
                Log.e(TAG, "deletealldataandrescagain: mList:" + mList.size());
            } else {
                Log.e(TAG, "deletealldataandrescagain: mList is null");
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void deletealldataandinsertagain(final Context mainActivity, final List<RecordingScheduleModel> mList) {
        Log.e(TAG, "deletealldataandinsertagain: mList :" + mList.size());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseRoom.with(mainActivity).deleteAllRecording();
                for (RecordingScheduleModel recordingScheduleModel : mList) {
                    //  checkalredyhavingtime(recordingScheduleModel);
                    requesttoDirectinsertindb(recordingScheduleModel, mainActivity);// TODO
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                stopprogress();
            }
        }.execute();
    }

    ConnectionInfoModel connectionInfoModel;

    private void insertintoconnectionmodel(Intent intent) {

        Bundle todoBundle = intent.getBundleExtra("insertconnectionmodel");
        Log.e(TAG, "insertintoconnectionmodel: 1");
        String reqtype = todoBundle != null ? todoBundle.getString("reqtype") : "";
        Log.e(TAG, "insertintoconnectionmodel: 2");
        if (todoBundle != null && todoBundle.containsKey("connectionInfoModel") && reqtype != null && !reqtype.equals("")) {
            Log.e(TAG, "insertintoconnectionmodel: 3");
            connectionInfoModel = todoBundle.getParcelable("connectionInfoModel");
            Log.e(TAG, "insertintoconnectionmodel: 4");
            insertconnectionintodb(this, connectionInfoModel);

        } else {
            Log.e(TAG, "insertintoconnectionmodel: 5");
            Log.e(TAG, "insertintoconnectionmodel: else :");
            stopprogress();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertconnectionintodb(final Context mContext, final ConnectionInfoModel connectionInfoModel) {
        if (connectionInfoModel != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    DatabaseRoom.with(mContext).addConnection(connectionInfoModel);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    stopprogress();
                }
            }.execute();
        } else {
            Log.e(TAG, "insertconnectionintodb:connectionInfoModel is null ");
            stopprogress();
        }
    }

    RecordingScheduleModel recordingScheduleModel;

    private void insertintodbandschdule(Intent intent) {
        Log.e(TAG, "insertintodbandschdule: ");
        Gson gson = new Gson();
        Bundle todoBundle = intent.getBundleExtra("getinsertdata");
        String reqtype = todoBundle != null ? todoBundle.getString("reqtype") : "";
        // MyApplication.getInstance().getPrefManager().setRecordingStoragePath(intent.getStringExtra("path"));
        if (todoBundle != null && todoBundle.containsKey("recordingScheduleModel") && reqtype != null && !reqtype.equals("")) {

            recordingScheduleModel = gson.fromJson(todoBundle.getString("recordingScheduleModel"), RecordingScheduleModel.class);
            if (reqtype.equals("insertandsc")) {
                /*check if service1 has runnig some task or not*/
                if (isrunningdownloadtask_s1) {
                   /*service1 recording something , check given schedule doesn't intercept in current recording
                    runningtaskendtime < recordingScheduleModel.getStartTime() = 11:30<11:31 so insert and schedule task*/
                    if (runningtaskendtime_s1 != 0 && runningtaskendtime_s1 < recordingScheduleModel.getStartTime()) {
                        /* s1 is running but it will ends on before new task start so add to db and schedule*/
                        whichtosend = "s1";
                        requesttoinsertindb(recordingScheduleModel, this);
                    } else {
                       /* s1 is running but current task is not completing within new task's start time
                         now checking for service2 is accepting or not*/
                        if (isrunningdownloadtask_s2) {
                            /*service2 recording something , check given schedule doesn't intercept in current recording*/
                            if (runningtaskendtime_s2 != 0 && runningtaskendtime_s2 < recordingScheduleModel.getStartTime()) {
                                /*    s2 is running but it will ends on before new task start so add to db and schedule*/
                                whichtosend = "s2";
                                requesttoinsertindb(recordingScheduleModel, this);
                            } else {
                               /* s2 is running but it will not ends on before new task start so don't add to db
                                 no service can records so give user to error*/
                                whichtosend = "";
                                ToastE(this, "Recording already started for another task or you can't record untill current task finish, please schedule for another time range.");
                                stopprogress();


                            }

                        } else {
                            /* s2 not running , now add to db and schedule*/
                            whichtosend = "s2";
                            requesttoinsertindb(recordingScheduleModel, this);
                        }
//                        ToastE(this, "Recording already started for another task or you can't record untill current task finish, please schedule for another time range.");
//                        stopprogress();
                    }

                } else {
                    /* no service currently recording now so add to db and schedule*/
                    whichtosend = "";
                    checkalredyhavingtime(recordingScheduleModel);
                    stopprogress();


                }
            }
        } else {
            Log.e(TAG, "insertintodbandschdule: else");
            stopprogress();
        }
    }

    List<RecordingScheduleModel> recordingScheduleModels = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private void checkalredyhavingtime(RecordingScheduleModel recordingScheduleMode) {


//        try {
        new AsyncTask<Boolean, Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground(Boolean... strings) {
                // recordingScheduleModels = DatabaseRoom.with(MainActivity.this).getAllScheduleRecordingwithpkgname(0, pkgname); TODO
                recordingScheduleModels = DatabaseRoom.with(MainActivity.this).getAllScheduleRecording(0, pkgname);
                Log.e(TAG, "doInBackground: recordingScheduleModels:size:" + recordingScheduleModels.size());
                return true;
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                if (recordingScheduleModels != null && !recordingScheduleModels.isEmpty()) {
                    /* Some recording found
                    Sort by upcoming show by time */
                    Collections.sort(recordingScheduleModels, new Comparator<RecordingScheduleModel>() {
                        @Override
                        public int compare(RecordingScheduleModel o1, RecordingScheduleModel o2) {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
                            try {
                                Date date1 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o1).getStartTime())));
                                Date date2 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o2).getStartTime())));
                                return date1.compareTo(date2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                if (BuildConfig.DEBUG)
                                    Log.e(TAG, "compare: catch" + e.getMessage());
                            }
                            return 0;
                        }

                    });
                    Log.e(TAG, "doInBackground: checkalredyhavingtime: not null");

                    for (RecordingScheduleModel recordingScheduleModel : recordingScheduleModels) {
                        /* here checking for already having in timeline or not*/
                        long startimeforcurrent = recordingScheduleMode.getStartTime();
                        long endtimeforcurrent = recordingScheduleMode.getEndTime();
                        long starttimeforgiven = recordingScheduleModel.getStartTime();
                        long endtimeforgiven = recordingScheduleModel.getEndTime();

                        if (betweenExclusive(startimeforcurrent, starttimeforgiven, endtimeforgiven) || betweenExclusive(endtimeforcurrent, starttimeforgiven, endtimeforgiven) || (startimeforcurrent == starttimeforgiven || endtimeforcurrent == endtimeforgiven)) {
                            /* already having task ,  */
                            ishaving = true;
                            String getservicenumber = recordingScheduleModel.getServicenumber();
                            Log.e(TAG, "onPostExecute: getservicenumber :" + getservicenumber);
                            if (getservicenumber.equals("s1")) {
                                Log.e(TAG, "onPostExecute: found on s1");
                                whichtosend = "s2";
                                requesttoinsertindb(recordingScheduleMode, MainActivity.this);
                                stopprogress();
                                return;
                            } else {
                                ToastE(MainActivity.this, "Given show time duration contain another show time , please select another program.");
                                stopprogress();
                            }
                            Log.e(TAG, "doInBackground: checkalredyhavingtime already have");
                            return;
                        } else {
                            ishaving = false;
                            // not have
                            Log.e(TAG, "doInBackground: checkalredyhavingtime already not have");


                            // return;
                        }
                    }
                    if (!ishaving) {
                        whichtosend = "s1";
                        requesttoinsertindb(recordingScheduleMode, MainActivity.this);
                    }
                } else {
                    Log.e(TAG, "doInBackground: checkalredyhavingtime:   null");

                    /* No recording found for scheduled*/
                    whichtosend = "s1";

                    requesttoinsertindb(recordingScheduleMode, MainActivity.this);
                }

            }

        }.execute();

    }

    boolean ishaving;
    RecordingScheduleModel rSModelwithService;

    @SuppressLint("StaticFieldLeak")
    private void requesttoinsertindb(final RecordingScheduleModel recordingScheduleModel,
                                     final Context mContext) {
        Log.e(TAG, "requesttoinsertindb: ");
        if (recordingScheduleModel != null) {
            new AsyncTask<Void, Void, Void>() {
                long uid;
                String error = null;

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        rSModelwithService = new RecordingScheduleModel();
                        rSModelwithService.setConnection_id(recordingScheduleModel.getConnection_id());
                        rSModelwithService.setShowName(recordingScheduleModel.getShowName());
                        rSModelwithService.setStartTime(recordingScheduleModel.getStartTime());
                        rSModelwithService.setEndTime(recordingScheduleModel.getEndTime());
                        rSModelwithService.setStatus(recordingScheduleModel.getStatus());
                        rSModelwithService.setUrl(recordingScheduleModel.getUrl());
                        rSModelwithService.setChannelName(recordingScheduleModel.getChannelName());
                        rSModelwithService.setPkgname(recordingScheduleModel.getPkgname());
                        if (!isAndroid10_or_Above()) {
                            rSModelwithService.setRecordpath(recordingScheduleModel.getRecordpath());
                        } else {
                            rSModelwithService.setRecordpath(getAndroid10AbovePath(mContext));
                        }
                        rSModelwithService.setServicenumber((whichtosend != null && !whichtosend.equals("") ? whichtosend : "none"));
                        whichtosend = "";
                        if (!rSModelwithService.getServicenumber().equals("") && !rSModelwithService.getServicenumber().equals("none")) {
                            /*Don't insert to db bcz no service can handle*/
                            Log.e(TAG, "doInBackground: requesttoinsertindb ");
                            DatabaseRoom.with(mContext).insertScheduleRecording(rSModelwithService);
                            uid = DatabaseRoom.with(mContext).
                                    getScheduleRecordingUidSpedific(
                                            rSModelwithService.getUrl(), rSModelwithService.getStartTime(), rSModelwithService.getEndTime(), rSModelwithService.getRecordpath(), rSModelwithService.getShowName());
                            UtilMethods.LogMethod("schedule123_uid", String.valueOf(uid));
                            Log.e(TAG, "doInBackground: uidL" + uid);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "doInBackground: catch:" + e.getMessage());
                        error = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    try {
                        Log.e(TAG, "onPostExecute: requesttoinsertindb");
                        final long startTime = rSModelwithService.getStartTime();
                        long endTime = rSModelwithService.getEndTime();
                        long durationInMilli = endTime - startTime;
                        UtilMethods.LogMethod("schedule123_startTime", String.valueOf(startTime));
                        UtilMethods.LogMethod("schedule123_endTime", String.valueOf(endTime));
                        UtilMethods.LogMethod("schedule123_durationInMilli", String.valueOf(durationInMilli));
                        if (durationInMilli != -1 && rSModelwithService.getUrl() != null && uid != -1 && (!rSModelwithService.getServicenumber().equals("") && !rSModelwithService.getServicenumber().equals("none"))) {
                            final int durationInMin = (int) (durationInMilli / 60000);
                            UtilMethods.LogMethod("schedule123_durationInMin",
                                    String.valueOf(durationInMin));
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @SuppressLint("ScheduleExactAlarm")
                                @Override
                                public void run() {
                                    Log.e(TAG, "run:sc: recoding_file_name:" + rSModelwithService.getShowName().replaceAll("[^a-zA-Z0-9&.]+", "_"));
                                    Log.e(TAG, "run:sc: downloadUrl:" + rSModelwithService.getUrl());
                                    Log.e(TAG, "run:sc: minute:" + durationInMin);
                                    Log.e(TAG, "run:sc: uid:" + uid);
                                    Log.e(TAG, "run:sc: path:" + rSModelwithService.getRecordpath());
                                    Log.e(TAG, "run:sc: pkgname:" + rSModelwithService.getPkgname());

                                    Intent i = null;
                                    if (rSModelwithService.getServicenumber().equals("s1")) {
                                        i = new Intent(mContext, RecordingService.class);
                                    } else {
                                        i = new Intent(mContext, RecordingService2.class);
                                    }

                                    i.putExtra("recoding_file_name", rSModelwithService.getShowName().replaceAll("[^a-zA-Z0-9&.]+", "_"));
                                    i.putExtra("downloadUrl", rSModelwithService.getUrl());
                                    i.putExtra("minute", durationInMin);
                                    i.putExtra("uid", uid);
                                    i.putExtra("path", rSModelwithService.getRecordpath());
                                    i.putExtra("pkgname", rSModelwithService.getPkgname());




                                    PendingIntent contentIntent = null;
                                    if (Build.VERSION.SDK_INT >= 31) {
                                        Log.e(TAG, "newNotification if....: ");
                                        contentIntent = PendingIntent.getService(
                                                mContext, (int) uid, i,
                                                PendingIntent.FLAG_MUTABLE);

                                    } else {
                                        contentIntent = PendingIntent.getService(mContext, (int) uid
                                                , i, PendingIntent.FLAG_IMMUTABLE);
                                    }

                                    AlarmManager alarm =
                                            (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                    if (alarm != null) {
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            UtilMethods.LogMethod("schedule123_alarm",
                                                    String.valueOf(alarm));
                                            alarm.setExact(AlarmManager.RTC_WAKEUP,
                                                    startTime, contentIntent);
                                        } else {
                                            alarm.setExact(AlarmManager.RTC_WAKEUP,
                                                    startTime, contentIntent);
                                        }
                                    }
                                }
                            }, 100); //Time Delay
                            ToastS(MainActivity.this, "Recording Scheduled Successfully");
                            stopprogress();
                        } else {
                            ToastE(MainActivity.this, "Recording error , please try again");

                        }
                    } catch (
                            Exception e) {
                        Log.e(TAG, "doInBackground: catch:" + e.getMessage());
                        error = e.getMessage();
                        stopprogress();
                    }
                }
            }.execute();

        }

    }

    @SuppressLint("StaticFieldLeak")
    private void requesttoDirectinsertindb(final RecordingScheduleModel recordingScheduleModel,
                                           final Context mContext) {
        Log.e(TAG, "requesttoinsertindb: ");
        if (recordingScheduleModel != null) {
            new AsyncTask<Void, Void, Void>() {
                long uid;
                String error = null;

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        rSModelwithService = new RecordingScheduleModel();
                        rSModelwithService.setConnection_id(recordingScheduleModel.getConnection_id());
                        rSModelwithService.setShowName(recordingScheduleModel.getShowName());
                        rSModelwithService.setStartTime(recordingScheduleModel.getStartTime());
                        rSModelwithService.setEndTime(recordingScheduleModel.getEndTime());
                        rSModelwithService.setStatus(recordingScheduleModel.getStatus());
                        rSModelwithService.setUrl(recordingScheduleModel.getUrl());
                        rSModelwithService.setChannelName(recordingScheduleModel.getChannelName());
                        rSModelwithService.setPkgname(recordingScheduleModel.getPkgname());
                        if (!isAndroid10_or_Above()) {
                            rSModelwithService.setRecordpath(recordingScheduleModel.getRecordpath());
                        } else {
                            rSModelwithService.setRecordpath(getAndroid10AbovePath(mContext));
                        }
                        rSModelwithService.setServicenumber(recordingScheduleModel.getServicenumber());
                        whichtosend = "";
                        if (!rSModelwithService.getServicenumber().equals("") && !rSModelwithService.getServicenumber().equals("none")) {
                            /*Don't insert to db bcz no service can handle*/
                            Log.e(TAG, "doInBackground: requesttoinsertindb ");
                            DatabaseRoom.with(mContext).insertScheduleRecording(rSModelwithService);
                            uid = DatabaseRoom.with(mContext).
                                    getScheduleRecordingUidSpedific(
                                            rSModelwithService.getUrl(), rSModelwithService.getStartTime(), rSModelwithService.getEndTime(), rSModelwithService.getRecordpath(), rSModelwithService.getShowName());
                            UtilMethods.LogMethod("schedule123_uid", String.valueOf(uid));
                            Log.e(TAG, "doInBackground: uidL" + uid);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "doInBackground: catch:" + e.getMessage());
                        error = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    try {
                        Log.e(TAG, "onPostExecute: requesttoinsertindb");
                        final long startTime = rSModelwithService.getStartTime();
                        long endTime = rSModelwithService.getEndTime();
                        long durationInMilli = endTime - startTime;
                        UtilMethods.LogMethod("schedule123_startTime", String.valueOf(startTime));
                        UtilMethods.LogMethod("schedule123_endTime", String.valueOf(endTime));
                        UtilMethods.LogMethod("schedule123_durationInMilli", String.valueOf(durationInMilli));
                        if (durationInMilli != -1 && rSModelwithService.getUrl() != null && uid != -1 && (!rSModelwithService.getServicenumber().equals("") && !rSModelwithService.getServicenumber().equals("none"))) {
                            final int durationInMin = (int) (durationInMilli / 60000);
                            UtilMethods.LogMethod("schedule123_durationInMin",
                                    String.valueOf(durationInMin));
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @SuppressLint("ScheduleExactAlarm")
                                @Override
                                public void run() {
                                    Log.e(TAG, "run:sc: recoding_file_name:" + rSModelwithService.getShowName().replaceAll("[^a-zA-Z0-9&.]+", "_"));
                                    Log.e(TAG, "run:sc: downloadUrl:" + rSModelwithService.getUrl());
                                    Log.e(TAG, "run:sc: minute:" + durationInMin);
                                    Log.e(TAG, "run:sc: uid:" + uid);
                                    Log.e(TAG, "run:sc: path:" + rSModelwithService.getRecordpath());
                                    Log.e(TAG, "run:sc: pkgname:" + rSModelwithService.getPkgname());
                                    Intent i = null;
                                    if (rSModelwithService.getServicenumber().equals("s1")) {
                                        i = new Intent(mContext, RecordingService.class);
                                    } else {
                                        i = new Intent(mContext, RecordingService2.class);
                                    }

                                    i.putExtra("recoding_file_name", rSModelwithService.getShowName().replaceAll("[^a-zA-Z0-9&.]+", "_"));
                                    i.putExtra("downloadUrl", rSModelwithService.getUrl());
                                    i.putExtra("minute", durationInMin);
                                    i.putExtra("uid", uid);
                                    i.putExtra("path", rSModelwithService.getRecordpath());
                                    i.putExtra("pkgname", rSModelwithService.getPkgname());




                                    PendingIntent contentIntent = null;
                                    if (Build.VERSION.SDK_INT >= 31) {
                                        Log.e(TAG, "newNotification if....: ");
                                        contentIntent = PendingIntent.getService(
                                                mContext,  (int) uid, i,
                                                PendingIntent.FLAG_MUTABLE);

                                    } else {
                                        contentIntent = PendingIntent.getService(mContext,  (int) uid, i, PendingIntent.FLAG_IMMUTABLE);
                                    }

                                    AlarmManager alarm =
                                            (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                    if (alarm != null) {
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            UtilMethods.LogMethod("schedule123_alarm",
                                                    String.valueOf(alarm));
                                            alarm.setExact(AlarmManager.RTC_WAKEUP,
                                                    gettimewithplus1mins(startTime), contentIntent);
                                        } else {
                                            alarm.setExact(AlarmManager.RTC_WAKEUP,
                                                    gettimewithplus1mins(startTime), contentIntent);
                                        }
                                    }
                                }
                            }, 100); //Time Delay
                            ToastS(MainActivity.this, "Recording Scheduled Successfully");
                            stopprogress();
                        } else {
                            ToastE(MainActivity.this, "Recording error , please try again");

                        }
                    } catch (
                            Exception e) {
                        Log.e(TAG, "doInBackground: catch:" + e.getMessage());
                        error = e.getMessage();
                        stopprogress();
                    }
                }
            }.execute();

        }

    }

    String downloadUrl;
    String recoding_file_name;
    static String path;
    int minute;
    long endTime;

    private void handleSendText(Intent intent) {
        recoding_file_name = intent.getStringExtra("recoding_file_name");
        downloadUrl = intent.getStringExtra("downloadUrl");
        if (isAndroid10_or_Above()) {
            path = getAndroid10AbovePath(this);
        } else {
            path = intent.getStringExtra("path");
        }

        int minute = intent.getIntExtra("minute", -1);
        Log.e(TAG, "handleSendText:recoding_file_name " + recoding_file_name);
        Log.e(TAG, "handleSendText:downloadUrl " + downloadUrl);
        Log.e(TAG, "handleSendText:minute " + minute);
        Log.e(TAG, "handleSendText:path " + path);

        endTime = System.currentTimeMillis() + minute * 60000;
        if (minute != -1 && intent.getStringExtra("getdirectrecord").equals("true")) {
            Intent i = null;
            if (MyApplication.getInstance().getPrefManager().getIsRunningRecording()) {
                //service 1 running
                if (!MyApplication.getInstance().getPrefManager().getIsRunningRecording2()) {
                    // service 2 is not running
                    i = new Intent(MainActivity.this, RecordingService2.class);
                } else {
                    // service 1 and service 2 both are running
                    Log.e(TAG, "handleSendText: already running both service");
                    ToastE(MainActivity.this, "You have already Recording started , first complete/stop the recording to do next Recording ");
                    stopprogress();
                    return;
                }
            } else {
                i = new Intent(MainActivity.this, RecordingService.class);
            }
//                Intent i = new Intent(MainActivity.this, RecordingService.class);
            i.putExtra("recoding_file_name", recoding_file_name);
            i.putExtra("downloadUrl", downloadUrl);
            i.putExtra("minute", minute);
            i.putExtra("path", path);
            i.putExtra("pkgname", pkgname);

            // check here if already running or not
//            if (MyApplication.getInstance().getPrefManager().getIsRunningRecording() && MyApplication.getInstance().getPrefManager().getIsRunningRecording2()) {
            //already running
//                Log.e(TAG, "handleSendText: already running");
//                ToastE(MainActivity.this, "You have already Recording started , first complete/stop the recording to do next Recording ");
//            } else {
            //not running recording start now
            Log.e(TAG, "handleSendText: start service");
            MainActivity.this.startService(i);
            ToastS(MainActivity.this, "Recording has Started.");
//            }

            stopprogress();


        } else {
            Log.e(TAG, "handleSendText: ");
        }

    }

    public void showSDPermissionDialog(final Context mContext, String path) {
        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
        dialog.setContentView(R.layout.dialog_sd_permission);
        final TextView text_instruction = (TextView) dialog.findViewById(R.id.text_instruction);
        final TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);

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
                triggerStorageAccessFramework((Activity) mContext);
            }
        });


        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    public void triggerStorageAccessFramework(Activity activity) {
        Intent intent = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        try {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activity.startActivityForResult(intent, 3);
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            stopprogress();
        }

//        }

    }

    private SharedPreferences sharedPrefs;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UtilMethods.LogMethod("app1234_treeUri", String.valueOf("onActivityResult"));
        if (requestCode == 3) {
            Uri treeUri;
            if (resultCode == Activity.RESULT_OK) {
                // Get Uri from Storage Access Framework.
                treeUri = data.getData();
                // Persist URI - this is required for verification of writability.
                if (treeUri != null) {
                    sharedPrefs.edit().putString(PreferencesConstants.PREFERENCE_URI,
                            treeUri.toString()).commit();
                    UtilMethods.LogMethod("app1234_treeUri", String.valueOf(treeUri.toString()));
                    MyApplication.getInstance().getPrefManager().
                            setExternalStorageUri(treeUri.toString());
                    FileUtils.mkFile(new HybridFile(OpenMode.FILE, intent.getStringExtra("path")), MainActivity.this, intent.getStringExtra("recoding_file_name"));
                    checkExternalStoragewritebleornot(MainActivity.this);
                    gotoNext(action, type, intent);
                }
            } else {
                // If not confirmed SAF, or if still not writable, then revert settings.
                /* DialogUtil.displayError(getActivity(), R.string.message_dialog_cannot_write_to_folder_saf, false, currentFolder);
                        ||!FileUtil.isWritableNormalOrSaf(currentFolder)*/
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

        } else {
            stopprogress();
        }
    }

    public static boolean checkExternalStoragewritebleornot(Context context) {
        if (true) {
            return true;
        }
        String file_name = "aaa";
        String external_url = MyApplication.getInstance().getPrefManager().
                getExternalStorageUri();
        Log.e(TAG, "checkExternalStoragewritebleornot: external_url:" + external_url);
        if (external_url != null) {
            Log.e(TAG, "checkExternalStoragewritebleornot: external_url is not null");
            DocumentFile document = DocumentFile.fromTreeUri(context,
                    Uri.parse(external_url));
            com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_document", String.valueOf(document));
            com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_external_url", String.valueOf(external_url));
            if (document != null) {
                try {
                    DocumentFile apkFile = document.createFile("video/MP2T",
                            file_name);
                    com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_apkFile", String.valueOf(apkFile));
                    if (apkFile != null) {
                        Log.e(TAG, "onRequestPermissionsResult: file created");
                        try {
                            Log.e(TAG, "delete: 2");
                            DocumentFile apkFile1 = document.findFile(file_name);
                            Log.e(TAG, "delete: 3");
                            if (apkFile1 != null && apkFile1.exists())
                                apkFile1.delete();
                            return true;
                        } catch (Exception e) {
                            Log.e(TAG, "delete: 4 catch:" + e.getMessage());
                            return false;
                        }
                    } else {

                        Log.e(TAG, "onRequestPermissionsResult: file not created");
                        Log.e(TAG, "backgroundTask: apkfile is null");
                        ToastE(context, "Storage Access Error please select another directory");
                        return false;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onRequestPermissionsResult: catch:" + e.getMessage());
                    com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_ee1111", String.valueOf(e));
                    ToastE(context, "Storage Access Error please select another directory");
                    return false;
                }
            }
        } else if (path != null && !path.equals("")) {
            return path.contains("emulated");
        }
        return false;
    }

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;

    private void downloadwithvlc() {
        final ArrayList<String> args = new ArrayList<>();

        if (com.fof.android.vlcplayer.BuildConfig.DEBUG) {
            args.add("-vvv"); // verbosity
        }

        args.add("--aout=opensles");
        args.add("--vout=android_display");
        args.add("--audio-time-stretch"); // time stretching
        args.add("--no-sub-autodetect-file");
        args.add("--swscale-mode=0");
        args.add("--network-caching=500");
        args.add("--no-drop-late-frames");
        args.add("--no-skip-frames");
        args.add("--avcodec-skip-frame");
        args.add("--avcodec-hw=any");
        mLibVLC = new LibVLC(MainActivity.this, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        Media media = new Media(mLibVLC, Uri.parse(downloadUrl));
        String directoryPath = path;
        String destination = directoryPath + "/" + recoding_file_name;
        File dirFile = new File(directoryPath);
        if (!dirFile.exists()) {
            boolean createFile = dirFile.mkdirs();

            Log.e(TAG, "checkvlc: new File(destination):" + new File(destination));
            if (!new File(destination).exists()) {
                UtilMethods.LogMethod("dir/file not exist",
                        String.valueOf(createFile));
                return;
            } else {
                Log.e(TAG, "downloadwithvlc: dir not exist 1");
            }
            UtilMethods.LogMethod("recordingService123_createFile",
                    String.valueOf(createFile));
        } else {
            Log.e(TAG, "downloadwithvlc: dir not exist 2");
        }
        media.addOption(":sout=#transcode{vcodec=mp4v,vb=1024,acodec=mp4a,ab=192,channels=2,deinterlace}:standard{access=file,mux=ts,dst=" + destination + "}");
        media.addOption(":sout-keep");
//
//        mMediaPlayer.play(media);
//        while (true) {
//            Log.e(TAG, "downloadwithvlc: System.currentTimeMillis():" + System.currentTimeMillis() + " >= endTime:" + endTime);
//            if (System.currentTimeMillis() >= endTime) {
//                Log.e(TAG, "downloadwithvlc: time over");
//                mMediaPlayer.stop();
//                mMediaPlayer.release();
//                mLibVLC.release();
//                break;
//            }
//            Log.e(TAG, "downloadwithvlc: media running");
//        }
//
//        Log.e(TAG, "downloadwithvlc: media release");
    }

}
