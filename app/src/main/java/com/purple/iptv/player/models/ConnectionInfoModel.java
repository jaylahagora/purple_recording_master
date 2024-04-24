package com.purple.iptv.player.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.smartrecording.recordingplugin.model.BaseModel;

@Entity(tableName = "ConnectionInfoModel")
public class ConnectionInfoModel extends BaseModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    @ColumnInfo(name = "friendly_name")
    private String friendly_name;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "online")
    private boolean online;
    @ColumnInfo(name = "domain_url")
    private String domain_url;
    @ColumnInfo(name = "epg_url")
    private String epg_url;
    @ColumnInfo(name = "vod_url")
    private String vod_url;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "epg_mode")
    private String epg_mode;
    @ColumnInfo(name = "expire_date")
    private long expire_date;
    @ColumnInfo(name = "epg_offset")
    private String epg_offset;
    @ColumnInfo(name = "group_channel_numbering")
    private String group_channel_numbering;
    @ColumnInfo(name = "last_live_updated_time")
    private long last_live_updated_time = -1;
    @ColumnInfo(name = "last_vod_updated_time")
    private long last_vod_updated_time = -1;
    @ColumnInfo(name = "last_series_updated_time")
    private long last_series_updated_time = -1;
    @ColumnInfo(name = "last_login")
    private boolean last_login = false;
    @ColumnInfo(name = "user_agent")
    private String user_agent;

    public ConnectionInfoModel() {
    }


    protected ConnectionInfoModel(Parcel in) {
        uid = in.readLong();
        friendly_name = in.readString();
        type = in.readString();
        online = in.readByte() != 0;
        domain_url = in.readString();
        epg_url = in.readString();
        vod_url = in.readString();
        username = in.readString();
        password = in.readString();
        epg_mode = in.readString();
        expire_date = in.readLong();
        epg_offset = in.readString();
        group_channel_numbering = in.readString();
        last_live_updated_time = in.readLong();
        last_vod_updated_time = in.readLong();
        last_series_updated_time = in.readLong();
        last_login = in.readByte() != 0;
        user_agent = in.readString();
    }

    public static final Creator<ConnectionInfoModel> CREATOR = new Creator<ConnectionInfoModel>() {
        @Override
        public ConnectionInfoModel createFromParcel(Parcel in) {
            return new ConnectionInfoModel(in);
        }

        @Override
        public ConnectionInfoModel[] newArray(int size) {
            return new ConnectionInfoModel[size];
        }
    };

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getFriendly_name() {
        return friendly_name;
    }

    public void setFriendly_name(String friendly_name) {
        this.friendly_name = friendly_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDomain_url() {
        return domain_url;
    }

    public void setDomain_url(String domain_url) {
        this.domain_url = domain_url;
    }

    public String getEpg_url() {
        return epg_url;
    }

    public void setEpg_url(String epg_url) {
        this.epg_url = epg_url;
    }

    public String getVod_url() {
        return vod_url;
    }

    public void setVod_url(String vod_url) {
        this.vod_url = vod_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEpg_mode() {
        return epg_mode;
    }

    public void setEpg_mode(String epg_mode) {
        this.epg_mode = epg_mode;
    }

    public String getEpg_offset() {
        return epg_offset;
    }

    public void setEpg_offset(String epg_offset) {
        this.epg_offset = epg_offset;
    }

    public String getGroup_channel_numbering() {
        return group_channel_numbering;
    }

    public void setGroup_channel_numbering(String group_channel_numbering) {
        this.group_channel_numbering = group_channel_numbering;
    }

    public long getLast_live_updated_time() {
        return last_live_updated_time;
    }

    public void setLast_live_updated_time(long last_live_updated_time) {
        this.last_live_updated_time = last_live_updated_time;
    }

    public long getLast_vod_updated_time() {
        return last_vod_updated_time;
    }

    public void setLast_vod_updated_time(long last_vod_updated_time) {
        this.last_vod_updated_time = last_vod_updated_time;
    }

    public long getLast_series_updated_time() {
        return last_series_updated_time;
    }

    public void setLast_series_updated_time(long last_series_updated_time) {
        this.last_series_updated_time = last_series_updated_time;
    }

    public long getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(long expire_date) {
        this.expire_date = expire_date;
    }

    public boolean isLast_login() {
        return last_login;
    }

    public void setLast_login(boolean last_login) {
        this.last_login = last_login;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeLong(uid);
        parcel.writeString(friendly_name);
        parcel.writeString(type);
        parcel.writeByte((byte) (online ? 1 : 0));
        parcel.writeString(domain_url);
        parcel.writeString(epg_url);
        parcel.writeString(vod_url);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(epg_mode);
        parcel.writeLong(expire_date);
        parcel.writeString(epg_offset);
        parcel.writeString(group_channel_numbering);
        parcel.writeLong(last_live_updated_time);
        parcel.writeLong(last_vod_updated_time);
        parcel.writeLong(last_series_updated_time);
        parcel.writeByte((byte) (last_login ? 1 : 0));
        parcel.writeString(user_agent);
    }

    @Override
    public String toString() {
        return "ConnectionInfoModel{" +
                "uid=" + uid +
                ", friendly_name='" + friendly_name + '\'' +
                ", type='" + type + '\'' +
                ", online=" + online +
                ", domain_url='" + domain_url + '\'' +
                ", epg_url='" + epg_url + '\'' +
                ", vod_url='" + vod_url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", epg_mode='" + epg_mode + '\'' +
                ", expire_date=" + expire_date +
                ", epg_offset='" + epg_offset + '\'' +
                ", group_channel_numbering='" + group_channel_numbering + '\'' +
                ", last_live_updated_time=" + last_live_updated_time +
                ", last_vod_updated_time=" + last_vod_updated_time +
                ", last_series_updated_time=" + last_series_updated_time +
                ", last_login=" + last_login +
                ", user_agent='" + user_agent + '\'' +
                '}';
    }
}
