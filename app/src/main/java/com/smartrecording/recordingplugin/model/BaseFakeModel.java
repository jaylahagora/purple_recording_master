package com.smartrecording.recordingplugin.model;


public class BaseFakeModel {
    public BaseFakeModel() {

    }

    public int getMviewtype() {
        return mviewtype;
    }

    public void setMviewtype(int mviewtype) {
        this.mviewtype = mviewtype;
    }

    public int mviewtype;

    public BaseFakeModel(int i) {
        mviewtype = i;
    }

    public int getViewType() {
        return mviewtype;
    }
}
