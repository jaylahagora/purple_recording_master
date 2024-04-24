package com.smartrecording.recordingplugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smartrecording.recordingplugin.service.RecordingService;
import com.smartrecording.recordingplugin.service.RecordingService2;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SensorRestarterBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        try {
            String from = "";
            from = intent.hasExtra("from") ? intent.getStringExtra("from") : "";

            if (from != null && from.equals("s2"))
                context.startService(new Intent(context, RecordingService.class));
            else
                context.startService(new Intent(context, RecordingService2.class));
        } catch (Exception e) {
            Log.e(TAG, "onReceive: catch:" + e.getMessage());
        }
    }

}
