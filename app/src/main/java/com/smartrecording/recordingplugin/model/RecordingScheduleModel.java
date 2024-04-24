package com.smartrecording.recordingplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "RecordingScheduleModel")
public class RecordingScheduleModel extends BaseModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "connection_id")
    long connection_id;
    @ColumnInfo(name = "showName")
    String showName;
    @ColumnInfo(name = "channelName")
    String channelName;
    @ColumnInfo(name = "startTime")
    long startTime;
    @ColumnInfo(name = "endTime")
    long endTime;
    @ColumnInfo(name = "status")
    String status;
    @ColumnInfo(name = "url")
    String url;
    @ColumnInfo(name = "pkgname")
    String pkgname;
    @ColumnInfo(name = "recordpath")
    String recordpath;
    @ColumnInfo(name = "servicenumber")
    String servicenumber;

    public String getServicenumber() {
        return servicenumber;
    }

    public void setServicenumber(String servicenumber) {
        this.servicenumber = servicenumber;
    }

    public String getRecordpath() {
        return recordpath;
    }

    public void setRecordpath(String recordpath) {
        this.recordpath = recordpath;
    }


    public RecordingScheduleModel() {
    }

    protected RecordingScheduleModel(Parcel in) {
        uid = in.readInt();
        connection_id = in.readLong();
        showName = in.readString();
        channelName = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        status = in.readString();
        url = in.readString();
        pkgname = in.readString();
        recordpath = in.readString();
        servicenumber = in.readString();
    }

    public static final Creator<RecordingScheduleModel> CREATOR = new Creator<RecordingScheduleModel>() {
        @Override
        public RecordingScheduleModel createFromParcel(Parcel in) {
            return new RecordingScheduleModel(in);
        }

        @Override
        public RecordingScheduleModel[] newArray(int size) {
            return new RecordingScheduleModel[size];
        }
    };

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(long connection_id) {
        this.connection_id = connection_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(uid);
        parcel.writeLong(connection_id);
        parcel.writeString(showName);
        parcel.writeString(channelName);
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeString(status);
        parcel.writeString(url);
        parcel.writeString(pkgname);
        parcel.writeString(recordpath);
        parcel.writeString(servicenumber);
    }
}
