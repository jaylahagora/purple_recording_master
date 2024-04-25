package com.smartrecording.recordingplugin.fragment;


import static com.purple.iptv.player.utils.Config._REMAININGTIIME;
import static com.purple.iptv.player.utils.Config._downloadstatus;
import static com.purple.iptv.player.utils.UtilConstant.PKGFORRECORDING;
import static com.purple.iptv.player.utils.UtilConstant.ToastE;
import static com.purple.iptv.player.utils.UtilMethods.parseSpeed;
import static com.smartrecording.recordingplugin.utils.CommonMethods.appInstalledOrNot;
import static com.smartrecording.recordingplugin.utils.Constant.MY_TRIGGER;
import static com.smartrecording.recordingplugin.utils.Constant.MY_TRIGGER1;
import static com.smartrecording.recordingplugin.utils.Constant.RECORDING_FILENAME;
import static com.smartrecording.recordingplugin.utils.Constant._REMAININGTIIMESTR;
import static com.smartrecording.recordingplugin.utils.Constant._downloadspeed;
import static com.smartrecording.recordingplugin.utils.Constant.isAndroid10_or_Above;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.purple.iptv.player.common.CustomInterface;
import com.purple.iptv.player.common.RuntimePermissionClass;
import com.purple.iptv.player.utils.Config;
import com.purple.iptv.player.utils.ITrafficSpeedListener;
import com.purple.iptv.player.utils.TrafficSpeedMeasurer;
import com.purple.iptv.player.utils.UtilMethods;
import com.purple.iptv.player.views.PageHeaderView;
import com.smartrecording.recordingplugin.activity.RecordingActivity;
import com.smartrecording.recordingplugin.adapter.PopupAdapter;
import com.smartrecording.recordingplugin.adapter.RecordingMultiAdapter;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.database.DatabaseRoom;
import com.smartrecording.recordingplugin.events.Stoprecording1;
import com.smartrecording.recordingplugin.events.Stoprecording2;
import com.smartrecording.recordingplugin.model.BaseFakeModel;
import com.smartrecording.recordingplugin.model.BaseModel;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.model.RecordingModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;
import com.smartrecording.recordingplugin.service.RecordingService;
import com.smartrecording.recordingplugin.service.RecordingService2;
import com.smartrecording.recordingplugin.utils.CommonMethods;
import com.smartrecording.recordingplugin.utils.FileUtils;
import com.xunison.recordingplugin.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


