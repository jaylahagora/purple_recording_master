package com.smartrecording.recordingplugin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.purple.iptv.player.common.StorageListClass;
import com.purple.iptv.player.utils.UtilMethods;

import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.xunison.recordingplugin.R;

import java.util.ArrayList;

public class StorageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<StorageListClass.StorageFileModel> list;
    LayoutInflater inflater;
    adapterInterface listener;


    public StorageListAdapter(Context context,
                              ArrayList<StorageListClass.StorageFileModel> list,
                              adapterInterface listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cardview_storage, parent, false);
        return new StorageViewHolder(v);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 final int position) {
        UtilMethods.LogMethod("recy1212_", "onBindViewHolder");
        if (holder instanceof StorageViewHolder) {
            final StorageViewHolder storageHolder = (StorageViewHolder) holder;
            StorageListClass.StorageFileModel model =
                    (StorageListClass.StorageFileModel) list.get(position);

            if (model.isFolder()) {
                storageHolder.media_image.setImageResource(R.drawable.ic_folder_svg);
            } else {
                storageHolder.media_image.setImageResource(R.drawable.ic_file_svg);
            }

            storageHolder.folder_name.setText(model.getNick_name());

            storageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(storageHolder, position);
                    }
                }
            });
            ColorModel colorModel = new Gson().fromJson(MyApplication.getInstance().getPrefManager().getColormodel(), ColorModel.class);
            storageHolder.ll_cv_storage.setBackground(getBgstoragecard(colorModel.getSelected_categoryList(), colorModel.getTab_selected(), colorModel.getFocused_selected_color(), mContext.getResources().getColor(R.color.transparent)));


        }
    }

    public static StateListDrawable getBgstoragecard(int selected_categoryList, int tab_selected, int normalcolor, int focusecolor) {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected}, createNormalDrawablewithstroke(selected_categoryList));
        out.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected}, createNormalDrawablewithstroke(tab_selected));
        out.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected}, createNormalDrawablewithstroke(normalcolor));
        out.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected}, createNormalDrawablewithstroke(focusecolor));

        return out;
    }

    public static GradientDrawable createNormalDrawablewithstroke(int color ) {
        GradientDrawable out = new GradientDrawable();
  //     out.setCornerRadius(5);
        out.setColor(color);
    //    out.setStroke(0, strokecolor);
        return out;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StorageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView media_image;
        private final TextView folder_name;
        LinearLayout ll_cv_storage;


        public StorageViewHolder(View itemView) {
            super(itemView);
            media_image = (ImageView) itemView.findViewById(R.id.storage_type_image);
            folder_name = (TextView) itemView.findViewById(R.id.folder_name);
            ll_cv_storage = itemView.findViewById(R.id.ll_cv_storage);

        }
    }

    public interface adapterInterface {

        public void onClick(StorageViewHolder holder, int position);

    }

}
