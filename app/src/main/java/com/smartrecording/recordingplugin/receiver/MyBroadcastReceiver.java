package com.smartrecording.recordingplugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.purple.iptv.player.utils.Config;

import static com.purple.iptv.player.utils.Config.RECORDING_FILENAME;
import static com.purple.iptv.player.utils.Config._REMAININGTIIME;
import static com.purple.iptv.player.utils.Config._REMAININGTIIMESTR;
import static com.purple.iptv.player.utils.Config._downloadspeed;
import static com.purple.iptv.player.utils.Config._downloadstatus;

import androidx.annotation.Keep;

@Keep
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //  Toast.makeText(context, "Data Received from External App", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onReceive:MyBroadcastReceiver in service called ");

        if (intent != null) {
            if (intent.getAction() != null && intent.getAction().equals(context.getPackageName())) {
                String downloadspeed = intent.getStringExtra(_downloadspeed);
                String downloadstatus = intent.getStringExtra(_downloadstatus);
                String recordingname = intent.getStringExtra(RECORDING_FILENAME);
                String rtime = intent.getStringExtra(_REMAININGTIIMESTR);
                long reminingtime = intent.getLongExtra(_REMAININGTIIME, System.currentTimeMillis());
                Log.e(TAG, "onReceive:recordingname " + recordingname);
                Log.e(TAG, "onReceive:downloadstatus " + downloadstatus);
                Log.e(TAG, "onReceive:downloadspeed " + downloadspeed);
                Log.e(TAG, "onReceive:reminingtime " + reminingtime);
                Log.e(TAG, "onReceive:rtime " + rtime);

                Intent intent1 = new Intent();
                intent1.setAction(Config.MY_TRIGGER);
                intent1.putExtra(_downloadspeed, downloadspeed);
                intent1.putExtra(_downloadstatus, downloadstatus);
                intent1.putExtra(RECORDING_FILENAME, recordingname);
                intent1.putExtra(_REMAININGTIIME, reminingtime);
                intent1.putExtra(_REMAININGTIIMESTR, rtime);
                context.sendBroadcast(intent1);

            } else if(intent.getAction() != null && intent.getAction().equals("stoprecordingintent")) {
                Intent intent1 = new Intent();
                intent1.setAction("stoprecordingintent");
                intent1.putExtra("task", intent.getStringExtra("task"));
                intent1.putExtra("what", intent.getStringExtra("what"));
                context.sendBroadcast(intent1);
            }
        }

    }


}
