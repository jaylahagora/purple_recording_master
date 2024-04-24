package com.smartrecording.recordingplugin.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.purple.iptv.player.models.ConnectionInfoModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;

import java.util.List;

public class DatabaseRoom {
    private static final String TAG = DatabaseRoom.class.getName();
    private static DatabaseRoom instance;
    public static String[] extra_category = {"All", "Favourites"};
    AppDatabase db;

    private DatabaseRoom(Context mContext) {
        db = AppDatabase.getAppDatabase(mContext);
    }

    public static DatabaseRoom with(Context context) {
        instance = new DatabaseRoom(context);
        return instance;
    }


    public void addConnection(ConnectionInfoModel model) {
        db.ConnectionInfoDao().updateConnection(model);
    }

    public long getConnectionId(String friendly_name, String domain_url) {
        return db.ConnectionInfoDao().getConnectionId(friendly_name, domain_url);
    }

    public void deleteConnection(String friendly_name, String domain_url) {
        db.ConnectionInfoDao().deleteConnection(friendly_name, domain_url);
    }

    public ConnectionInfoModel getLastLoginPlaylist() {
        return db.ConnectionInfoDao().getLastLoginPlaylist();
    }

    public void updateLastLoginPlaylist(ConnectionInfoModel connectionInfoModel) {
        db.ConnectionInfoDao().updateLastLoginPlaylist(connectionInfoModel);
    }

    @SuppressLint("StaticFieldLeak")
    public void resetLastLoginPlayList() {
        db.ConnectionInfoDao().resetLastLoginPlaylist();
    }

    @SuppressLint("StaticFieldLeak")
    public void resetLastLoginPlayListNew(String domain_url) {
        db.ConnectionInfoDao().resetLastLoginPlaylistNew(domain_url);
    }

    public void updateLastUpdateTime(long time, long id) {
        // db.ConnectionInfoDao().resetLastUpdateTime(id);
        db.ConnectionInfoDao().updateLastUpdateTime(time, id);
    }

    public void updateLastVodUpdateTime(long time, long id) {
        //  db.ConnectionInfoDao().resetLastVodUpdateTime(id);
        db.ConnectionInfoDao().updateLastVodUpdateTime(time, id);
    }

    public void updateLastSeriesUpdateTime(long time, long id) {
        //  db.ConnectionInfoDao().resetLastSeriesUpdateTime(id);
        db.ConnectionInfoDao().updateLastSeriesUpdateTime(time, id);
    }

    @SuppressLint("StaticFieldLeak")
    public ConnectionInfoModel getLastPlayedConnection() {
        return db.ConnectionInfoDao().getLastPlayedConnection();
    }

    public List<ConnectionInfoModel> getConnection(String type) {
        return db.ConnectionInfoDao().getConnections(type);
    }

    public List<ConnectionInfoModel> getAllConnections() {
        return db.ConnectionInfoDao().getAllConnections();
    }

    public void updateConnection(ConnectionInfoModel model) {
        db.ConnectionInfoDao().update(model);
    }

    public void deleteAllConnection() {
        db.ConnectionInfoDao().deleteAll();
    }


    /*
     *
     * Schedule Recording
     *
     * */

    public void insertScheduleRecording(RecordingScheduleModel recordingScheduleModel) {
        db.recordingScheduleDao().insertScheduleRecording(recordingScheduleModel);
    }


    public long getScheduleRecordingUid(long connectionId, String url) {
        return db.recordingScheduleDao().getScheduleRecordingUid(connectionId, url);
    }

    public int getScheduleRecordingUidSpedific(String url, long startTime, long endTime, String getRecordpath, String showname) {
        return db.recordingScheduleDao().getScheduleRecordingUidSpedific(url, startTime, endTime, getRecordpath, showname);
    }

    public List<RecordingScheduleModel> getAllScheduleRecordingwithpkgname(long connectionId, String pkgname) {
        return db.recordingScheduleDao().getAllScheduleRecordingwithpkgname(pkgname);
    }

    public List<RecordingScheduleModel> getAllScheduleRecording(long connectionId, String pkgname) {
        return db.recordingScheduleDao().getAllScheduleRecording();
    }

    public void updateScheduleRecordingStatus(long connectionId, long uid,
                                              String status) {
        db.recordingScheduleDao().updateScheduleRecordingStatus(connectionId, uid, status);
    }

    public boolean isChannelInScheduleRecording(long connectionId, String channel_name) {
        String name = db.recordingScheduleDao().
                getScheduleRecordingByName(connectionId, channel_name);
        if (name != null && !TextUtils.isEmpty(name)) {
            return true;
        }
        return false;
    }

    public boolean isShowInScheduleRecording(long connectionId, String channel_name,
                                             String show_name, long startTime) {
        String name = db.recordingScheduleDao().
                getScheduleRecordingShow(connectionId, channel_name, show_name, startTime);
        if (name != null && !TextUtils.isEmpty(name)) {
            return true;
        }
        return false;
    }


    public RecordingScheduleModel getScheduleRecordingByShowName(long connectionId, String channel_name,
                                                                 String show_name, long startTime) {
        return db.recordingScheduleDao().getScheduleRecordingByShowName(connectionId, channel_name,
                show_name, startTime);
    }

    public void deleteScheduleRecording(long uid) {
        db.recordingScheduleDao().deleteScheduleRecording(uid);
    }

    public void deleteAllRecording() {
        db.recordingScheduleDao().deleteAll();
    }

    /*
     *
     * APP DESIGN DAO
     *
     * */


}

