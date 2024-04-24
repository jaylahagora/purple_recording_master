package com.fof.android.vlcplayer.common;

import android.content.Context;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fof.android.vlcplayer.R;
import com.fof.android.vlcplayer.adapters.TrackAdapter;
import com.fof.android.vlcplayer.models.TrackModel;

import java.util.ArrayList;

public class CustomDialogs {

    public static void showTrackInfoDialog(Context mContext, String type,
                                           final ArrayList<TrackModel> track_info,
                                           final DialogInterface.trackRecyclerDialog trackListener) {
        if (track_info != null && track_info.size() > 0) {
            final android.app.Dialog dialog = new android.app.Dialog(mContext, R.style.ThemeDialog);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_track_recycler);
            final RecyclerView recycler_dialog_track =
                    (RecyclerView) dialog.findViewById(R.id.recycler_dialog_track);
            final android.widget.TextView text_track_type = (android.widget.TextView) dialog.findViewById(R.id.text_track_type);

            String media_type = "";
            if (type.equalsIgnoreCase("video")) {
                media_type = "Video Tracks";
            } else if (type.equalsIgnoreCase("audio")) {
                media_type = "Audio Tracks";
            } else if (type.equalsIgnoreCase("subtitle")) {
                media_type = "Subtitles";
            }

            text_track_type.setText(media_type);
            TrackAdapter adapter = new TrackAdapter(mContext, track_info,
                    new TrackAdapter.BluetoothClickInterface() {
                        @Override
                        public void onClick(TrackAdapter.TrackViewHolder holder, int position) {
                            if (trackListener != null) {
                                trackListener.onTrackSelected(track_info.get(position));
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFocused(TrackAdapter.TrackViewHolder holder,
                                              int position, boolean hasFocused) {

                        }
                    });

            recycler_dialog_track.setLayoutManager(new LinearLayoutManager(mContext));
            recycler_dialog_track.setAdapter(adapter);
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);


            dialog.show();
        }
    }
}
