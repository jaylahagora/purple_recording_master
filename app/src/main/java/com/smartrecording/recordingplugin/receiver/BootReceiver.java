package com.smartrecording.recordingplugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.purple.iptv.player.utils.UtilMethods;
import com.smartrecording.recordingplugin.activity.MainActivity;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UtilMethods.LogMethod("receiver123_", "BootReceiver");

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);

    }
}
