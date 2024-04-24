package com.smartrecording.recordingplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordingModel extends BaseModel implements Parcelable {

    String fileName;
    String filePath;
    String fileSize;
    String fileDownloadDate;
    String status;
    long lastModified;


    public RecordingModel() {
    }

    protected RecordingModel(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        fileSize = in.readString();
        fileDownloadDate = in.readString();
        status = in.readString();
    }

    public static final Creator<RecordingModel> CREATOR = new Creator<RecordingModel>() {
        @Override
        public RecordingModel createFromParcel(Parcel in) {
            return new RecordingModel(in);
        }

        @Override
        public RecordingModel[] newArray(int size) {
            return new RecordingModel[size];
        }
    };

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDownloadDate() {
        return fileDownloadDate;
    }

    public void setFileDownloadDate(String fileDownloadDate) {
        this.fileDownloadDate = fileDownloadDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeString(fileSize);
        parcel.writeString(fileDownloadDate);
        parcel.writeString(status);
    }
}
