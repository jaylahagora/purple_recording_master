package com.fof.android.vlcplayer.common;

import com.fof.android.vlcplayer.models.TrackModel;


public interface DialogInterface {

    public interface popupRecyclerListener {
        void onListItemClick(String clicked_text);

        void onPopupDismissed(android.widget.PopupWindow popupWindow);
    }

    public interface trackRecyclerDialog {
        void onTrackSelected(TrackModel trackModel);
    }


}
