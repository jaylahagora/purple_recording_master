package com.smartrecording.recordingplugin.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import com.fof.android.vlcplayer.utils.UtilMethods;
import com.purple.iptv.player.models.ConnectionInfoModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;

import java.util.List;

public class Daos {


    @Dao
    public static abstract class ConnectionInfoDao {

        @Transaction
        void updateConnection(ConnectionInfoModel connectionInfoModel) {
            // deleteConnection(connectionInfoModel.getFriendly_name(), connectionInfoModel.getDomain_url());
            ConnectionInfoModel checkModel = isConnectionAvailable(connectionInfoModel.
                    getFriendly_name(), connectionInfoModel.getDomain_url());
            UtilMethods.LogMethod("MovieSeriesActivity123_vod_list123_", String.valueOf(checkModel));
            if (checkModel == null) {
                insert(connectionInfoModel);
            }

        }

        @Transaction
        void updateLastLoginPlaylist(ConnectionInfoModel connectionInfoModel) {
            resetLastLoginPlaylist();
            UtilMethods.LogMethod("Splash123_MovieSeriesActivity123_vod_list123_", "updateVodList");
            updateLastLoginPlaylistTime(connectionInfoModel.getUid(), System.currentTimeMillis());
        }

        @Insert
        abstract void insert(ConnectionInfoModel... connectionInfoModels);

        @Query("SELECT * From ConnectionInfoModel WHERE friendly_name = :friendly_name AND " +
                "domain_url = :domain_url")
        abstract ConnectionInfoModel isConnectionAvailable(String friendly_name, String domain_url);

        @Query("SELECT uid From ConnectionInfoModel WHERE friendly_name = :friendly_name AND " +
                "domain_url = :domain_url")
        abstract long getConnectionId(String friendly_name, String domain_url);

        @Query("UPDATE ConnectionInfoModel SET last_live_updated_time = -1 WHERE uid LIKE :uid")
        abstract void resetLastUpdateTime(long uid);

        @Query("UPDATE ConnectionInfoModel SET last_live_updated_time = :time WHERE uid LIKE :uid")
        abstract void updateLastUpdateTime(long time, long uid);

        @Query("UPDATE ConnectionInfoModel SET last_vod_updated_time = -1 WHERE uid LIKE :uid")
        abstract void resetLastVodUpdateTime(long uid);

        @Query("UPDATE ConnectionInfoModel SET last_vod_updated_time = :time WHERE uid LIKE :uid")
        abstract void updateLastVodUpdateTime(long time, long uid);

        @Query("UPDATE ConnectionInfoModel SET last_series_updated_time = -1 WHERE uid LIKE :uid")
        abstract void resetLastSeriesUpdateTime(long uid);

        @Query("UPDATE ConnectionInfoModel SET last_series_updated_time = :time WHERE uid LIKE :uid")
        abstract void updateLastSeriesUpdateTime(long time, long uid);

        @Query("SELECT * From ConnectionInfoModel WHERE last_live_updated_time != -1")
        abstract ConnectionInfoModel getLastPlayedConnection();

        @Query("SELECT * From ConnectionInfoModel")
        abstract List<ConnectionInfoModel> getAllConnections();

        @Query("SELECT * From ConnectionInfoModel WHERE type LIKE :type")
        abstract List<ConnectionInfoModel> getConnections(String type);

        @Update
        abstract void update(ConnectionInfoModel connectionInfoModel);

        @Delete
        abstract void delete(ConnectionInfoModel connectionInfoModel);

        @Query("UPDATE ConnectionInfoModel SET last_live_updated_time=:milli WHERE uid =:uid")
        abstract void updateLastLoginPlaylistTime(long uid, long milli);

        @Query("UPDATE ConnectionInfoModel SET last_live_updated_time='-1'")
        abstract void resetLastLoginPlaylist();

        @Query("DELETE From ConnectionInfoModel WHERE domain_url=:domain_url")
        abstract void resetLastLoginPlaylistNew(String domain_url);

        @Query("SELECT * From ConnectionInfoModel WHERE last_live_updated_time != '-1'")
        abstract ConnectionInfoModel getLastLoginPlaylist();

        @Query("DELETE From ConnectionInfoModel")
        abstract void deleteAll();

        @Query("DELETE From ConnectionInfoModel WHERE " +
                "friendly_name =:friendly_name AND domain_url =:url")
        abstract void deleteConnection(String friendly_name, String url);
    }

    @Dao
    static abstract class RecordingScheduleDao {

//        @Transaction
//        void insertOrUpDateExternalPlayerData(ExternalPlayerModel externalPlayerModel) {
//            if (isPlayerAvailable(externalPlayerModel.getPlayer_package_name()) == null) {
//                insertExternalPlayer(externalPlayerModel);
//            }
//        }


        @Insert
        abstract void insertScheduleRecording(RecordingScheduleModel recordingScheduleModel);

        @Query("SELECT uid From RecordingScheduleModel WHERE connection_id =:connectionId AND " +
                "url=:url")
        abstract long getScheduleRecordingUid(long connectionId, String url);

        @Query("SELECT uid From RecordingScheduleModel WHERE  " +
                "url=:url AND startTime=:startTime AND endTime=:endTime AND recordpath=:getRecordpath AND showName=:showname ")
        abstract int getScheduleRecordingUidSpedific(String url, long startTime, long endTime, String getRecordpath, String showname);

        @Query("SELECT * From RecordingScheduleModel WHERE pkgname=:pkgname ORDER BY uid DESC")
        abstract List<RecordingScheduleModel> getAllScheduleRecordingwithpkgname(String pkgname);

        @Query("SELECT * From RecordingScheduleModel  ORDER BY uid DESC")
        abstract List<RecordingScheduleModel> getAllScheduleRecording();

        @Query("SELECT channelName From RecordingScheduleModel WHERE connection_id =:connectionId " +
                "AND channelName = :channel_name")
        abstract String getScheduleRecordingByName(long connectionId, String channel_name);

        @Query("SELECT channelName From RecordingScheduleModel WHERE connection_id =:connectionId " +
                "AND channelName = :channel_name AND showName = :show_name AND startTime = :start_time")
        abstract String getScheduleRecordingShow(long connectionId, String channel_name,
                                                 String show_name, long start_time);

        @Query("UPDATE RecordingScheduleModel SET status = :status" +
                " WHERE connection_id = :connectionId AND uid = :uid")
        abstract void updateScheduleRecordingStatus(long connectionId, long uid,
                                                    String status);

        @Query("SELECT * From RecordingScheduleModel WHERE connection_id =:connectionId " +
                "AND channelName = :channel_name AND showName = :show_name AND startTime = :start_time")
        abstract RecordingScheduleModel getScheduleRecordingByShowName(long connectionId, String channel_name,
                                                                       String show_name, long start_time);

        @Query("DELETE From RecordingScheduleModel WHERE uid = :uid")
        abstract void deleteScheduleRecording(long uid);

        @Query("DELETE FROM RecordingScheduleModel")
        abstract void deleteAll();

    }


}
