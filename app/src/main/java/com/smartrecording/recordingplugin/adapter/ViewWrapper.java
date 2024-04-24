package com.smartrecording.recordingplugin.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {
    public ViewWrapper(V v) {
        super(v);
    }
}
