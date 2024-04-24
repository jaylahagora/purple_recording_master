package com.purple.iptv.player.common;

import android.app.Dialog;
import android.widget.ProgressBar;
import android.widget.TextView;


public class CustomInterface {

    public interface SortByListener {
        void onSortData(Dialog dialog, int sort_type);

    }

    public interface ArchiveListener {
        void onarchive();

    }

    public interface deviceTypeListener {
        void onDone(Dialog dialog);
    }


    public interface onSearchClick {

        boolean onSearchBtnClick();

        void onPerformSearch(String keyword);

        void onSortClick(Dialog dialog, int sort_type);
    }

    public interface epgDialogInterface {
        void onAddClick(Dialog dialog, String url);

        void onCancel(Dialog dialog);
    }

    public interface versionUpdateInterface {
        void onRemindMe(Dialog dialog);

        void onUpdate(Dialog dialog);
    }

    public interface resetSettingsInterface {
        void onYes(Dialog dialog);

        void onNo(Dialog dialog);
    }

    public interface MultiScreenInfoDialog {
        void onOkClick();
    }

    public interface MultiScreenExitDialog {
        void onExit();

        void onChangeLayout();
    }

    public interface MultiScreenLayoutDialog {
        void onClick(int layoutType);
    }

    public interface parentControlInterface {

        void onSubmit(Dialog dialog, String password);

    }

    public interface deleteAlertInterface {

        void onYes(Dialog dialog);

    }

    public interface onRecordingPathChanged {

        void onChange();

    }

    public interface updateByListener {
        void UpdateBy(Dialog dialog, int update_type);

        void onCancelClick(Dialog dialog);
    }

    public interface permissionDeclineListener {
        void onYes(Dialog dialog);

        void onNo(Dialog dialog);
    }

    public interface smartTVCodeListener {
        void onSubmit(Dialog dialog, String entered_text);

        void onCloseApp(Dialog dialog);
    }

    public interface percentageProgressBarListener {
        void onDialogShown(Dialog dialog, ProgressBar progressBar, TextView progressText);

        void onCancel(Dialog dialog);
    }

    public interface onCancelUpdateListener {
        void onCancel();
    }

    public interface onStorageDialogListener {

        void onOkClicked(String path);
    }



    public interface onParentalListener {
        void onSubmit(Dialog dialog);

        void onCancel(Dialog dialog);
    }





    public interface dialogBootOnStartup {
        void onDone(Dialog dialog);
    }

    public interface dialogPermission {
        void onYes(Dialog dialog);

        void onlater(Dialog dialog);
    }

    public interface dialogWarningonLogout {
        void onYes(Dialog dialog);

        void onNo(Dialog dialog);
    }
    public interface dialogWarningNoInternet {
        void onYes(Dialog dialog);

        void onNo(Dialog dialog);
    }


}
