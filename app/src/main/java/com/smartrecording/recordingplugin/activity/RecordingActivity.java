package com.smartrecording.recordingplugin.activity;

import static com.purple.iptv.player.utils.UtilConstant.PKGFORRECORDING;
import static com.smartrecording.recordingplugin.activity.MainActivity.checkExternalStoragewritebleornot;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.base.BaseActivity;
import com.smartrecording.recordingplugin.fragment.RecordingFragment;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.receiver.MyBroadcastReceiver;
import com.smartrecording.recordingplugin.utils.FileUtils;
import com.xunison.recordingplugin.R;


public class RecordingActivity extends BaseActivity {
//    static {
//        System.loadLibrary("ijkjni");
//    }
    private static final String TAG = "RecordingActivity";
    private FragmentManager manager;
    public String pkgname = "";
    //    public ConnectionInfoModel connectionInfoModel;
    private Fragment currentFragment;
    private MyBroadcastReceiver MyReceiver;
    public String path;
    public boolean ispathotgselected = false;

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 22) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onRequestPermissionsResult: granted");
                // Permission granted.
                checkExternalStoragewritebleornot(RecordingActivity.this);
                startActivity(new Intent(this, RecordingActivity.class));
                finish();
            } else {
                Log.e(TAG, "onRequestPermissionsResult:not granted");
                // User refused to grant permission.
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getintentdata();
        Log.e(TAG, "onCreate: ");
        // Analytics.trackEvent("Recording");
        setContentView(R.layout.activity_recording);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, FileUtils.getPermission()) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, FileUtils.getPermissionList(),
                    22);

            return;
        }
//        String file_name = "aaa";
//        String external_url = MyApplication.getInstance().getPrefManager().
//                getExternalStorageUri();
//        if (external_url != null) {
//            DocumentFile document = DocumentFile.fromTreeUri(this,
//                    Uri.parse(external_url));
//            com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_document", String.valueOf(document));
//            com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_external_url", String.valueOf(external_url));
//            if (document != null) {
//                try {
//                    DocumentFile apkFile = document.createFile("video/MP2T",
//                            file_name);
//                    com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_apkFile", String.valueOf(apkFile));
//                    if (apkFile != null) {
//                        Log.e(TAG, "onRequestPermissionsResult: file created");
////                            outputStream = mContext.getContentResolver().
////                                    openOutputStream(apkFile.getUri());
//                    } else {
//                        Log.e(TAG, "onRequestPermissionsResult: file not created");
//                        Log.e(TAG, "backgroundTask: apkfile is null");
//                        //    Toast.makeText(mContext, "Something went wrong please change directory", Toast.LENGTH_SHORT).show();
////                            response_result = 0;
////                            sendBroadcasttootherapp(0, pkgname, "completed", file_name);
////                            return;
//
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG, "onRequestPermissionsResult: catch:" + e.getMessage());
//                    com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_ee1111", String.valueOf(e));
////                        sendBroadcasttootherapp(0, pkgname, "completed", file_name);
//                    //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            checkExternalStoragewritebleornot(RecordingActivity.this);
        IntentFilter intentFilter = new IntentFilter(PKGFORRECORDING);
        MyReceiver = new MyBroadcastReceiver();
        if (intentFilter != null) {
            registerReceiver(MyReceiver, intentFilter);
        }
        bindData();

        //  getUSBProblematic(this);

    }


    private void getintentdata() {
        try {
            Intent intent = getIntent();
            ispathotgselected = intent.getBooleanExtra("ispathotgselected", false);
            Log.e(TAG, "getintentdata: ispathotgselected:" + ispathotgselected);
        } catch (Exception e) {
            Log.e(TAG, "getintentdata: catch:" + e.getMessage());
        }
    }


    private void logShow(String s) {
        Log.e(TAG, "test: " + s);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (currentFragment instanceof RecordingFragment &&
                ((RecordingFragment) currentFragment).onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public ColorModel colorModel;

    private void bindData() {
        try {


            Gson gson = new Gson();
            manager = getSupportFragmentManager();
            pkgname = getIntent().getStringExtra("pkgname");
            path = getIntent().getStringExtra("path");
            colorModel = gson.fromJson(getIntent().getStringExtra("colorModelforrecording"), ColorModel.class);
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
            MyApplication.getInstance().getPrefManager().setColormodel(gson.toJson(colorModel));

//        connectionInfoModel = (ConnectionInfoModel) getIntent().
//                getParcelableExtra("connectionInfoModel");
//        if(connectionInfoModel!=null){
            setFragments();
//        }
        } catch (Exception e) {
            Log.e(TAG, "bindData: catch:" + e.getMessage());
            finish();
        }
    }

    public void setFragments() {
        currentFragment = RecordingFragment.newInstance("", "");
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, currentFragment,
                currentFragment.getClass().getName());
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyReceiver != null)
            unregisterReceiver(MyReceiver);
    }
}
