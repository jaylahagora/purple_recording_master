package com.smartrecording.recordingplugin.service;


import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseIntArray;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.fof.android.vlcplayer.utils.UtilMethods;
import com.smartrecording.recordingplugin.receiver.SensorRestarterBroadcastReceiver;
import com.xunison.recordingplugin.BuildConfig;
import com.smartrecording.recordingplugin.activity.DialogActivity;
import com.smartrecording.recordingplugin.activity.MainActivity;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.database.DatabaseRoom;
import com.smartrecording.recordingplugin.download.DownloadCallbackAdapter;
import com.smartrecording.recordingplugin.download.DownloadManager;
import com.smartrecording.recordingplugin.download.DownloadRequest;
import com.smartrecording.recordingplugin.download.Logger;
import com.smartrecording.recordingplugin.download.OkHttpDownloader;
import com.smartrecording.recordingplugin.download.Priority;
import com.smartrecording.recordingplugin.events.Stoprecording2;
import com.smartrecording.recordingplugin.utils.FileUtil;
import com.smartrecording.recordingplugin.utils.HybridFile;
import com.smartrecording.recordingplugin.utils.OpenMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

import static com.smartrecording.recordingplugin.utils.Constant.RECORDING_FILENAME;
import static com.smartrecording.recordingplugin.utils.Constant._REMAININGTIIME;
import static com.smartrecording.recordingplugin.utils.Constant._REMAININGTIIMESTR;
import static com.smartrecording.recordingplugin.utils.Constant._downloadspeed;
import static com.smartrecording.recordingplugin.utils.Constant._downloadstatus;
import static com.smartrecording.recordingplugin.utils.Constant.printDifference;


public class RecordingService2 extends IntentService {

    private static final String TAG = "RecordingService2";
    public static final int UPDATE_PROGRESS = 8344;
    public static int HANDLER_TIME = 1000;
    private static boolean isShowServiceLog = true;
    private Context mContext;
    private String downloadUrl;
    private String recoding_file_name;
    public String path;
    private int minute;
    String file_name;
    String file_nametemp = "";
    String pkgname;
    File apkFile;
    OutputStream outputStream;
    int response_result = 0;
    int count;
    long milliTime;
    long endTime;
    private final Handler handler = new Handler();
    public static volatile boolean shouldContinue_s2 = true;
    public static volatile boolean isrunningdownloadtask_s2 = false;
    public static volatile long runningtaskendtime_s2;
    public static volatile String _serviceRunningfilename = "";
    public static int filenamecounter = 0;
    /* for otg ------- start*/
    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;

    private static final String ACTION_USB_PERMISSION = "com.demo.otgusb.USB_PERMISSION";
    /* for otg ------- end*/
    //public static volatile String filenamechange = "(" + filenamecounter + ").mp4";


