package com.smartrecording.recordingplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "NotificationidstoreModel")
public class NotificationidstoreModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    @ColumnInfo(name = "notificationid")
    private long notificationid;


    public NotificationidstoreModel() {
    }

    protected NotificationidstoreModel(Parcel in) {
        uid = in.readLong();
        notificationid = in.readLong();

    }

    public static final Creator<NotificationidstoreModel> CREATOR = new Creator<NotificationidstoreModel>() {
        @Override
        public NotificationidstoreModel createFromParcel(Parcel in) {
            return new NotificationidstoreModel(in);
        }

        @Override
        public NotificationidstoreModel[] newArray(int size) {
            return new NotificationidstoreModel[size];
        }
    };

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(uid);
        parcel.writeLong(notificationid);

    }

    public long getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(long notificationid) {
        this.notificationid = notificationid;
    }
}
