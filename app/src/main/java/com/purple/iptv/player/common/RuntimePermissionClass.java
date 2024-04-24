package com.purple.iptv.player.common;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RuntimePermissionClass {
    public String[] PERMISSION_LIST;
    public static final int REQUEST_PERMISSIONS = 1001;
    private Activity mActivity;
    private static RuntimePermissionClass mInstance;

    private RuntimePermissionClass(Activity mActivity) {
        this.mActivity = mActivity;
    }


    public static RuntimePermissionClass getInstance(Activity activity) {
        mInstance = new RuntimePermissionClass(activity);
        return mInstance;
    }

    public boolean isPermissionsGranted(String[] PERMISSION_LIST) {
        this.PERMISSION_LIST = PERMISSION_LIST;
        if (isPermissionRequired()) {
            requestMultiplePermissions();
            return false;
        } else {
            return true;
        }
    }

    private boolean isPermissionRequired() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSION_LIST) {
                if ((ContextCompat.checkSelfPermission(mActivity,
                        permission) != PackageManager.PERMISSION_GRANTED)) {
                    return true;
                }
            }
        }
        return false;

    }

    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : PERMISSION_LIST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mActivity.checkSelfPermission(permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    remainingPermissions.add(permission);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.requestPermissions(remainingPermissions.
                            toArray(new String[remainingPermissions.size()]),
                    REQUEST_PERMISSIONS);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults,onPermissionResult listener) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        listener.onPermissionGranted();
                    } else {
//                        CustomDialogs.onDeclinePermissionDialog(mActivity,
//                                new CustomInterface.permissionDeclineListener() {
//                                    @Override
//                                    public void onYes(Dialog dialog) {
//                                        isPermissionsGranted(PERMISSION_LIST);
//                                    }
//
//                                    @Override
//                                    public void onNo(Dialog dialog) {
//                                        listener.onPermissionDenied();
//                                    }
//                                });
                        return;
                    }
                }
            }
        }
    }

    public interface onPermissionResult {

        void onPermissionGranted();

        void onPermissionDenied();
    }

}