public class RecordingFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "RecordingFragment";
    private static final boolean SHOW_SPEED_IN_BITS = false;
    public String _serviceRunningfilename = "";
    public String _serviceRunningfilename2 = "";
    MyReceiver myReceiver;
    MyReceiverfors2 myReceiverfors2;
    boolean issss = true;
    boolean issss2 = true;
    String storage_path;
    RecordingMultiAdapter dashBoardMultiAdapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
    // public static String[] PERMISSION_LIST = {/*Manifest.permission.WRITE_EXTERNAL_STORAGE,*/ Manifest.permission.READ_EXTERNAL_STORAGE};
    private String mParam1;
    private String mParam2;
    private RecyclerView recycler_category;
    private TextView text_category_name, text_downloadspeed;
    private TextView text_no_data;
    private TextView btn_change_directory;
    private TextView text_recording_path;
    private ProgressBar progressBar;
    private RecordingActivity mContext;
    private ArrayList<BaseModel> mList;
    private ArrayList<BaseFakeModel> baseFakeModelArrayList;
    private ArrayList<BaseModel> mListnew;
    private ArrayList<BaseModel> mListnewfortoday;
    private ArrayList<BaseModel> mListnewfortommorow;
    private ArrayList<BaseModel> mListnewforfuture;
    private ArrayList<BaseModel> mListnewforalreadyrecorded;
    private PageHeaderView header_view;
    private PopupWindow popupWindow;
    private int currentSelectedPosition;
    private FrameLayout frame_recording;
    private FrameLayout frame_recording2;
    private LinearLayout llcover;
    private LinearLayout llcover2;
    private TextView text_name, text_size, text_time, text_status;
    private TextView text_name2, text_size2, text_time2, text_status2;
    private TrafficSpeedMeasurer mTrafficSpeedMeasurer;
    //today ,tommorow
    private SimpleDateFormat simpleDateFormat;
    private ITrafficSpeedListener mStreamSpeedListener = new ITrafficSpeedListener() {

        @Override
        public void onTrafficSpeedMeasured(final double upStream, final double downStream) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String upStreamSpeed = parseSpeed(upStream, SHOW_SPEED_IN_BITS);
                    String downStreamSpeed = parseSpeed(downStream, SHOW_SPEED_IN_BITS);
                    text_downloadspeed.setText("Your Internet Speed: ⇑ " + upStreamSpeed + " - " + "⇓ " + downStreamSpeed);
                }
            });
        }
    };
    private ArrayList<RecordingScheduleModel> mListfordeletion;

    public RecordingFragment() {

    }

    public static RecordingFragment newInstance(String param1, String param2) {
        RecordingFragment fragment = new RecordingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static StateListDrawable getSelectorDrawableRound(int normalcolor, int focusecolor) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{-android.R.attr.state_focused}, createNormalDrawablewithcircle(normalcolor));
        out.addState(new int[]{android.R.attr.state_focused}, createNormalDrawablewithcircle(focusecolor));

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

    public static GradientDrawable createNormalDrawable(int color) {
        GradientDrawable out = new GradientDrawable();
        out.setCornerRadius(5);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mContext != null) {
                mContext.unregisterReceiver(myReceiver);
                mContext.unregisterReceiver(myReceiverfors2);
            }
            mTrafficSpeedMeasurer.stopMeasuring();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTrafficSpeedMeasurer != null)
            mTrafficSpeedMeasurer.removeListener(mStreamSpeedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        myReceiverfors2 = new MyReceiverfors2();
        IntentFilter intentFilter = new IntentFilter();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter.addAction(MY_TRIGGER);
        intentFilter1.addAction(MY_TRIGGER1);

        mContext.registerReceiver(myReceiver, intentFilter);
        mContext.registerReceiver(myReceiverfors2, intentFilter1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTrafficSpeedMeasurer != null)
            mTrafficSpeedMeasurer.registerListener(mStreamSpeedListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (RecordingActivity) getActivity();
        simpleDateFormat = CommonMethods.getEPGDateFormat(mContext);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (mContext.getCurrentFocus() != null &&
                        mContext.getCurrentFocus().getId() == R.id.frame_recording
                ) {
                    //  btn_change_directory.requestFocus();
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recording, container, false);
        if (mContext.colorModel == null) {
            ColorModel colorModel = new ColorModel();
            colorModel.setUnselected_btn_color(mContext.getResources().getColor(R.color.unselected_btn_color));
            colorModel.setUnselected_categoryList(mContext.getResources().getColor(R.color.unselected_categoryList));
            colorModel.setSelected_color(mContext.getResources().getColor(R.color.selected_color));
            colorModel.setFocused_selected_color(mContext.getResources().getColor(R.color.focused_selected_color));
            colorModel.setSelected_categoryList(mContext.getResources().getColor(R.color.selected_categoryList));
            colorModel.setSecondary_text_color(mContext.getResources().getColor(R.color.secondary_text_color));
            colorModel.setColor_dialog_bg(mContext.getResources().getColor(R.color.color_dialog_bg));
            colorModel.setTab_selected(mContext.getResources().getColor(R.color.tab_selected));
            colorModel.setFocused_color(mContext.getResources().getColor(R.color.focused_color));
            mContext.colorModel = colorModel;
        }
        bindView(v);
        bindData();
        resetserviceifnotrunning();
        return v;
    }

    private void resetserviceifnotrunning() {
        if (RecordingService._serviceRunningfilename == null || RecordingService._serviceRunningfilename.equalsIgnoreCase("")) {
            MyApplication.getInstance().getPrefManager().setIsrunningRecording(false);
        } else if (RecordingService2._serviceRunningfilename == null || RecordingService2._serviceRunningfilename.equalsIgnoreCase("")) {
            MyApplication.getInstance().getPrefManager().setIsrunningRecording2(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_change_directory) {
            if (!isAndroid10_or_Above()) {
                CommonMethods.openStorageDialog(mContext, text_recording_path,
                        new CustomInterface.onRecordingPathChanged() {
                            @Override
                            public void onChange() {

                                // loadData();
                                startActivity(new Intent(getContext(), RecordingActivity.class).putExtra("pkgname", mContext.pkgname));
                                getActivity().finish();
                            }
                        });
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(storage_path); // a directory
                    intent.setDataAndType(uri, "*/*");
                    startActivity(Intent.createChooser(intent, "Open folder"));
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastE(requireContext(), "Can't open File manager, Please install File-Manager.");
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        RuntimePermissionClass.getInstance((Activity) mContext).
                onRequestPermissionsResult(requestCode, permissions,
                        grantResults, new RuntimePermissionClass.onPermissionResult() {
                            @Override
                            public void onPermissionGranted() {
                                loadData();
                            }

                            @Override
                            public void onPermissionDenied() {
                                ((Activity) mContext).finish();
                            }
                        });
    }

    private void bindView(View v) {
        header_view = (PageHeaderView) v.findViewById(R.id.header_view);
        recycler_category = v.findViewById(R.id.recycler_category);

        text_no_data = (TextView) v.findViewById(R.id.text_no_data);
        text_downloadspeed = (TextView) v.findViewById(R.id.text_downloadspeed);
        btn_change_directory = (TextView) v.findViewById(R.id.btn_change_directory);
        text_recording_path = (TextView) v.findViewById(R.id.text_recording_path);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        frame_recording = v.findViewById(R.id.frame_recording);
        llcover = v.findViewById(R.id.llcover);


        text_name = v.findViewById(R.id.text_name);
        text_size = v.findViewById(R.id.text_size);
        text_time = v.findViewById(R.id.text_time);
        text_status = v.findViewById(R.id.text_status);
        //s2
        llcover2 = v.findViewById(R.id.llcover2);
        text_name2 = v.findViewById(R.id.text_name2);
        text_size2 = v.findViewById(R.id.text_size2);
        text_time2 = v.findViewById(R.id.text_time2);
        text_status2 = v.findViewById(R.id.text_status2);
        frame_recording2 = v.findViewById(R.id.frame_recording2);
        btn_change_directory.setBackground(getSelectorDrawable(mContext.colorModel.getUnselected_btn_color(), mContext.colorModel.getSelected_color()));
        btn_change_directory.setOnClickListener(this);
        header_view.header_btn_back.setBackground(getSelectorDrawableRound(mContext.getResources().getColor(R.color.transparent), mContext.colorModel.getSelected_color()));
        llcover.setBackground(getSelectorDrawableforcover(mContext.colorModel.getUnselected_categoryList(), mContext.colorModel.getSelected_categoryList(), mContext.colorModel.getSelected_color()));
        llcover2.setBackground(getSelectorDrawableforcover(mContext.colorModel.getUnselected_categoryList(), mContext.colorModel.getSelected_categoryList(), mContext.colorModel.getSelected_color()));

        if (isAndroid10_or_Above()) {
            text_recording_path.setMaxLines(1);
            text_recording_path.setSelected(true);
            btn_change_directory.setVisibility(View.GONE);
            btn_change_directory.setText(getResources().getString(R.string.recording_file_manager));
        } else {
            btn_change_directory.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        setHeaderLayout();
//        if (mContext.connectionInfoModel != null) {
        if (RuntimePermissionClass.getInstance(mContext).
                isPermissionsGranted(FileUtils.getPermissionList())) {
            loadData();
        }
//        }
        mTrafficSpeedMeasurer = new TrafficSpeedMeasurer(TrafficSpeedMeasurer.TrafficType.ALL);
        mTrafficSpeedMeasurer.startMeasuring();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        Log.e(TAG, "loadData: called 1");

//        if (mContext.path != null) {
//            Log.e(TAG, "loadData: called 2" + mContext.path);
//            storage_path = mContext.path;
//        } else {

        storage_path =
                MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
        Log.e(TAG, "loadData: called 3" + storage_path);
//        }
        Log.e(TAG, "loadData: called  4:" + storage_path);
        text_recording_path.setText(mContext.
                getString(R.string.recording_save_to) + storage_path);
        // if (isAndroid10_or_Above()) {
        storage_path =
                MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
        text_recording_path.setText(mContext.
                getString(R.string.recording_save_to) + "\n" + storage_path);
        text_recording_path.setMaxLines(2);
        // text_recording_path.setSelected(true);
        // }
        new dataTask().execute();

    }

    private void setHeaderLayout() {
        header_view.header_title.setText(mContext.getString(R.string.str_dashboard_recording));
        header_view.header_helper_view.setVisibility(View.GONE);
        header_view.header_pre_title.setVisibility(View.GONE);
        header_view.header_search.setVisibility(View.GONE);
        header_view.header_menu.setVisibility(View.GONE);
    }

//    private void createfilemodel(String _serviceRunningfilename) {
//        issss = false;
//        String path = MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
//        UtilMethods.LogMethod("Files123_", "Path: " + path);
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        Set<String> currently_recording_list = MyApplication.getInstance().getPrefManager().
//                getCurrentlyRecordingList();
//        if (files != null) {
//            mListnew = new ArrayList<>();
//            for (int i = 0; i < files.length; i++) {
//                if (files[i].getPath().contains(".ts") ||
//                        files[i].getPath().contains(".mp4")) {
//                    RecordingModel model = new RecordingModel();
//                    Log.e(TAG, "loadFiles:createfilemodel _serviceRunningfilename->" + _serviceRunningfilename + " <-  file->" + files[i].getName());
//                    Log.e(TAG, "loadFiles:createfilemodel _serviceRunning.... " + _serviceRunningfilename.contains(files[i].getName()));
//                    if (currently_recording_list.contains(files[i].getName()) || (!_serviceRunningfilename.equals("") && _serviceRunningfilename.contains(files[i].getName()))) {
//                        model.setStatus(Config.RECORDING_RECORDING);
//                    } else {
//                        model.setStatus(Config.RECORDING_COMPLETED);
//                    }
//                    model.setLastModified(files[i].lastModified());
//                    model.setFileName(files[i].getName());
//                    model.setFilePath(files[i].getPath());
//                    model.setFileDownloadDate(UtilMethods.getTimeFromMilli(files[i].lastModified(),
//                            "HH:mm  dd-MMM-yyyy"));
//                    model.setFileSize(UtilMethods.convertByteToMB(files[i].length()));
//                    if (files[i].getName().equals(_serviceRunningfilename))
//                        mListnew.add(model);
//                }
//            }
//        }
//        Log.e(TAG, "createfilemodel: mListnew:" + mListnew.size());
//    }

    private void Addtoarraylist() {
        mList.addAll(mListnewfortoday);
        mList.addAll(mListnewfortommorow);
        mList.addAll(mListnewforfuture);
        mList.addAll(mListnewforalreadyrecorded);
        try {
            Collections.sort(mList, new Comparator<BaseModel>() {
                @Override
                public int compare(BaseModel b0, BaseModel b1) {
                    return (int) (((RecordingModel) b1).getLastModified() -
                            ((RecordingModel) b0).getLastModified());

                }
            });
        } catch (Exception e) {

        }
    }

    private void splitdatabydate() {
        if (mList != null) {
            try {
                if (mListnewfortoday != null && !mListnewfortoday.isEmpty()) {
                    mListnewfortoday.clear();
                }
                if (mListnewfortommorow != null && !mListnewfortommorow.isEmpty()) {
                    mListnewfortommorow.clear();
                }
                if (mListnewforfuture != null && !mListnewforfuture.isEmpty()) {
                    mListnewforfuture.clear();
                }
                if (mListnewforalreadyrecorded != null && !mListnewforalreadyrecorded.isEmpty()) {
                    mListnewforalreadyrecorded.clear();
                }

                mListnewfortoday = new ArrayList<>();
                mListnewfortommorow = new ArrayList<>();
                mListnewforfuture = new ArrayList<>();

                //  Log.e(TAG, "splitdatabydate: mList:" + mList.size());
                for (BaseModel data : mList) {
                    RecordingScheduleModel data1 = (RecordingScheduleModel) data;
                    String date = simpleDateFormat.format(data1.getStartTime());
                    //    Log.e(TAG, "splitdatabydate: date:" + date);
                    Calendar calendartoday = Calendar.getInstance();
                    Calendar calendartomorrow = Calendar.getInstance();
                    calendartomorrow.add(Calendar.DATE, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    sdf.setTimeZone(TimeZone.getTimeZone(MyApplication.getInstance().getPrefManager().getTimeZone()));
                    sdf.format(calendartoday.getTime());
                    String todaydate = sdf.format(calendartoday.getTime());
                    String tommorowdate = sdf.format(calendartomorrow.getTime());

                    //   Log.e(TAG, "splitdatabydate: today date:" + todaydate);
                    //   Log.e(TAG, "splitdatabydate: tommorowdate date:" + tommorowdate);
                    if (todaydate.equals(date)) {
                        //     Log.e(TAG, "splitdatabydate: matched today");
                        mListnewfortoday.add(data);

                    } else if (tommorowdate.equals(date)) {
                        mListnewfortommorow.add(data);
                        //    Log.e(TAG, "splitdatabydate: matched tomorrow");
                    } else {
                        mListnewforfuture.add(data);
                        //    Log.e(TAG, "splitdatabydate: not matched  ");
                    }

                }
            } catch (Exception e) {
                Log.e(TAG, "splitdatabydate: catch:" + e.getMessage());
            }
        }
    }

    private void Setad() {
        baseFakeModelArrayList = new ArrayList<>();
        baseFakeModelArrayList.add(new BaseFakeModel(100));
        baseFakeModelArrayList.add(new BaseFakeModel(101));
        if (mListnewforfuture != null && !mListnewforfuture.isEmpty())
            baseFakeModelArrayList.add(new BaseFakeModel(102));
        baseFakeModelArrayList.add(new BaseFakeModel(103));
        // Log.e(TAG, "Setad: baseFakeModelArrayList: " + baseFakeModelArrayList.size());
        recycler_category.setVisibility(View.VISIBLE);
        text_no_data.setVisibility(View.GONE);


        dashBoardMultiAdapter = new RecordingMultiAdapter(baseFakeModelArrayList, getContext(), mListnewfortoday, mListnewfortommorow, mListnewforfuture, mListnewforalreadyrecorded, mContext.colorModel, new RecordingMultiAdapter.adapternotify() {
            @Override
            public void notifydatachanged() {
                //sendtorecordingagainalldata();
                loadData();
            }
        });

        recycler_category.setLayoutManager(layoutManager);
        recycler_category.setAdapter(dashBoardMultiAdapter);
        recycler_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //if (newState == RecyclerView.SCROLL_STATE_IDLE){
                int position = getCurrentItem(recyclerView);
                // onPageChanged(position);
                // Log.e(TAG, "onScrollStateChanged: pos:" + position);
                //}
            }
        });
        // Log.e(TAG, "Setad: getCurrentItem:" + getCurrentItem(recycler_category));
        layoutManager.scrollToPositionWithOffset(0, 0);
        //recycler_category.scrollToPosition(0);
        dashBoardMultiAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
//    private void sendtorecordingagainalldata() {
//        try {
//            new AsyncTask<Void, String, Void>() {
//
//                @Override
//                protected Void doInBackground(Void... objects) {
//                    mListfordeletion = new ArrayList<>();
//                    if (DEBUG)
//                        Log.e(TAG, "doInBackground: connectionInfoModel.getUid():" + mContext.connectionInfoModel.getUid());
//                    List<RecordingScheduleModel> scheduleList =
//                            DatabaseRoom.with(mContext).
//                                    getAllScheduleRecording(mContext.connectionInfoModel.getUid());
//                    for (int i = 0; i < scheduleList.size(); i++) {
//                        RecordingScheduleModel model = scheduleList.get(i);
//                        if (model.getStartTime() < System.currentTimeMillis()) {
//                            deleteScheduleFromDatabase(model.getUid());
//                        } else {
//                            mListfordeletion.add(model);
//                        }
//
//                    }
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//                    if (mList != null && mList.size() > 0) {
//                        if (DEBUG) Log.e(TAG, "doInBackground: task  found");
//                        sentScheduleRecording(mContext, mListfordeletion);
//                    } else {
//                        if (DEBUG) Log.e(TAG, "doInBackground: task not found");
//                    }
//                }
//            }.execute();
//
//
//        } catch (Exception e) {
//            if (DEBUG)
//                Log.e(TAG, "checkanyscheduledtaskpendingornot: catch" + e.getMessage());
//        }
//    }

    private void sentScheduleRecording(Context mContext, ArrayList<RecordingScheduleModel> mList) {
        Log.e(TAG, "abc called : sentScheduleRecording: ");
        if (appInstalledOrNot(mContext, PKGFORRECORDING)) {
            List arrayList = new ArrayList();
            PackageManager packageManager = mContext.getPackageManager();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            List queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
            for (int i = 0; i < queryIntentActivities.size(); i++) {
                ResolveInfo resolveInfo = (ResolveInfo) queryIntentActivities.get(i);
                String str3 = resolveInfo.activityInfo.packageName;
                Log.e("Package Name", str3);
                if (str3.contains(PKGFORRECORDING)) {
                    Intent intent2 = new Intent();
                    intent2.setComponent(new ComponentName(str3, resolveInfo.activityInfo.name));
                    intent2.setAction("android.intent.action.SEND");
                    intent2.setType("*/*");
                    //send model and insert then schedule with alarm
                    Gson gson = new Gson();
                    String mListforrecordinglist = gson.toJson(mList);
                    Bundle todoBundle = new Bundle();
                    todoBundle.putString("recordingScheduleModellist", mListforrecordinglist);
                    todoBundle.putString("reqtype", "deleteallandinsertagainandsc");
                    intent2.putExtra("deletedataandinsertagain", todoBundle);
                    intent2.putExtra("pkgname", mContext.getPackageName());
                    intent2.putExtra("path", MyApplication.getInstance().getPrefManager().
                            getRecordingStoragePath());
                    intent2.setPackage(str3);
                    arrayList.add(intent2);
                }
                if (arrayList.isEmpty()) {
                    System.out.println("Do not Have Intent");
                } else {
                    Intent createChooser = Intent.createChooser((Intent) arrayList.remove(0), "Choose app to share");
                    createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[0]));
                    mContext.startActivity(createChooser);
                }
            }

        } else {
            Toast.makeText(mContext, "Recording Plugin Not Found", Toast.LENGTH_LONG).show();
        }
    }

    private int getCurrentItem(RecyclerView recyclerView) {
        return ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    private void openPopupforcurrentrecording(View longPressedView) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.layout_popup, null);
        RecyclerView recycler_popup =
                (RecyclerView) view.findViewById(R.id.recycler_popup);
        recycler_popup.setLayoutManager(new LinearLayoutManager(mContext));
        popupWindow = new PopupWindow(view, (int) mContext.getResources().
                getDimension(R.dimen.popup_dialog_width),
                FrameLayout.LayoutParams.WRAP_CONTENT, true);
//        final BaseModel baseModel = mList.get(adapterPosition);
        final ArrayList<String> menuList = new ArrayList<>();

        String name = "";

        //menuList.add(mContext.getString(R.string.longpressed_popup_play));
        // menuList.add(mContext.getString(R.string.str_delete));
        //   name = ((RecordingModel) baseModel).getFileName();
//        String status = ((RecordingModel) baseModel).getStatus();
//        if (status != null && status.equals(Config.RECORDING_RECORDING)) {
        menuList.add(mContext.getString(R.string.popup_stop_recording));
//        }

        menuList.add(mContext.getString(R.string.popup_close));
        final String finalName = name;
        PopupAdapter popupAdapter = new PopupAdapter(mContext, menuList,
                new PopupAdapter.BluetoothClickInterface() {
                    @Override
                    public void onClick(PopupAdapter.PopupViewHolder holder, int position) {
                        String clickText = menuList.get(position);
                        if (clickText.equals(mContext.getString(R.string.longpressed_popup_play))) {
//                            playRecording((RecordingModel) baseModel);
                        } else if (clickText.equals(mContext.getString(R.string.str_delete))) {
                            //   deleteAlertDialog(finalName, mList, adapterPosition);
                        } else if (clickText.equals(mContext.getString(R.string.popup_stop_recording))) {
//                            Set<String> currently_recording_list = MyApplication.getInstance().
//                                    getPrefManager().
//                                    getCurrentlyRecordingList();
                            //currently_recording_list.remove(((RecordingModel) baseModel).getFileName());
//                            MyApplication.getInstance().getPrefManager().
//                                    setCurrentlyRecordingList(currently_recording_list);

                            // RecordingService.shouldContinue = false;
                            sendbroadcasttorecordingplugin();
                            EventBus.getDefault().post(new Stoprecording1());
                            Toast.makeText(mContext, "Recording has stopped.",
                                    Toast.LENGTH_LONG).show();
                            _serviceRunningfilename = "";
                            if (dashBoardMultiAdapter != null) {
                                dashBoardMultiAdapter.notifyDataSetChanged();
                            }
                            loadData();
                        }
                        popupWindow.dismiss();

                    }
                });
        recycler_popup.setAdapter(popupAdapter);
        if (popupWindow != null && longPressedView != null) {
            popupWindow.showAsDropDown(longPressedView,
                    longPressedView.getWidth() / 2, -longPressedView.getHeight());
        }

    }
//

    private void openPopupforcurrentrecording2(View longPressedView) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.layout_popup, null);
        RecyclerView recycler_popup =
                (RecyclerView) view.findViewById(R.id.recycler_popup);
        recycler_popup.setLayoutManager(new LinearLayoutManager(mContext));
        popupWindow = new PopupWindow(view, (int) mContext.getResources().
                getDimension(R.dimen.popup_dialog_width),
                FrameLayout.LayoutParams.WRAP_CONTENT, true);
