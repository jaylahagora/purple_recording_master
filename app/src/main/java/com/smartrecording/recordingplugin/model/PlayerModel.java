package com.smartrecording.recordingplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerModel implements Parcelable {

    String media_name;
    String media_url;
    String user_agent;

    public PlayerModel() {
    }

    protected PlayerModel(Parcel in) {
        media_name = in.readString();
        media_url = in.readString();
        user_agent = in.readString();
    }

    public static final Creator<PlayerModel> CREATOR = new Creator<PlayerModel>() {
        @Override
        public PlayerModel createFromParcel(Parcel in) {
            return new PlayerModel(in);
        }

        @Override
        public PlayerModel[] newArray(int size) {
            return new PlayerModel[size];
        }
    };

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
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
        parcel.writeString(media_name);
        parcel.writeString(media_url);
        parcel.writeString(user_agent);
    }
}
