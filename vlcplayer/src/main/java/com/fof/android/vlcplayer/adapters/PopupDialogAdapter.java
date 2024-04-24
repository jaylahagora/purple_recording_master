package com.fof.android.vlcplayer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fof.android.vlcplayer.R;

import java.util.ArrayList;

import static com.fof.android.vlcplayer.VLCPlayer.DEFAULTSELECTION;


public class PopupDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<String> list;
    LayoutInflater inflater;
    RecyclerClickInterface listener;

    boolean staticFocusDone = false;


    public PopupDialogAdapter(Context context, ArrayList<String> list,
                              RecyclerClickInterface listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cardview_popup_dialog_layout, parent, false);
        return new PopupViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 final int position) {
        if (holder instanceof PopupViewHolder) {
            final PopupViewHolder popupHolder = (PopupViewHolder) holder;
            String text = list.get(position);
            popupHolder.text_popup_item.setText(text);
            if (DEFAULTSELECTION != null && !DEFAULTSELECTION.equals("") && DEFAULTSELECTION.equalsIgnoreCase(text)) {
                popupHolder.text_popup_item.setSelected(true);
                popupHolder.text_popup_item.setTextColor(ContextCompat.getColor(mContext, R.color.selected_color));
            } else {
                popupHolder.text_popup_item.setSelected(false);
                popupHolder.text_popup_item.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }
            popupHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(popupHolder, position);

                    }
                }
            });
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (listener != null)
                            listener.onFocus(popupHolder, position);
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


    public interface RecyclerClickInterface {
        public void onClick(PopupViewHolder holder, int position);

        public void onFocus(PopupViewHolder holder, int position);
    }


}