    //    MyReceiver myReceiver;
    private DialogActivity.ResultListener mListener = new DialogActivity.ResultListener() {

        @Override
        public void onretry(int number) {
            if (isShowServiceLog)
                if (isShowServiceLog) Log.e(TAG, "onretry: number" + number);
            // removed retry counter TODO
            //   if (RETRY_CURRENTCOUNTER <= RETRY_MAXCOUNTER && System.currentTimeMillis() <= endTime && shouldContinue) {
            if (System.currentTimeMillis() <= endTime && shouldContinue_s2) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        renamefilenameforretry();
                        backgroundTask();
                    }
                });
                thread.start();


            } else {
                removeCallBack();
            }
        }

        @Override
        public void ondismiss() {
            if (isShowServiceLog) Log.e(TAG, "ondismiss: called");
            removeCallBack();

        }
    };

    private void renamefilenameforretry() {
        filenamecounter++;
        recoding_file_name = file_nametemp.replace(".mp4", "(" + filenamecounter + ").mp4");
    }

    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isShowServiceLog) Log.e(TAG, "onReceive: service called");
            try {
                if (isShowServiceLog) Log.e(TAG, "onReceive: getAction:" + intent.getAction());
                if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("stoprecordingintent2")) {
                    String task = intent.getStringExtra("task");
                    String what = intent.getStringExtra("what");
                    if (isShowServiceLog) Log.e(TAG, "onReceive: what:" + what);
                    if (isShowServiceLog) Log.e(TAG, "onReceive: task:" + task);
                    if ((what != null && what.equalsIgnoreCase("stop")) && (task != null && task.equalsIgnoreCase("recording"))) {
                        shouldContinue_s2 = false;
                        if (runnableforbroadcast != null)
                            handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
                    }
                }

            } catch (Exception e) {
                if (isShowServiceLog) Log.e(TAG, "onReceive: catch:" + e.getMessage());
            }

        }
    }

    public RecordingService2() {
        super("RecordingService");
        setIntentRedelivery(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UtilMethods.LogMethod("recordingService123_onCreate",
                String.valueOf("onCreate"));
        mContext = RecordingService2.this;
        shouldContinue_s2 = true;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Subscribe
    public void stopme(Stoprecording2 stoprecording1) {
        shouldContinue_s2 = false;
        if (runnableforbroadcast != null)
            handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "onHandleIntent: service ");
        try {

            UtilMethods.LogMethod("recordingService123_onHandleIntent",
                    String.valueOf("onHandleIntent"));
            UtilMethods.LogMethod("recordingService123_intent",
                    String.valueOf(intent));
            if (intent != null) {
                if (isShowServiceLog) Log.e(TAG, "onHandleIntent: intent:" + intent.getAction());
                Log.e(TAG, "onHandleIntent: ssdd  :" + (intent.hasExtra("downloadUrl") ? "true" : "false"));
            }
            if (intent != null && shouldContinue_s2) {
                downloadUrl = intent.getStringExtra("downloadUrl");
                Log.e(TAG, "onHandleIntent: downloadUrl:" + downloadUrl);
                pkgname = intent.getStringExtra("pkgname");
                Log.e(TAG, "onHandleIntent: pkgname:" + pkgname);
                if (pkgname == null || pkgname.equals("")) {
                    //return;
                }
                path = intent.getStringExtra("path");
                Log.e(TAG, "onHandleIntent: path:" + path);
                //if (path == null || path.equals("")) {
                    path = MyApplication.getInstance().getPrefManager().getRecordingStoragePath();
              //  }
                recoding_file_name = intent.getStringExtra("recoding_file_name");
                file_nametemp = intent.getStringExtra("recoding_file_name");
                //
                // recoding_file_name = "";
                file_name = recoding_file_name;
                minute = intent.getIntExtra("minute", -1);
                long uid = intent.getLongExtra("uid", -1);
                UtilMethods.LogMethod("recordingService123_downloadUrl",
                        String.valueOf(downloadUrl));
                UtilMethods.LogMethod("recordingService123_recoding_file_name",
                        String.valueOf(recoding_file_name));
                UtilMethods.LogMethod("recordingService123_minute",
                        String.valueOf(minute));
                UtilMethods.LogMethod("recordingService123_uid",
                        String.valueOf(uid));
                if (uid != -1) {
                    DatabaseRoom.with(mContext).deleteScheduleRecording(uid);
                }
                if (downloadUrl == null || recoding_file_name == null || minute == -1) {
                    Log.e(TAG, "onHandleIntent: downloadUrl is null");
                    shouldContinue_s2 = false;
                    if (runnableforbroadcast != null)
                        handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
                    sendBroadcasttootherapp(0, pkgname, "completed", "");
                    return;
                }
                if (MyApplication.getInstance().getPrefManager().getIsRunningRecording2() && MyApplication.getInstance().getPrefManager().getRunningtasktime2() != -1) {
                    endTime = MyApplication.getInstance().getPrefManager().getRunningtasktime2();
                } else {
                    endTime = System.currentTimeMillis() + minute * 60000;
                }

                UtilMethods.LogMethod("recordingService123_endTime",
                        String.valueOf(endTime));
                milliTime = minute * 60 * 1000;
                handler.postDelayed(downloadMinuteRunnable, HANDLER_TIME);

                if (downloadUrl.contains(".ts") || downloadUrl.contains(".m3u8") && path != null && !path.equals("")) {
                    backgroundTask();
                } else {
                    NewDownloader();
                }


            } else {
                Log.e(TAG, "onHandleIntent: else");
                UtilMethods.LogMethod("recordingService123_intent",
                        String.valueOf("1111111"));
                removeCallBack();
            }
        } catch (Exception e) {
            Log.e(TAG, "onHandleIntent: catch:" + e.getMessage());
        }
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent);
            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case ACTION_USB_PERMISSION://User Authorized Broadcast
                    synchronized (this) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) { //Allow permission to apply
                            //   test();
                        } else {
                            //   logShow ("User is not authorized, access to USB device failed");
                        }
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED://USB device plugged into the broadcast
                    // logShow("USB device plugin");
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED://USB device unplugs the broadcast
                    // logShow("USB device unplugged");
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null)
            if (isShowServiceLog) Log.e(TAG, "onStartCommand:intent: " + intent.getAction());
        super.onStartCommand(intent, flags, startId);

        startTimer();
        return START_REDELIVER_INTENT;
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;
    public int counter = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;

    private void downloadwithvlc() {
        final ArrayList<String> args = new ArrayList<>();

        if (com.fof.android.vlcplayer.BuildConfig.DEBUG) {
            args.add("-vvv"); // verbosity
        }

        args.add("--aout=opensles");
        args.add("--vout=android_display");
        args.add("--audio-time-stretch"); // time stretching
        args.add("--no-sub-autodetect-file");
        args.add("--swscale-mode=0");
        args.add("--network-caching=1000");
        args.add("--no-drop-late-frames");
        args.add("--no-skip-frames");
        args.add("--avcodec-skip-frame");
        args.add("--avcodec-hw=any");
        mLibVLC = new LibVLC(mContext, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        Media media = new Media(mLibVLC, Uri.parse(downloadUrl));
        String directoryPath = path;
        String destination = directoryPath + "/" + file_name;
        if (isShowServiceLog) Log.e(TAG, "downloadwithvlc: destination:" + destination);
        File dirFile = new File(directoryPath);
        if (!dirFile.exists()) {
            boolean createFile = dirFile.mkdirs();

            if (isShowServiceLog)
                Log.e(TAG, "checkvlc: new File(destination):" + new File(destination));
            if (!new File(destination).exists()) {
                UtilMethods.LogMethod("dir/file not exist",
                        String.valueOf(createFile));
                return;
            }
            UtilMethods.LogMethod("recordingService123_createFile",
                    String.valueOf(createFile));
        } else {

        }
        // media.addOption(":sout=#transcode{vcodec=mp4v,vb=1024,acodec=mp4a,ab=192,channels=2,deinterlace}:standard{access=file,mux=ts,dst=" + destination + "}");
        // media.addOption(":sout=#transcode{vcodec=h264,vb=2000,venc=x264{profile=baseline},scale=Auto,width=1280,height=720,acodec=mp3,ab=192,channels=2,samplerate=44100}:standard{access=file,mux=ts,dst=" + destination + "}");
        // media.addOption(":sout=#std{access=file,mux=ts," + destination + "}");

        media.addOption(":sout=#standard{access=file,mux=ts,dst=" + destination + "}");

        //media.addOption(":no-sout-all");
        media.addOption(":sout-keep");

        mMediaPlayer.play(media);
        sendBroadcasttootherapp(0, pkgname, "started", "");
        runningtaskendtime_s2 = endTime;
        boolean isrunning = true;
        while (true) {

            //   if (isshoeservicelog)Log.e(TAG, "downloadwithvlc: System.currentTimeMillis():" + System.currentTimeMillis() + " >= endTime:" + endTime);

            if (System.currentTimeMillis() >= endTime) {
                //      if (isshoeservicelog)Log.e(TAG, "downloadwithvlc: time over");

                mMediaPlayer.stop();
                mMediaPlayer.release();
                mLibVLC.release();
                sendBroadcasttootherapp(0, pkgname, "completed", "");
                removeCallBack();
                break;

            } else {
                //   if (isshoeservicelog)Log.e(TAG, "downloadwithvlc: running");
                if (isrunning) {
                    isrunning = false;
                    sendBroadcasttootherapp(123, pkgname, "running", file_name);
                }
            }

            if (!shouldContinue_s2) {
                if (isShowServiceLog) Log.e(TAG, "downloadwithvlc: stopped:");
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mLibVLC.release();
                sendBroadcasttootherapp(0, pkgname, "completed", "");
                removeCallBack();
                break;
            }
        }


    }


    DownloadManager manager;
    private SparseIntArray ids = new SparseIntArray();
    private static final int INDEX_0 = 0;

    private void NewDownloader() {
        int index = INDEX_0;
        int id = ids.get(index, -1);
        List<ConnectionSpec> connectionSpecs=new ArrayList<>();
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).supportsTlsExtensions(true)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                .build();
        connectionSpecs.add(spec);
        connectionSpecs.addAll(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT));
        OkHttpClient client = new OkHttpClient.Builder() .hostnameVerifier((hostname, session) -> {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify(downloadUrl, session);
        }).connectionSpecs(connectionSpecs).build();
