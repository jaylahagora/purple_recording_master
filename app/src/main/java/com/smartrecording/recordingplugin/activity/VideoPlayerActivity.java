package com.smartrecording.recordingplugin.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.fof.android.vlcplayer.VLCPlayer;
import com.purple.iptv.player.utils.UtilMethods;
import com.xunison.recordingplugin.BuildConfig;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.model.PlayerModel;
import com.smartrecording.recordingplugin.model.RemoteConfigModel;
import com.xunison.recordingplugin.R;

import java.util.HashMap;
import java.util.Map;

public class VideoPlayerActivity extends AppCompatActivity {

    private final String TAG = "VideoPlayerActivity123_";
    private VideoPlayerActivity mContext;
    private VLCPlayer vlc_player;
    private PlayerModel playerModel;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        keepScreenon();
        setwakeLock();
        mContext = VideoPlayerActivity.this;
        bindViews();
        bindData();
    }

    private void setwakeLock() {
        try {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My :Tag");
                wakeLock.setReferenceCounted(false);
                wakeLock.acquire(60000);
            }
        } catch (Exception e) {
            Log.e(TAG, "setwakeLock: catch:" + e.getMessage());
        }


    }


    private void keepScreenon() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                this.setTurnScreenOn(true);
                final Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                final Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                       );
            }

        } catch (Exception e) {
            Log.e(TAG, "keepScreenon: catch:" + e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vlc_player != null) {
            if (vlc_player.isPrepared) {
                vlc_player.start();
            } /*else if (playerModel != null) {
                if (playerModel.getMedia_url() != null) {
                    vlc_player.setSource(Uri.parse(playerModel.getMedia_url()), new HashMap<>());
                }
            }*/
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (BuildConfig.DEBUG) Log.e(TAG, "onKeyUp: keyCode" + keyCode);
        if (BuildConfig.DEBUG) Log.e(TAG, "onKeyUp: event" + event.getKeyCode());
        if (BuildConfig.DEBUG) Log.e(TAG, "onKeyUp: event" + event.getAction());

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                //its fwd
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    vlc_player.moveForward();
                    break;
                //play pause
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    vlc_player.playpauseonclick();
                    break;
                //its back/ prev
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    vlc_player.moveBackward();
                default:
                    break;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (BuildConfig.DEBUG) Log.e(TAG, "onKeyDown: keyCode" + keyCode);
        if (BuildConfig.DEBUG) Log.e(TAG, "onKeyDown: event" + event.getKeyCode());
        if (BuildConfig.DEBUG) Log.e(TAG, "onKeyDown: event" + event.getAction());
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (vlc_player != null) {
            if (vlc_player.isPrepared) {
                vlc_player.pause();
            } else {
                vlc_player.stop();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (vlc_player != null) {
            vlc_player.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (vlc_player != null) {
            if (vlc_player.isControllerShown()) {
                vlc_player.hideControl();
                return;
            }
        }
        finish();
    }

    private void bindViews() {
        vlc_player = (VLCPlayer) findViewById(R.id.vlc_player);
    }

    private void bindData() {
        playerModel = getIntent().getParcelableExtra("player_model");
        vlc_player.initPlayer(vlc_player, null, false);
        vlc_player.setLiveContent(false);
        if (playerModel != null) {
//            OnlineUserModel onlineUserModel = MyApplication.getInstance().getPrefManager()
//                    .getOnlineUser();
            RemoteConfigModel remoteConfigModel = MyApplication.getInstance().getPrefManager().
                    getRemoteConfig();
            Map<String, String> header = new HashMap<>();
            if (!TextUtils.isEmpty(playerModel.getUser_agent())) {
                header.put("User-Agent", playerModel.getUser_agent().trim());
            } else if (remoteConfigModel != null &&
                    remoteConfigModel.getOnlineHeaderValue() != null) {
                header.put("User-Agent", remoteConfigModel.getOnlineHeaderValue());
            }
            UtilMethods.LogMethod(TAG + "_useragent", String.valueOf(header));
            UtilMethods.LogMethod(TAG + "_url", String.valueOf(playerModel.getMedia_url()));
            if (playerModel.getMedia_url() != null) {
                vlc_player.setSource(Uri.parse(playerModel.getMedia_url()), header,null);
                vlc_player.getMediaNameTextView().setText(playerModel.getMedia_name());

            }
        }

    }
}
