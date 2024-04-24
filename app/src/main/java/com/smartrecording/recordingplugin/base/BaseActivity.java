package com.smartrecording.recordingplugin.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fof.android.vlcplayer.utils.UtilMethods;
import com.purple.iptv.player.views.CustomBaseView;
import com.xunison.recordingplugin.R;
import com.smartrecording.recordingplugin.app.MyApplication;


public abstract class BaseActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private BaseActivity mContext;
    public CustomBaseView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        view = new CustomBaseView(mContext);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(view.getView());
      //  Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(mContext));

//        ANRWatchDog anrWatchDog = new ANRWatchDog(20000);
//        anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
//            @Override
//            public void onAppNotResponding(ANRError error) {
//                UtilMethods.LogMethod("anr123_error", String.valueOf(error));
//                UtilMethods.LogMethod("anr123_", "ANRWatchDog");
//                CommonMethods.onCrash(mContext);
//            }
//        });
//        anrWatchDog.start();
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFrameLayout = (FrameLayout) findViewById(R.id.sub_layout);
        com.purple.iptv.player.utils.UtilMethods.LogMethod("view1234_frame", String.valueOf(mFrameLayout));
        assert inflater != null;
        View v = inflater.inflate(layoutResID, null);
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UtilMethods.LogMethod("app1234_treeUri", String.valueOf("onActivityResult"));
        if (requestCode == 3) {
            if (data != null) {
                Uri treeUri = data.getData();
                if (treeUri != null) {
                    UtilMethods.LogMethod("app1234_treeUri", String.valueOf(treeUri.toString()));
                    MyApplication.getInstance().getPrefManager().
                            setExternalStorageUri(treeUri.toString());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        UtilMethods.LogMethod("adsfs123_showAds", String.valueOf("onBackPressed"));


        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
