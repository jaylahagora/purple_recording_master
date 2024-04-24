package com.smartrecording.recordingplugin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purple.iptv.player.utils.UtilMethods;
import com.xunison.recordingplugin.R;

import java.util.ArrayList;

public class PopupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<String> list;
    LayoutInflater inflater;
    BluetoothClickInterface listener;

    boolean staticFocusDone = false;


    public PopupAdapter(Context context, ArrayList<String> list,
                        BluetoothClickInterface listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cardview_popup_layout, parent, false);
        return new PopupViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 final int position) {
        if (holder instanceof PopupViewHolder) {
            final PopupViewHolder popupHolder = (PopupViewHolder) holder;
            String text = list.get(position);
            if (text != null && text.contains(".")) {
                text = UtilMethods.getAppNameFromPackageName(mContext, text);
                if (text.equalsIgnoreCase("")) {
                    text = list.get(position);
                }
            }
            popupHolder.text_popup_item.setText(text);
            popupHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(popupHolder, position);

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PopupViewHolder extends RecyclerView.ViewHolder {

        private final TextView text_popup_item;
        private final ImageView image_popup_item;

        public PopupViewHolder(View itemView) {
            super(itemView);
            text_popup_item = (TextView) itemView.findViewById(R.id.text_popup_item);
            image_popup_item = (ImageView) itemView.findViewById(R.id.image_popup_item);
        }
    }


    public interface BluetoothClickInterface {
        public void onClick(PopupViewHolder holder, int position);
    }


}
