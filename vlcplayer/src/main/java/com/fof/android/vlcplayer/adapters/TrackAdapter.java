package com.fof.android.vlcplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fof.android.vlcplayer.R;
import com.fof.android.vlcplayer.models.TrackModel;

import java.util.ArrayList;


public class TrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<TrackModel> list;
    LayoutInflater inflater;
    BluetoothClickInterface listener;
    private View previous_selected;
    View previous_focused_view;
    private int previous_selected_position;
    boolean staticFocusDone = false;


    public TrackAdapter(Context context, ArrayList<TrackModel> list,
                        BluetoothClickInterface listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cardview_track, parent, false);
        return new TrackViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 final int position) {
        if (holder instanceof TrackViewHolder) {
            final TrackViewHolder trackHolder = (TrackViewHolder) holder;
            TrackModel trackModel = list.get(position);
            trackHolder.sf_text.setText(trackModel.getName());

            if (trackModel.isCurrentTrack()) {
                trackHolder.sf_linear.setSelected(true);
                previous_selected = trackHolder.sf_linear;
            } else {
                trackHolder.sf_linear.setSelected(false);
            }

            if (position == 0 && !staticFocusDone) {
                staticFocusDone = true;
                trackHolder.sf_linear.requestFocus();
            }

            trackHolder.sf_linear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        if (listener != null) {
                            listener.onFocused(trackHolder, position, true);
                        }
                    } else {
                        if (listener != null) {
                            listener.onFocused(trackHolder, position, false);
                        }
                    }
                }
            });


            trackHolder.sf_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (previous_selected != null) {
                        previous_selected.setSelected(false);
                    }
                    previous_selected = trackHolder.sf_linear;
                    trackHolder.sf_linear.setSelected(true);
                    previous_selected_position = position;
                    if (listener != null) {
                        listener.onClick(trackHolder, position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {
        private final TextView sf_text;
        private final LinearLayout sf_linear;

        public TrackViewHolder(View itemView) {
            super(itemView);
            sf_text = (TextView) itemView.findViewById(R.id.sf_text);
            sf_linear = (LinearLayout) itemView.findViewById(R.id.sf_linear);
        }
    }


    public interface BluetoothClickInterface {
        public void onClick(TrackViewHolder holder, int position);

        public void onFocused(TrackViewHolder holder, int position, boolean hasFocused);
    }


}