//        HttpsTrustManager.allowAllSSL();
        manager = new DownloadManager.Builder().context(this)
                .downloader(OkHttpDownloader.create(client))
                .threadPoolSize(3)
                .logger(new Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("TAG error:", message);
                    }
                })
                .build();

        if (manager.isDownloading(id)) {
            manager.cancel(id);
        } else {
            try {


                String directoryPath = path;// get the dir for download file
                if (isShowServiceLog) Log.e(TAG, "NewDownloader: directoryPath:" + directoryPath);
                // downloadUrl="https://oj7lng29dg82-hls-live.5centscdn.com/lives/f7b44cfafd5c52223d5498196c8a2e7b.sdp/playlist.m3u8";
                if (isShowServiceLog) Log.e(TAG, "NewDownloader: downloadUrl:" + downloadUrl);
//
                DownloadRequest request = new DownloadRequest.Builder().url(downloadUrl).destinationFilePath(directoryPath + "/" + file_name)
                        .downloadCallback(new Listener())
                        .retryTime(3).filename(file_name)
                        .endtime(endTime)
                        .retryInterval(3, TimeUnit.SECONDS)
                        .progressInterval(1, TimeUnit.SECONDS)
                        .priority(Priority.HIGH)
                        .allowedNetworkTypes(DownloadRequest.NETWORK_ALL)
                        .build();
                runningtaskendtime_s2 = endTime;
                int downloadId = manager.add(request);
                ids.put(index, downloadId);
            } catch (Exception e) {
                sendBroadcasttootherapp(0, pkgname, "completed", "");
                if (isShowServiceLog) Log.e(TAG, "NewDownloader: catch:" + e.getMessage());

            }
        }
    }

    private int queryIndex(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.valueAt(i) == id) {
                return ids.keyAt(i);
            }
        }

        return 0;
    }

    private class Listener extends DownloadCallbackAdapter {
        private long startTimestamp = 0;
        private long startSize = 0;

        @Override
        public void onStart(int downloadId, long totalBytes) {
            if (isShowServiceLog) Log.e(TAG, "start download: " + downloadId);
            if (isShowServiceLog) Log.e(TAG, "totalBytes: " + totalBytes);
            startTimestamp = System.currentTimeMillis();
            sendBroadcasttootherapp(0, pkgname, "started", "");
        }

        @Override
        public void onRetry(int downloadId) {

            if (isShowServiceLog) Log.e(TAG, "retry downloadId: " + downloadId);
            sendBroadcasttootherapp(0, pkgname, "retry", file_name);
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onProgress(int downloadId, long bytesWritten, long totalBytes, double aaa) {
            sendBroadcasttootherapp(aaa, pkgname, "running", file_name);


            int progress = (int) (bytesWritten * 100f / totalBytes);
            long currentTimestamp = System.currentTimeMillis();
            if (isShowServiceLog) Log.e(TAG, "progress: " + progress);

            int speed;
            int deltaTime = (int) (currentTimestamp - startTimestamp + 1);
            speed = (int) ((bytesWritten - startSize) * 1000 / deltaTime) / 1024;
            startSize = bytesWritten;

            int index = queryIndex(downloadId);
            switch (index) {
                case INDEX_0:
                    if (isShowServiceLog) Log.e(TAG, "onProgress: " + speed + "kb/s");
                    //  progressBar.setProgress(progress);
                    //  textSpeed.setText(speed + "kb/s");
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onSuccess(int downloadId, String filePath) {
            if (isShowServiceLog)
                Log.e(TAG, "success: " + downloadId + " size: " + new File(filePath).length());
            sendBroadcasttootherapp(0, pkgname, "completed", "");
        }

        @Override
        public void onFailure(int downloadId, int statusCode, String errMsg) {
            if (isShowServiceLog)
                Log.e(TAG, "fail: " + downloadId + " " + statusCode + " " + errMsg);
            sendBroadcasttootherapp(0, pkgname, "failed", file_name);
        }
    }

    static final Handler handlerforbroadcasr = new Handler();
    static Runnable runnableforbroadcast;

    private void sendBroadcasttootherapp(final double downloadspeed, final String pkgname, final String status, final String file_name) {
        if (isShowServiceLog) Log.e(TAG, "sendBroadcasttootherapp: status:" + status);
        Intent intent = new Intent();
        intent.setAction(mContext.getPackageName() + "2");
        intent.putExtra(_downloadspeed, String.valueOf(downloadspeed));
        intent.putExtra(RECORDING_FILENAME, file_name);
        intent.putExtra(_downloadstatus, status);
        intent.putExtra(_REMAININGTIIME, endTime);
        //intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra(_REMAININGTIIMESTR, printDifference(System.currentTimeMillis(), endTime, ""));
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        //  intent.setComponent(new ComponentName(pkgname, "com.purple.iptv.player.receivers.MyBroadcastReceiver"));
        intent.setComponent(new ComponentName(mContext.getPackageName(), "com.smartrecording.recordingplugin.receiver.MyBroadcastReceiver2"));
        //    if (isshoeservicelog)Log.e(TAG, "backgroundTask: aaasasas:" + String.valueOf(downloadspeed));
        sendBroadcast(intent);
        if (status.equalsIgnoreCase("running")) {
            final int delay = 1000;
            handlerforbroadcasr.postDelayed(runnableforbroadcast = new Runnable() {
                public void run() {
                    //do something
                    if (isShowServiceLog) Log.e(TAG, "run: handler started");
                    Intent intent = new Intent();
                    intent.setAction(mContext.getPackageName() + "2");
                    intent.putExtra(_downloadspeed, String.valueOf(downloadspeed));
                    intent.putExtra(RECORDING_FILENAME, file_name);
                    intent.putExtra(_downloadstatus, status);
                    intent.putExtra(_REMAININGTIIME, endTime);
                    //intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intent.putExtra(_REMAININGTIIMESTR, printDifference(System.currentTimeMillis(), endTime, ""));
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    intent.setComponent(new ComponentName(mContext.getPackageName(), "com.smartrecording.recordingplugin.receiver.MyBroadcastReceiver2"));
                    //      if (isshoeservicelog)Log.e(TAG, "backgroundTask: aaasasas:" + String.valueOf(downloadspeed));
                    MyApplication.getInstance().getPrefManager().setIsrunningRecording2(true);
                    MyApplication.getInstance().getPrefManager().setRunningtasktime2(endTime);
                    sendBroadcast(intent);
                    if (shouldContinue_s2) {
                        handlerforbroadcasr.postDelayed(runnableforbroadcast, delay);
                    } else {
                        handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
                    }
                }
            }, delay);


        }
        if (status.equalsIgnoreCase("completed")) {
            if (isShowServiceLog) Log.e(TAG, "run: handler stoepped");
            if (runnableforbroadcast != null)
                handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
            shouldContinue_s2 = false;
            filenamecounter = 0;
            isrunningdownloadtask_s2 = false;
            runningtaskendtime_s2 = 0;
            MyApplication.getInstance().getPrefManager().setIsrunningRecording2(false);
            MyApplication.getInstance().getPrefManager().setRunningtasktime2(-1);
        }

    }


    @Override
    public void onDestroy() {
        if (isShowServiceLog) Log.e(TAG, "onDestroy: called");
        handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
        handler.removeCallbacks(downloadMinuteRunnable);
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        broadcastIntent.putExtra("from", "s2");
        if (isShowServiceLog) Log.e(TAG, "onDestroy: shouldContinue:" + shouldContinue_s2);
        if (shouldContinue_s2)
            sendBroadcast(broadcastIntent);
        stoptimertask();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();

    }


    Runnable downloadMinuteRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e(TAG, "run: called");
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                String path1 = path;
                path1 = path1 + "/" + recoding_file_name;
                UtilMethods.LogMethod("recordingService123_path", String.valueOf(path1));
                File outFile = new File(path1);
                UtilMethods.LogMethod("recordingService123_exists", String.valueOf(outFile.exists()));
                UtilMethods.LogMethod("recordingService123_shouldContinue", String.valueOf(shouldContinue_s2));
                if (shouldContinue_s2) {
                    UtilMethods.LogMethod("recordingService123_outFile",
                            String.valueOf(outFile.exists()));
                    if (outFile.exists() && outFile.length() > 0) {
                        long timeInMillisec = System.currentTimeMillis();
                        UtilMethods.LogMethod("recordingService123_currentTime", String.valueOf(timeInMillisec));
                        UtilMethods.LogMethod("recordingService123_endTime", String.valueOf(endTime));
                        if (timeInMillisec < endTime) {
                            UtilMethods.LogMethod("recordingService123_ifff", String.valueOf("ifff"));
                            handler.postDelayed(downloadMinuteRunnable, HANDLER_TIME);
                        } else {
                            UtilMethods.LogMethod("recordingService123_ifff", String.valueOf("elsee"));
//                            Set<String> currently_recording_list = MyApplication.getInstance().
//                                    getPrefManager().
//                                    getCurrentlyRecordingList();
//                            currently_recording_list.remove(recoding_file_name);
//                            MyApplication.getInstance().getPrefManager().
//                                    setCurrentlyRecordingList(currently_recording_list);
//                            Toast.makeText(mContext, "Recording has Completed.",
//                                    Toast.LENGTH_LONG).show();
                            removeCallBack();
                        }
                    } else {
                        handler.postDelayed(downloadMinuteRunnable, HANDLER_TIME);
                    }
                } else {
                    UtilMethods.LogMethod("recordingService123_intent",
                            String.valueOf("222222"));
                    removeCallBack();
                }
            } catch (Exception e) {
                UtilMethods.LogMethod("recordingService123_eeeee",
                        String.valueOf(e));
                removeCallBack();
//                Toast.makeText(mContext, mContext.getString(R.string.recording_failed),
//                        Toast.LENGTH_LONG).show();
            }


        }
    };

    private void removeCallBack() {
        Log.e(TAG, "removeCallBack: called");
        sendBroadcasttootherapp(0, pkgname, "completed", "");
        _serviceRunningfilename = "";
        isrunningdownloadtask_s2 = false;
        Intent intent = new Intent();
//        intent.setAction(MY_TRIGGER);
//        intent.putExtra(_downloadspeed, "");
//        intent.putExtra(_downloadstatus, "completed");
//        sendBroadcast(intent);
        handler.removeCallbacks(downloadMinuteRunnable);
    }


    private void backgroundTask() {
        if (!MainActivity.checkExternalStoragewritebleornot(mContext) && !path.contains("emulated") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBroadcasttootherapp(0, pkgname, "completed", "");
            return;
        }
        try {
            if (downloadUrl.contains("http")) {
                if (BuildConfig.DEBUG) if (isShowServiceLog)
                    Log.e(TAG, "backgroundTask: downloadUrl" + downloadUrl);
                com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_uid",
                        String.valueOf("iffff"));
                Request.Builder request = new Request.Builder();
                request.url(downloadUrl);
                request.get();
                Request request1 = request.build();
                if (isShowServiceLog) Log.e(TAG, "backgroundTask: ........................1");
                List<ConnectionSpec> connectionSpecs=new ArrayList<>();
                ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                        .cipherSuites(
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                        .build();
                connectionSpecs.add(spec);
                connectionSpecs.addAll(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT));
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                        .connectionSpecs(connectionSpecs)
                        .connectTimeout(3, TimeUnit.MINUTES) // connect timeout
                        .readTimeout(1, TimeUnit.MINUTES)  // socket timeout
                        .writeTimeout(3, TimeUnit.MINUTES);
                if (isShowServiceLog) Log.e(TAG, "backgroundTask: ........................2");
                OkHttpClient client = clientBuilder.build();
                if (isShowServiceLog) Log.e(TAG, "backgroundTask: ........................3");
                Response response = client.newCall(request1).execute();
                if (isShowServiceLog) Log.e(TAG, "backgroundTask: ........................4");
                if (isShowServiceLog)
                    Log.e(TAG, "backgroundTask: response.body():" + response.body());
                if (isShowServiceLog)
                    Log.e(TAG, "backgroundTask: response.networkResponse().code():" + response.networkResponse().code());
                if (response.networkResponse() != null &&
                        response.body() != null) {
                    int status = response.networkResponse().code();
                    com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_status", String.valueOf(status));
                    // checking response status if 200 then countinue
                    if (status == HttpURLConnection.HTTP_OK ||
                            status == HttpURLConnection.HTTP_MOVED_TEMP) {
                        // checking response is not blank or null
                        if (response.body() != null) {
                            InputStream inputStream = response.body().byteStream();
//                            String directoryPath = MyApplication.getInstance().getPrefManager().
//                                    getRecordingStoragePath();// get the dir for download file
                            File dirFile = new File(path); // if not then create dir
                            com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_dirFile",
                                    String.valueOf(dirFile));
                            com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_exists",
                                    String.valueOf(dirFile.exists()));
                            // if dir not exist then create
                            if (!dirFile.exists()) {
                                boolean createFile = dirFile.mkdirs();
                                com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_createFile",
                                        String.valueOf(createFile));
                            }

                            file_name = recoding_file_name;

                            if (isShowServiceLog)
                                Log.e(TAG, "backgroundTask: recoding_file_name" + recoding_file_name);
                            //here code for if exist then append data into output stream
                            if (dirFile.getAbsolutePath().contains("emulated")) {
                                apkFile = new File(dirFile + "/" + file_name);
                                outputStream = new FileOutputStream(apkFile.getAbsolutePath(),
                                        apkFile.exists());
                            }   else {
                                String external_url = MyApplication.getInstance().getPrefManager().
                                        getExternalStorageUri();
                                DocumentFile document = DocumentFile.fromTreeUri(mContext,
                                        Uri.parse(external_url));
                                com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_document", String.valueOf(document));
                                com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_external_url", String.valueOf(external_url));
                                if (document != null) {
                                    try {
                                        Log.e(TAG, "backgroundTask: can write?:" + document.canWrite());

                                        Log.e(TAG, "backgroundTask: file_name:" + file_name);
                                        OpenMode openMode = OpenMode.FILE;
                                        HybridFile path = new HybridFile(openMode, this.path + "/" + file_name);
                                        DocumentFile document1 = FileUtil.getDocumentFile(path.getFile(), false, mContext);
                                        if (document1 != null) {
                                            DocumentFile apkFile = document1.createFile("video/MP2T",
                                                    path.getName());
                                            com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_apkFile", String.valueOf(apkFile));
                                            if (document1 != null) {
                                                outputStream = mContext.getContentResolver().
                                                        openOutputStream(document1.getUri());
                                            } else {
                                                if (isShowServiceLog)
                                                    Log.e(TAG, "backgroundTask: apkfile is null");
                                                //    Toast.makeText(mContext, "Something went wrong please change directory", Toast.LENGTH_SHORT).show();
                                                response_result = 0;
                                                sendBroadcasttootherapp(0, pkgname, "completed", "");
                                                return;

                                            }
                                        } else {
                                            if (isShowServiceLog)
                                                Log.e(TAG, "backgroundTask: document1 is null");
                                            //    Toast.makeText(mContext, "Something went wrong please change directory", Toast.LENGTH_SHORT).show();
                                            response_result = 0;
                                            sendBroadcasttootherapp(0, pkgname, "completed", "");
                                            return;
                                        }
                                    } catch (Exception e) {
                                        com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_ee1111", String.valueOf(e));
                                        sendBroadcasttootherapp(0, pkgname, "completed", "");
                                        return;
                                        //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_outputStream", String.valueOf(outputStream));
                            if (outputStream == null) {
                                response_result = 0;
                            }


                            byte data[] = new byte[4 * 1024];
                            double downloadElapsedTime = 0;
                            long endTime1 = 0;
                            long total = 0;
                            com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_inputStream",
                                    String.valueOf(inputStream.read()));
                            // added check for current task record untill end time reached
                            long startTime = System.currentTimeMillis();
                            boolean isrunning = true;
                            sendBroadcasttootherapp(0, pkgname, "started", "");
                            while ((count = inputStream.read(data)) != -1 && System.currentTimeMillis() <= endTime && shouldContinue_s2) {
                                total += count;
                                outputStream.write(data, 0, count);
                                isrunningdownloadtask_s2 = true;
                                runningtaskendtime_s2 = endTime;
                                _serviceRunningfilename = file_name;
                                if (total > 0) {
                                    endTime1 = System.currentTimeMillis();
                                    downloadElapsedTime = (endTime1 - startTime) / 1000.0;
                                    if (isrunning) {
                                        isrunning = false;
                                        sendBroadcasttootherapp(0, pkgname, "running", file_name);

                                    }


                                }
                            }
                            outputStream.flush();
                            outputStream.close();
                            inputStream.close();
                            runningtaskendtime_s2 = 0;
                            removeCallBack();
                        } else {
                            if (isShowServiceLog) Log.e(TAG, "backgroundTask: else 8");
                        }
                    } else {
                        if (isShowServiceLog) Log.e(TAG, "backgroundTask: else 9");
                        isrunningdownloadtask_s2 = false;
                        runningtaskendtime_s2 = 0;
                        _serviceRunningfilename = "";
                        com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_elseee", String.valueOf("elseee"));
                        sendBroadcasttootherapp(0, pkgname, "completed", "");
                        handler.removeCallbacks(downloadMinuteRunnable);
                    }
                } else {
                    if (isShowServiceLog) Log.e(TAG, "backgroundTask: else 10");

                }
            }
        } catch (Exception e) {
            isrunningdownloadtask_s2 = false;
            runningtaskendtime_s2 = 0;
            com.purple.iptv.player.utils.UtilMethods.LogMethod("recordingService123_eeeeee 1:",
                    String.valueOf(e.getMessage()));
            if (mContext != null) {
                if (isShowServiceLog)
                    Log.e(TAG, "backgroundTask: catch: contex not null and :" + e);
                if (e.getMessage() != null && e.getMessage().contains("bulk only mass storage reset failed")) {
                    startActivity(new Intent(mContext, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    shouldContinue_s2 = false;
                    if (runnableforbroadcast != null)
                        handlerforbroadcasr.removeCallbacks(runnableforbroadcast);
                    sendBroadcasttootherapp(0, pkgname, "completed", "");
                    return;
                }
                DialogActivity.setListener(mListener);
                startActivity(new Intent(mContext, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            } else {
                if (isShowServiceLog)
                    Log.e(TAG, "backgroundTask: catch: contex null and :" + e);
            }
            //removeCallBack();
        }

    }


    double instantDownloadRate = 0;

    public double setInstantDownloadRate(long downloadedByte, double elapsedTime) {

        if (downloadedByte >= 0) {
            this.instantDownloadRate = round((Double) (((downloadedByte * 8) / (1000 * 1000)) / elapsedTime), 2);
        } else {
            this.instantDownloadRate = 0.0;
        }
        return instantDownloadRate;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        } catch (Exception ex) {
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