//        final BaseModel baseModel = mList.get(adapterPosition);
        final ArrayList<String> menuList = new ArrayList<>();

        String name = "";

        //menuList.add(mContext.getString(R.string.longpressed_popup_play));
        // menuList.add(mContext.getString(R.string.str_delete));
        //   name = ((RecordingModel) baseModel).getFileName();
//        String status = ((RecordingModel) baseModel).getStatus();
//        if (status != null && status.equals(Config.RECORDING_RECORDING)) {
        menuList.add(mContext.getString(R.string.popup_stop_recording));
//        }

        menuList.add(mContext.getString(R.string.popup_close));
        final String finalName = name;
        PopupAdapter popupAdapter = new PopupAdapter(mContext, menuList,
                new PopupAdapter.BluetoothClickInterface() {
                    @Override
                    public void onClick(PopupAdapter.PopupViewHolder holder, int position) {
                        String clickText = menuList.get(position);
                        if (clickText.equals(mContext.getString(R.string.longpressed_popup_play))) {
//                            playRecording((RecordingModel) baseModel);
                        } else if (clickText.equals(mContext.getString(R.string.str_delete))) {
                            //   deleteAlertDialog(finalName, mList, adapterPosition);
                        } else if (clickText.equals(mContext.getString(R.string.popup_stop_recording))) {

                            sendbroadcasttorecordingplugin2();
                            EventBus.getDefault().post(new Stoprecording2());
                            Toast.makeText(mContext, "Recording has stopped.",
                                    Toast.LENGTH_LONG).show();
                            _serviceRunningfilename2 = "";
                            if (dashBoardMultiAdapter != null) {
                                dashBoardMultiAdapter.notifyDataSetChanged();
                            }
                            loadData();
                        }
                        popupWindow.dismiss();

                    }
                });
        recycler_popup.setAdapter(popupAdapter);
        if (popupWindow != null && longPressedView != null) {
            popupWindow.showAsDropDown(longPressedView,
                    longPressedView.getWidth() / 2, -longPressedView.getHeight());
        }

    }

    private void sendbroadcasttorecordingplugin() {
        Intent intent = new Intent();
        intent.setAction("stoprecordingintent");
        intent.putExtra("task", "recording");
        intent.putExtra("what", "stop");
        //intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        // intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        //  intent.setComponent(new ComponentName(PKGFORRECORDING, "com.xunison.recordingplugin.receiver.MyBroadcastReceiver"));
        mContext.sendBroadcast(intent);
    }

    private void sendbroadcasttorecordingplugin2() {
        Intent intent = new Intent();
        intent.setAction("stoprecordingintent2");
        intent.putExtra("task", "recording");
        intent.putExtra("what", "stop");
        //intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        // intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        //  intent.setComponent(new ComponentName(PKGFORRECORDING, "com.xunison.recordingplugin.receiver.MyBroadcastReceiver"));
        mContext.sendBroadcast(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteScheduleFromDatabase(final long uid) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseRoom.with(mContext).deleteScheduleRecording(uid);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class dataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.requestFocus();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mList = new ArrayList<>();
            scheduleRecordingData();// get list here for today,tomorrow, future
            splitdatabydate();//split data by given list
            loadFiles();// load already recorded files
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            //   setRecycler(mList);
            Setad();

        }


        private void loadFiles() {
            mListnewforalreadyrecorded = new ArrayList<>();
//            String path = MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
            UtilMethods.LogMethod("Files123_", "Path: " + storage_path);

            File directory = new File(storage_path);
            File[] files = directory.listFiles();
            if (files != null) {
                Arrays.sort(files, (Comparator) (o1, o2) -> {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                });
                UtilMethods.LogMethod("Files1234_", "Size: " + files.length);
                Set<String> currently_recording_list = MyApplication.getInstance().getPrefManager().
                        getCurrentlyRecordingList();
                UtilMethods.LogMethod("currently_recording_list_", String.valueOf(Arrays.toString(new Set[]{currently_recording_list})));
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getPath().contains(".ts") ||
                            files[i].getPath().contains(".mp4")) {
                        RecordingModel model = new RecordingModel();
                        //    Log.e(TAG, "loadFiles: _serviceRunningfilename->" + _serviceRunningfilename + " <-  file->" + files[i].getName());
                        //  Log.e(TAG, "loadFiles: _serviceRunning.... " + _serviceRunningfilename.contains(files[i].getName()));
                        model.setStatus(Config.RECORDING_COMPLETED);
                        model.setLastModified(files[i].lastModified());
                        model.setFileName(files[i].getName());
                        model.setFilePath(files[i].getPath());
                        model.setFileDownloadDate(UtilMethods.getTimeFromMilli(files[i].lastModified(),
                                "HH:mm  dd-MMM-yyyy"));
                        model.setFileSize(UtilMethods.convertByteToMB(files[i].length()));
                        Log.e(TAG, "loadFiles:1 " + files[i].getName() + "<--hello-->" + _serviceRunningfilename + " -- Storage path-->" + storage_path);
                        Log.e(TAG, "loadFiles:2 " + files[i].getName() + "<--hello-->" + _serviceRunningfilename2);
                        if (!files[i].getName().equals(_serviceRunningfilename) || !files[i].getName().equals(_serviceRunningfilename2)) {
                            mList.add(model);
                            mListnewforalreadyrecorded.add(model);
                        }
                    }
                }
                Addtoarraylist();
            } else {
                Log.e(TAG, "loadFiles: files is null");
            }


        }

        private void scheduleRecordingData() {
            List<RecordingScheduleModel> scheduleList = (mContext.pkgname != null && !mContext.pkgname.equals("")) ?
                    DatabaseRoom.with(mContext).
                            getAllScheduleRecordingwithpkgname(-1, mContext.pkgname) : DatabaseRoom.with(mContext).
                    getAllScheduleRecording(-1, mContext.pkgname);
            for (int i = 0; i < scheduleList.size(); i++) {
                RecordingScheduleModel model = scheduleList.get(i);
                if (model.getStartTime() < System.currentTimeMillis()) {
                    deleteScheduleFromDatabase(model.getUid());
                } else {
                    mList.add(model);
                }

            }


        }

    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Log.e(TAG, "onReceive: getAction:" + arg1.getAction());

            if (arg1 != null) {
                String downloadspeed = arg1.getStringExtra(_downloadspeed);
                String downloadstatus = arg1.getStringExtra(_downloadstatus);
                String recordingname = arg1.getStringExtra(RECORDING_FILENAME);
                final String rtime = arg1.getStringExtra(_REMAININGTIIMESTR);
                final long remaningtime = arg1.getLongExtra(_REMAININGTIIME, System.currentTimeMillis());

                _serviceRunningfilename = recordingname;
                Log.e(TAG, "onReceive: downloadspeed not if:" + downloadspeed);
                Log.e(TAG, "onReceive: downloadstatus :" + downloadstatus);
                Log.e(TAG, "onReceive: _serviceRunningfilename :" + _serviceRunningfilename);
                if (downloadspeed != null && !downloadspeed.equals("") && downloadstatus != null && !downloadstatus.equals("") && downloadstatus.equals("running")) {
                    Log.e(TAG, "onReceive: downloadspeed in if:" + downloadspeed);
                    if (issss) {
                        issss = false;
                        if (dashBoardMultiAdapter != null) {
                            dashBoardMultiAdapter.notifyDataSetChanged();
                        }
                        loadData();
                    }
                    //createfilemodel(_serviceRunningfilename);
//                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            text_time.setText("Recording Ends on:" + rtime);
                        }
                    });

                    frame_recording.setVisibility(View.VISIBLE);
                    text_name.setText(_serviceRunningfilename);
                    text_status.setText("Recording");
                    text_size.setText("Recording File:" + downloadspeed + " Mbps");


                    frame_recording.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPopupforcurrentrecording(v);
                        }
                    });

                } else if (downloadstatus != null && !downloadstatus.equals("") && downloadstatus.equals("completed")) {
                    _serviceRunningfilename = "";
                    issss = true;
                    Log.e(TAG, "onReceive: downloadspeed else if:" + downloadspeed);
                    if (RuntimePermissionClass.getInstance(mContext).
                            isPermissionsGranted(FileUtils.getPermissionList())) {
                        if (dashBoardMultiAdapter != null) {
                            dashBoardMultiAdapter.notifyDataSetChanged();
                        }
                        loadData();
                    }
                    frame_recording.setVisibility(View.GONE);
                } else {
                    frame_recording.setVisibility(View.GONE);
                }

            }

        }

    }

    private class MyReceiverfors2 extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Log.e(TAG, "onReceive: getAction:" + arg1.getAction());

            if (arg1 != null) {
                String downloadspeed = arg1.getStringExtra(_downloadspeed);
                String downloadstatus = arg1.getStringExtra(_downloadstatus);
                String recordingname = arg1.getStringExtra(RECORDING_FILENAME);
                final String rtime = arg1.getStringExtra(_REMAININGTIIMESTR);
                final long remaningtime = arg1.getLongExtra(_REMAININGTIIME, System.currentTimeMillis());

                _serviceRunningfilename2 = recordingname;
                Log.e(TAG, "onReceive: downloadspeed not if:" + downloadspeed);
                Log.e(TAG, "onReceive: downloadstatus :" + downloadstatus);
                Log.e(TAG, "onReceive: _serviceRunningfilename :" + _serviceRunningfilename2);
                if (downloadspeed != null && !downloadspeed.equals("") && downloadstatus != null && !downloadstatus.equals("") && downloadstatus.equals("running")) {
                    Log.e(TAG, "onReceive: downloadspeed in if:" + downloadspeed);
                    if (issss2) {
                        issss2 = false;
                        if (dashBoardMultiAdapter != null) {
                            dashBoardMultiAdapter.notifyDataSetChanged();
                        }
                        loadData();
                    }
                    //createfilemodel(_serviceRunningfilename);
//                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            text_time2.setText("Recording Ends on:" + rtime);
                        }
                    });

                    frame_recording2.setVisibility(View.VISIBLE);
                    text_name2.setText(_serviceRunningfilename2);
                    text_status2.setText("Recording");
                    text_size2.setText("Recording File:" + downloadspeed + " Mbps");


                    frame_recording2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPopupforcurrentrecording2(v);
                        }
                    });

                } else if (downloadstatus != null && !downloadstatus.equals("") && downloadstatus.equals("completed")) {
                    _serviceRunningfilename2 = "";
                    issss2 = true;
                    Log.e(TAG, "onReceive: downloadspeed else if:" + downloadspeed);
                    if (RuntimePermissionClass.getInstance(mContext).
                            isPermissionsGranted(FileUtils.getPermissionList())) {
                        if (dashBoardMultiAdapter != null) {
                            dashBoardMultiAdapter.notifyDataSetChanged();
                        }
                        loadData();
                    }
                    frame_recording2.setVisibility(View.GONE);
                } else {
                    frame_recording2.setVisibility(View.GONE);
                }

            }

        }

    }

}
