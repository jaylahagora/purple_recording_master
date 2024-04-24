package com.fof.android.vlcplayer;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fof.android.vlcplayer.adapters.PopupDialogAdapter;
import com.fof.android.vlcplayer.common.CustomDialogs;
import com.fof.android.vlcplayer.common.DialogInterface;
import com.fof.android.vlcplayer.common.OnSwipeTouchListener;
import com.fof.android.vlcplayer.models.TrackModel;
import com.fof.android.vlcplayer.utils.UtilMethods;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.interfaces.IMedia;
import org.videolan.libvlc.interfaces.IVLCVout;
import org.videolan.libvlc.util.AndroidUtil;
import org.videolan.libvlc.util.DisplayManager;

import java.util.ArrayList;
import java.util.Map;

import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;

public class VLCPlayer extends FrameLayout implements IVLCVout.OnNewVideoLayoutListener,
        SurfaceHolder.Callback, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

//    static {
//        System.loadLibrary("vlcffmpeg");
//    }

    private MediaPlayer.ScaleType mCurrentScaleType = MediaPlayer.ScaleType.SURFACE_BEST_FIT;

    private int mVideoHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoVisibleHeight = 0;
    private int mVideoVisibleWidth = 0;
    private int mVideoSarNum = 0;
    private int mVideoSarDen = 0;

    private final String TAG = "VLCPlayer_";
    private static final int UPDATE_INTERVAL = 1000;
    private static final int JUMP_INTERVAL = 10 * 1000;
    private static final int JUMP_INTERVALFWD = 30 * 1000;
    public static final String SCREEN_RES_ORIGINAL = "Original";
    public static final String SCREEN_RES_4_3 = "4:3";
    public static final String SCREEN_RES_FIT_SCREEN = "FIT SCREEN";
    public static final String SCREEN_RES_FILL_SCREEN = "FILL SCREEN";
    public static final String SCREEN_RES_BEST_FIT_SCREEN = "BEST FIT SCREEN";
    public static String DEFAULTSELECTION = SCREEN_RES_ORIGINAL;
    public static int DEFAULTSELECTION_NO = 0;
    public ArrayList<String> aspectratiolist = new ArrayList<>();
    private Context mContext;
    private LibVLC mLibVLC;
    public MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceView mSubtitleSurface;
    private SurfaceHolder surfaceHolder;
    private Media media;
    private String mDataSource;
    private FrameLayout mVideoSurfaceFrame;
    private ProgressBar progressBar;
    private Uri mSource;
    private Map<String, String> mHeaders;
    IVLCVout vlcVout;
    private View mControlsFrame;
    private PopupWindow popupWindow;
    private TextView text_media_name;
    private TextView seek_position;
    private TextView seek_duration;
    private ImageView mBtnBack;
    private ImageView mBtnPlayPause;
    private ImageView mBtnRewind;
    private ImageView mBtnFastForward;
    private ImageView mBtnAspectRatio;
    private ImageView mAspect;
    private boolean isLiveContent = false;
    private AppCompatSeekBar mSeekBar;
    private Handler mSeekHandler = new Handler();
    public boolean isPrepared = false;
    private ImageView mAudioTrack;
    private ImageView mSubtitleTrack;
    private int retryCount = 1;
    private TextView text_error;
    private TextView text_aspect;
    private TextView txt_castplay;
    private Handler mRetryHandler = new Handler();
    private Handler mAspectHandler = new Handler();
    private Runnable runnableforprepare = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: for prepare");
            prepare();

        }
    };

    private Runnable runnableforAspect = new Runnable() {
        @Override
        public void run() {
            text_aspect.setVisibility(GONE);

        }
    };
    public VlcEventchangerLisener vlcEventchangerLisener;
    private OnLayoutChangeListener mOnLayoutChangeListener = null;
    private DisplayManager mDisplayManager;
    int cnt = 0;

    ArrayList<String> aspectList = new ArrayList<>();


    public interface VlcEventchangerLisener {
        public void OnEndedReached();

        public void OnStarted();

        public void OnStopped();

        public void OnVout();

    }

    public VLCPlayer(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }


    public VLCPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // initPlayer();

    }

    public VLCPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //initPlayer();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        UtilMethods.LogMethod(TAG + "_surface123_", "onFinishInflate");
        LayoutInflater li = LayoutInflater.from(getContext());
        View mSurfaceFrame = li.inflate(R.layout.layout_surface, this, false);
        mVideoSurfaceFrame = (FrameLayout) mSurfaceFrame.findViewById(R.id.player_surface_frame);
        mSurfaceView = (SurfaceView) mSurfaceFrame.findViewById(R.id.surfaceView);
        mSurfaceView.setZOrderMediaOverlay(true);
        progressBar = (ProgressBar) mSurfaceFrame.findViewById(R.id.progressBar123);
        text_error = (TextView) mSurfaceFrame.findViewById(R.id.text_error);
        text_aspect = (TextView) mSurfaceFrame.findViewById(R.id.text_aspect);
        txt_castplay = (TextView) mSurfaceFrame.findViewById(R.id.txt_castplay);

        mAspect = (ImageView) mSurfaceFrame.findViewById(R.id.mAspect);
        mAspect.setOnClickListener(this);

        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);

        mSubtitleSurface = (SurfaceView) mSurfaceFrame.findViewById(R.id.subtitle_surface);
        mSubtitleSurface.setZOrderMediaOverlay(true);
        mSubtitleSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        addView(mSurfaceFrame);

//        mClickFrame = new FrameLayout(getContext());
//        final int viewId = 11234;
//        mClickFrame.setId(viewId);
//        addView(mClickFrame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        //   mClickFrame.setOnTouchListener(mTouchListener);

        mControlsFrame = li.inflate(R.layout.controller, this, false);
        final LayoutParams controlsLp =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mControlsFrame);

        mBtnBack = (ImageView) mControlsFrame.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mBtnPlayPause = (ImageView) mControlsFrame.findViewById(R.id.btn_playPause);
        mBtnPlayPause.setOnClickListener(this);

        mBtnRewind = (ImageView) mControlsFrame.findViewById(R.id.btn_rewind);
        mBtnRewind.setOnClickListener(this);

        mBtnFastForward = (ImageView) mControlsFrame.findViewById(R.id.btn_fastForward);
        mBtnFastForward.setOnClickListener(this);

        mBtnAspectRatio = (ImageView) mControlsFrame.findViewById(R.id.btn_aspect);
        mBtnAspectRatio.setOnClickListener(this);

        mSeekBar = mControlsFrame.findViewById(R.id.seeker);
        mSeekBar.setOnSeekBarChangeListener(this);

        text_media_name = (TextView) mControlsFrame.findViewById(R.id.text_media_name);
        seek_position = (TextView) mControlsFrame.findViewById(R.id.seek_position);
        seek_duration = (TextView) mControlsFrame.findViewById(R.id.seek_duration);

        mAudioTrack = (ImageView) mControlsFrame.findViewById(R.id.btn_audio_track);
        mAudioTrack.setOnClickListener(this);

        mSubtitleTrack = (ImageView) mControlsFrame.findViewById(R.id.btn_subtitle_track);
        mSubtitleTrack.setOnClickListener(this);

        mControlsFrame.setVisibility(GONE);
        disableControls();
        if (!aspectratiolist.isEmpty()) {
            aspectratiolist.clear();
        }
        aspectratiolist.add(SCREEN_RES_ORIGINAL);
        aspectratiolist.add(SCREEN_RES_4_3);
        aspectratiolist.add(SCREEN_RES_FIT_SCREEN);
        aspectratiolist.add(SCREEN_RES_FILL_SCREEN);
        aspectratiolist.add(SCREEN_RES_BEST_FIT_SCREEN);

    }

    public void initPlayer() {
        final ArrayList<String> args = new ArrayList<>();

        if (BuildConfig.DEBUG) {
            args.add("-vvv"); // verbosity
        }

        args.add("--aout=opensles");
        args.add("--vout=android_display");
        args.add("--audio-time-stretch"); // time stretching
        args.add("--no-sub-autodetect-file");
        args.add("--swscale-mode=0");
        args.add("--network-caching=500");
        args.add("--no-drop-late-frames");
        args.add("--no-skip-frames");
        args.add("--avcodec-skip-frame");
        args.add("--avcodec-hw=any");
        //   args.add("--enable-gst-decode");


        mLibVLC = new LibVLC(mContext, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.setEventListener(eventListener);
        vlcVout = mMediaPlayer.getVLCVout();


    }

    public void initPlayer(VLCPlayer mVideoLayout, DisplayManager mDisplayManager, boolean ismute) {
        DEFAULTSELECTION = SCREEN_RES_ORIGINAL;
        DEFAULTSELECTION_NO = 0;
        this.mDisplayManager = mDisplayManager;
        final ArrayList<String> args = new ArrayList<>();

        if (BuildConfig.DEBUG) {
            args.add("-vvv"); // verbosity
        }

        args.add("--aout=opensles");
        args.add("--vout=android_display");
        args.add("--audio-time-stretch"); // time stretching
        args.add("--no-sub-autodetect-file");
        args.add("--swscale-mode=0");
        args.add("--network-caching=500");
        args.add("--no-drop-late-frames");
        args.add("--no-skip-frames");
        args.add("--avcodec-skip-frame");
        args.add("--avcodec-hw=any");
        //   args.add("--enable-gst-decode");


        mLibVLC = new LibVLC(mContext, args);
        mMediaPlayer = new MediaPlayer(mLibVLC, mVideoLayout);
        mMediaPlayer.setVolume(ismute ? 0 : 100);
        mMediaPlayer.setEventListener(eventListener);
        vlcVout = mMediaPlayer.getVLCVout();

    }

    public void initPlayer(VLCPlayer mVideoLayout, DisplayManager mDisplayManager) {
        DEFAULTSELECTION = SCREEN_RES_ORIGINAL;
        DEFAULTSELECTION_NO = 0;
        this.mDisplayManager = mDisplayManager;
        final ArrayList<String> args = new ArrayList<>();

        if (BuildConfig.DEBUG) {
            args.add("-vvv"); // verbosity
        }

        args.add("--aout=opensles");
        args.add("--vout=android_display");
        args.add("--audio-time-stretch"); // time stretching
        args.add("--no-sub-autodetect-file");
        args.add("--swscale-mode=0");
        args.add("--network-caching=500");
        args.add("--no-drop-late-frames");
        args.add("--no-skip-frames");
        args.add("--avcodec-skip-frame");
        args.add("--avcodec-hw=any");
        //   args.add("--enable-gst-decode");


        mLibVLC = new LibVLC(mContext, args);
        mMediaPlayer = new MediaPlayer(mLibVLC, mVideoLayout);
        mMediaPlayer.setEventListener(eventListener);
        vlcVout = mMediaPlayer.getVLCVout();
    }

    public int getVolume() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getVolume();
        return 0;
    }

    public void enableVolume() {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(100);
    }

    public void disableVolume() {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(0);
    }

    public void setSurfaceView(final SurfaceView surfaceView) {
        if (vlcVout != null) {
            vlcVout.setVideoView(surfaceView);
            if (mSubtitleSurface != null)
                vlcVout.setSubtitlesView(mSubtitleSurface);
            vlcVout.attachViews(this);

            if (mOnLayoutChangeListener == null) {
                mOnLayoutChangeListener = new OnLayoutChangeListener() {
                    private final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (mSurfaceView != null && mOnLayoutChangeListener != null)
                                updateVideoSurfaces();
                        }
                    };

                    @Override
                    public void onLayoutChange(View v, int left, int top, int right,
                                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                            handler.removeCallbacks(runnable);
                            handler.post(runnable);
                        }
                    }
                };
            }
            surfaceView.addOnLayoutChangeListener(mOnLayoutChangeListener);
            mMediaPlayer.setVideoTrackEnabled(true);
        }
    }

    public void playingoncast(boolean enable) {
        txt_castplay.setVisibility(enable ? VISIBLE : GONE);
        progressBar.setVisibility(GONE);
    }


    public void setSource(@NonNull Uri source, Map<String, String> headers, VlcEventchangerLisener vlcEventchangerLisener) {
        retryCount = 1;
        mSource = source;
        this.vlcEventchangerLisener = vlcEventchangerLisener;
        UtilMethods.LogMethod("media12333_qqqqqq", String.valueOf(source));
        mHeaders = headers;
        text_error.setVisibility(GONE);
        if (mMediaPlayer != null) {
            prepare();
        }
    }

    public void setSource(@NonNull Uri source, Map<String, String> headers, VlcEventchangerLisener vlcEventchangerLisener, boolean ismute) {
        retryCount = 1;
        mSource = source;
        this.vlcEventchangerLisener = vlcEventchangerLisener;
        UtilMethods.LogMethod("media12333_qqqqqq", String.valueOf(source));
        mHeaders = headers;
        text_error.setVisibility(GONE);
        if (mMediaPlayer != null) {
            if (ismute) {
                mMediaPlayer.setVolume(0);
            }
            prepare();
        }
    }

    @SuppressLint("LongLogTag")
    private void prepare() {
        UtilMethods.LogMethod(TAG, "prepare");
        if (mSource == null || mMediaPlayer == null || mSurfaceView == null)
            return;
        reset();
        setSurfaceView(mSurfaceView);
        setSourceInternal();
    }

    @SuppressLint("StaticFieldLeak")
    private void setSourceInternal() {
        if (mLibVLC == null && mMediaPlayer == null && mSource == null)
            return;

        try {
            UtilMethods.LogMethod("media12333_", String.valueOf(mSource));
            Media media = null;
            if (mSource != null && mHeaders != null && mSource.getScheme() != null &&
                    (mSource.getScheme().equals("http") || mSource.getScheme().equals("https"))) {
                UtilMethods.LogMethod(TAG + "player12341_ifff", String.valueOf("iffff"));
                media = new Media(mLibVLC, mSource);
                if (mHeaders.containsKey("User-Agent")) {
                    media.addOption("http-user-agent=" + mHeaders.get("User-Agent"));
                } else {
                    media.addOption("http-user-agent=" + "Purple VLC Player");

                }

            } else if (mSource != null && mSource.getScheme() != null &&
                    (mSource.getScheme().equals("http") || mSource.getScheme().equals("https"))) {
                media = new Media(mLibVLC, mSource);
            } else if (mSource != null && mSource.getScheme() != null &&
                    (mSource.getScheme().equals("file") && mSource.getPath() != null &&
                            mSource.getPath().contains("/android_assets/"))) {
                AssetFileDescriptor afd;
                afd = getContext().getAssets().openFd(mSource.toString().
                        replace("file:///android_assets/", ""));
                media = new Media(mLibVLC, afd);
                afd.close();
            } else if (mSource != null && mSource.getScheme() != null &&
                    mSource.getScheme().equals("asset")) {
                AssetFileDescriptor afd;
                afd = getContext().getAssets().openFd(mSource.toString().
                        replace("asset://", ""));
                media = new Media(mLibVLC, afd);
                afd.close();
            } else {
                UtilMethods.LogMethod("media12333_", "elsee");
                media = new Media(mLibVLC, mSource.toString());
            }

            if (media != null) {
                prepareAsync(media);
            }
        } catch (Exception e) {
            UtilMethods.LogMethod(TAG + "url12311_eee", String.valueOf(e));
            UtilMethods.LogMethod(TAG + "url12311_eee", String.valueOf(e.getMessage()));
        }

    }

    public String getDataSource() {
        return mDataSource;
    }

    @SuppressLint("StaticFieldLeak")
    public void prepareAsync(final Media media) throws IllegalStateException {
        if (mMediaPlayer == null) {
            return;
        }
        UtilMethods.LogMethod(TAG, "prepareAsync");
        new AsyncTask<Void, Void, Void>() {
            boolean success = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e(TAG, "onPreExecute: called progressBar visible");
                progressBar.setVisibility(VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    UtilMethods.LogMethod("exception123_", String.valueOf("try"));
                    mMediaPlayer.setMedia(media);
                    Log.e(TAG, "doInBackground: " + mMediaPlayer.getVideoScale());
                    mMediaPlayer.setVideoScale(MediaPlayer.ScaleType.SURFACE_16_9);
                    //   Log.e(TAG, "doInBackground__: " + mMediaPlayer.getVideoScale());
                    // TODO: 11/30/2020 release media asap https://code.videolan.org/videolan/libvlc-android-samples/-/blob/master/java_sample/src/main/java/org/videolan/javasample/JavaActivity.java
                    media.release();
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    UtilMethods.LogMethod("exception123_", String.valueOf(e));
                    success = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e(TAG, "onPreExecute: called progressBar GONE");
                progressBar.setVisibility(GONE);
                if (success) {
                    isPrepared = true;
                    mMediaPlayer.play();
                    UtilMethods.LogMethod("seek123_isLiveContent", String.valueOf(isLiveContent));
                    UtilMethods.LogMethod("seek123_isSeekable", String.valueOf(mMediaPlayer.isSeekable()));
                    if (!isLiveContent)
                        mSeekHandler.postDelayed(mUpdateCounters, 1000);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public void start() throws IllegalStateException {
        UtilMethods.LogMethod(TAG, "start");
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.play();
    }

    @SuppressLint("StaticFieldLeak")
    public void startwithmute() throws IllegalStateException {
        UtilMethods.LogMethod(TAG, "start");
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.setVolume(0);
        mMediaPlayer.play();
    }

    @SuppressLint("StaticFieldLeak")
    public void stop() throws IllegalStateException {
        if (mMediaPlayer == null) {
            return;
        }
        reset();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mMediaPlayer.stop();
                return null;
            }
        }.execute();

    }

    public void reset() throws IllegalStateException {
        if (vlcVout == null) {
            return;
        }
        //  stop();
        vlcVout.detachViews();
        onUpdateUiOnReset();
    }

    @SuppressLint("StaticFieldLeak")
    public void pause() throws IllegalStateException {
        if (mMediaPlayer == null) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                return null;
            }
        }.execute();

    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            return false;
        }
        return mMediaPlayer.isPlaying();
    }

    @SuppressLint("StaticFieldLeak")
    public void release() {
        Log.e(TAG, "release: called 1");
        if (mMediaPlayer == null || mLibVLC == null) {
            return;
        }
        try {
            Log.e(TAG, "release: called 2");
            stop();
            mRetryHandler.removeCallbacks(runnableforprepare);
            mAspectHandler.removeCallbacks(runnableforAspect);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Log.e(TAG, "release: called 3");
                    mMediaPlayer.release();
                    Log.e(TAG, "release: called 4");
                    mLibVLC.release();
                    Log.e(TAG, "release: called 5");
                    return null;
                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
            //UtilMethods.LogMethod("vlcPlayer123_eeeeeee", String.valueOf(e));
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (vlcVout == null) {
            return;

        }
        UtilMethods.LogMethod(TAG + "_surface123_", "surfaceCreated");
        vlcVout.detachViews();
        vlcVout.setVideoView(mSurfaceView);
        if (mSubtitleSurface != null)
            vlcVout.setSubtitlesView(mSubtitleSurface);
        vlcVout.attachViews(VLCPlayer.this);
    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, int i, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        UtilMethods.LogMethod(TAG + "_surface123_", "surfaceDestroyed");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            showexitalertpopup();

        } else if (view.getId() == R.id.btn_playPause) {
            if (mMediaPlayer == null) return;

            playpauseonclick();

        } else if (view.getId() == R.id.btn_fastForward) {
            moveForward();

        } else if (view.getId() == R.id.btn_rewind) {
            moveBackward();
        } else if (view.getId() == R.id.btn_audio_track) {
            getTrackInfo("audio");
        } else if (view.getId() == R.id.btn_subtitle_track) {
            getTrackInfo("subtitle");
        } else if (view.getId() == R.id.mAspect) {
            setAspectRatio(view);
        } else if (view.getId() == R.id.btn_aspect) {
            setAspectRatio(view);
        }
    }


    private void setAspectRatio(View v) {


        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        LayoutInflater inflater = (LayoutInflater)
                mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.layout_popup_recycler, null);
        RecyclerView recycler_popup = (RecyclerView) view.findViewById(R.id.recycler_popup);
        recycler_popup.setLayoutManager(new LinearLayoutManager(mContext));
        popupWindow = new PopupWindow(view, 450,
                LayoutParams.WRAP_CONTENT, true);
        final ArrayList<String> menuList = new ArrayList<>();
        menuList.add(SCREEN_RES_ORIGINAL);
        menuList.add(SCREEN_RES_4_3);
        menuList.add(SCREEN_RES_FIT_SCREEN);
        menuList.add(SCREEN_RES_FILL_SCREEN);
        menuList.add(SCREEN_RES_BEST_FIT_SCREEN);
        PopupDialogAdapter popupAdapter = new PopupDialogAdapter(mContext, menuList,
                new PopupDialogAdapter.RecyclerClickInterface() {
                    @Override
                    public void onClick(PopupDialogAdapter.PopupViewHolder holder, int position) {
                        String clickText = menuList.get(position);
                        DEFAULTSELECTION_NO = position;
                        DEFAULTSELECTION = clickText;
                        if (clickText.equalsIgnoreCase(SCREEN_RES_FIT_SCREEN)) {
                            setVideoScale(MediaPlayer.ScaleType.SURFACE_FIT_SCREEN);
                        } else if (clickText.equalsIgnoreCase(SCREEN_RES_FILL_SCREEN)) {
                            setVideoScale(MediaPlayer.ScaleType.SURFACE_FILL);
                        } else if (clickText.equalsIgnoreCase(SCREEN_RES_4_3)) {
                            setVideoScale(MediaPlayer.ScaleType.SURFACE_4_3);
                        } else if (clickText.equalsIgnoreCase(SCREEN_RES_BEST_FIT_SCREEN)) {
                            setVideoScale(MediaPlayer.ScaleType.SURFACE_BEST_FIT);
                        } else if (clickText.equalsIgnoreCase(SCREEN_RES_ORIGINAL)) {
                            setVideoScale(MediaPlayer.ScaleType.SURFACE_16_9);
                        }
                        popupWindow.dismiss();

                    }

                    @Override
                    public void onFocus(PopupDialogAdapter.PopupViewHolder holder, int position) {
                        showControl(null);
                    }
                });
        recycler_popup.setAdapter(popupAdapter);
        if (popupWindow != null && v != null) {
            popupWindow.showAsDropDown(v, 0, 0);
            //  popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }

    }


    public void autochnageaspectratio() {
        //DEFAULTSELECTION_NO
        int boundsize = aspectratiolist.size() - 1;
        DEFAULTSELECTION_NO++;
        if (DEFAULTSELECTION_NO > boundsize) {
            DEFAULTSELECTION_NO = 0;
            String clickText = aspectratiolist.get(DEFAULTSELECTION_NO);
            DEFAULTSELECTION = clickText;
            text_aspect.setText(clickText);
            text_aspect.setVisibility(VISIBLE);
            hideAspectmsg();

            Log.e(TAG, "autochnageaspectratio: 1:" + DEFAULTSELECTION_NO + " text -->" + clickText);
            if (clickText.equalsIgnoreCase(SCREEN_RES_FIT_SCREEN)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_FIT_SCREEN);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_FILL_SCREEN)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_FILL);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_4_3)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_4_3);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_BEST_FIT_SCREEN)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_BEST_FIT);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_ORIGINAL)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_16_9);
            }

        } else {
            String clickText = aspectratiolist.get(DEFAULTSELECTION_NO);
            DEFAULTSELECTION = clickText;
            text_aspect.setText(clickText);
            text_aspect.setVisibility(VISIBLE);
            hideAspectmsg();
            Log.e(TAG, "autochnageaspectratio: 2:" + DEFAULTSELECTION_NO + " text -->" + clickText);
            if (clickText.equalsIgnoreCase(SCREEN_RES_FIT_SCREEN)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_FIT_SCREEN);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_FILL_SCREEN)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_FILL);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_4_3)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_4_3);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_BEST_FIT_SCREEN)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_BEST_FIT);
            } else if (clickText.equalsIgnoreCase(SCREEN_RES_ORIGINAL)) {
                setVideoScale(MediaPlayer.ScaleType.SURFACE_16_9);
            }

        }


    }

    private void hideAspectmsg() {
        mAspectHandler.removeCallbacks(runnableforAspect);
        mAspectHandler.postDelayed(runnableforAspect, 2000);
    }

    public void showexitalertpopup() {
        final Dialog dialog = new Dialog(mContext, R.style.ThemeDialog);
        dialog.setContentView(R.layout.vlc_dialog_exit);
        final TextView text_exit = (TextView) dialog.findViewById(R.id.text_exit);
        final TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_yes);
        final TextView btn_no = (TextView) dialog.findViewById(R.id.btn_no);


        btn_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        btn_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                ((Activity) mContext).finish();
            }
        });


        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        dialog.show();
    }

    public void playpauseonclick() {
        Log.e(TAG, "playpauseonclick: ");

        if (mMediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        UtilMethods.LogMethod(TAG + "onKeyDown", "onKeyDownVLC");
        UtilMethods.LogMethod(TAG + "onKeyevent", String.valueOf(keyCode));
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                case KEYCODE_DPAD_LEFT:
                case KEYCODE_DPAD_RIGHT:
                    showControl(mBtnPlayPause);
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    moveForward();
                    break;
                //play pause
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    playpauseonclick();
                    break;
                //its back/ prev
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    moveBackward();
                    break;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private OnSwipeTouchListener mTouchListener =
            new OnSwipeTouchListener(false) {
                float diffTime = -1, finalTime = -1;
                int startVolume;
                int maxVolume;
                int startBrightness;
                int maxBrightness;

                @Override
                public void onMove(Direction dir, float diff) {
                }

                @Override
                public void onClick(View v) {
                    UtilMethods.LogMethod("oooooo", "onClick");
                    toggleControls();
                }

                @Override
                public void onDoubleTap(MotionEvent event) {

                }

                @Override
                public void onAfterMove() {
                }

                @Override
                public void onBeforeMove(Direction dir) {
                }
            };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            mMediaPlayer.setTime(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    MediaPlayer.EventListener eventListener = new MediaPlayer.EventListener() {
        @Override
        public void onEvent(MediaPlayer.Event event) {
            if (mMediaPlayer == null) return;
            switch (event.type) {
                case MediaPlayer.Event.MediaChanged:
                    UtilMethods.LogMethod(TAG + "vlcPlayer123_", String.valueOf("MediaChanged"));
                    // reset();
                    break;
                case MediaPlayer.Event.Opening:
                    UtilMethods.LogMethod(TAG + "vlcPlayer123_", String.valueOf("Opening"));
                    break;
                case MediaPlayer.Event.Buffering:
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("Buffering"));
                    if (progressBar != null) progressBar.setVisibility(VISIBLE);
                    break;
                case MediaPlayer.Event.Playing:
                    retryCount = 1;
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("Playing"));
                    if (progressBar != null) progressBar.setVisibility(GONE);
                    togglePlayPause();
                    break;
                case MediaPlayer.Event.Paused:
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("Paused"));
                    togglePlayPause();
                    break;
                case MediaPlayer.Event.Stopped:
                    if (vlcEventchangerLisener != null) {
                        vlcEventchangerLisener.OnStopped();
                    }
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("Stopped"));
                    if (isLiveContent) {
                        // prepare();
                    }
                    break;
                case MediaPlayer.Event.EndReached:
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("EndReached"));
                    if (vlcEventchangerLisener != null) {
                        vlcEventchangerLisener.OnEndedReached();
                    }
                    if (isLiveContent) {
                        // prepare();
                    } else {
                        ((Activity) mContext).finish();
                    }
                    break;
                case MediaPlayer.Event.EncounteredError:
                    UtilMethods.LogMethod("vlcPlayer123_EncounteredError", String.valueOf("EncounteredError"));
                    // if (retryCount <= 5) {
//                         Toast.makeText(mContext, "Please wait we are trying ("+retryCount+") to play channel",
//                                Toast.LENGTH_LONG).show();
                    // Toast.makeText(mContext, "Player/Playback error\n Retrying in 3s (" + retryCount + "/5)",
                    //        Toast.LENGTH_LONG).show();

//                        mRetryHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                prepare();
//                            }
//                        }, 3000);
                    //  } else {
                    // progressBar.setVisibility(View.GONE);
                    //  text_error.setVisibility(VISIBLE);
                    //}
                    Toast.makeText(mContext, "Please wait we are trying (" + retryCount + ") to play channel",
                            Toast.LENGTH_SHORT).show();
                    retryCount++;
                    mRetryHandler.postDelayed(runnableforprepare, 3000);
                    break;
                case MediaPlayer.Event.TimeChanged:
                    if (progressBar != null) progressBar.setVisibility(GONE);
                    break;
                case MediaPlayer.Event.PositionChanged:
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("PositionChanged"));
                    if (vlcEventchangerLisener != null) {
                        vlcEventchangerLisener.OnStarted();
                    }
                    break;
                case MediaPlayer.Event.SeekableChanged:
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("SeekableChanged"));
                    break;
                case MediaPlayer.Event.PausableChanged:
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("PausableChanged"));
                    break;
                case MediaPlayer.Event.Vout:
                    if (vlcEventchangerLisener != null) {
                        vlcEventchangerLisener.OnVout();
                    }
                    UtilMethods.LogMethod("vlcPlayer123_", String.valueOf("Vout"));
                    break;

            }

        }
    };

    private void onUpdateUiOnReset() {
        isPrepared = false;
        mSeekHandler.removeCallbacks(mUpdateCounters);
        hideControl();
    }

    public boolean isControllerShown() {
        return mControlsFrame.getVisibility() == VISIBLE;
    }

    public void toggleControls() {
        if (mControlsFrame.getVisibility() == VISIBLE) {
            hideControl();
        } else {
            showControl(mBtnPlayPause);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hideControl();
        }
    };

    private void showControl(View view) {
        if (mControlsFrame.getVisibility() == GONE || mControlsFrame.getVisibility() == INVISIBLE) {
            mControlsFrame.setVisibility(VISIBLE);
            if (view != null)
                view.requestFocus();
        }
        hidewithdelayed();
    }

    private void hidewithdelayed() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
    }

    public void hideControl() {
        mControlsFrame.setVisibility(GONE);
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        this.requestFocus();
    }

    private void togglePlayPause() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mBtnPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            mBtnPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setLiveContent(boolean isLiveContent) {
        this.isLiveContent = isLiveContent;
        if (isLiveContent) {
            // FIXME: 12/9/2020 enable below to scale in live tv
            // mAspect.setVisibility(VISIBLE);
            this.setFocusable(false);
            this.setOnTouchListener(null);
        } else {
            this.setFocusable(true);
            mAspect.setVisibility(GONE);
            this.setOnTouchListener(mTouchListener);
        }
    }

    public TextView getMediaNameTextView() {
        return text_media_name;
    }

    private void openPopupWindow(View v) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        LayoutInflater inflater = (LayoutInflater)
                mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.layout_popup_recycler, null);
        RecyclerView recycler_popup =
                (RecyclerView) view.findViewById(R.id.recycler_popup);
        recycler_popup.setLayoutManager(new LinearLayoutManager(mContext));
        popupWindow = new PopupWindow(view, 450,
                LayoutParams.WRAP_CONTENT, true);
        final ArrayList<String> menuList = new ArrayList<>();
        menuList.add("Video Track");
        menuList.add("Audio Track");
        menuList.add("Subtitle Track");
        PopupDialogAdapter popupAdapter = new PopupDialogAdapter(mContext, menuList,
                new PopupDialogAdapter.RecyclerClickInterface() {
                    @Override
                    public void onClick(PopupDialogAdapter.PopupViewHolder holder, int position) {
                        String clickText = menuList.get(position);
                        if (clickText.equalsIgnoreCase("Video Track")) {
                            getTrackInfo("video");
                        } else if (clickText.equalsIgnoreCase("Audio Track")) {
                            getTrackInfo("audio");
                        } else if (clickText.equalsIgnoreCase("Subtitle Track")) {
                            getTrackInfo("subtitle");
                        }

                        popupWindow.dismiss();

                    }

                    @Override
                    public void onFocus(PopupDialogAdapter.PopupViewHolder holder, int position) {

                    }
                });
        recycler_popup.setAdapter(popupAdapter);
        if (popupWindow != null && v != null) {
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    public void getTrackInfo(String type) {
        if (mMediaPlayer != null) {
            UtilMethods.LogMethod("track123_player_audio", String.valueOf(mMediaPlayer.getAudioTracks()));
            UtilMethods.LogMethod("track123_player_video", String.valueOf(mMediaPlayer.getVideoTracks()));
            UtilMethods.LogMethod("track123_player_spu", String.valueOf(mMediaPlayer.getSpuTracks()));
            MediaPlayer.TrackDescription[] tracks = null;
            int currentTrack = -1;
            if (type.equalsIgnoreCase("video")) {
                tracks = mMediaPlayer.getVideoTracks();
            } else if (type.equalsIgnoreCase("audio")) {
                tracks = mMediaPlayer.getAudioTracks();
                currentTrack = mMediaPlayer.getAudioTrack();
            } else if (type.equalsIgnoreCase("subtitle")) {
                tracks = mMediaPlayer.getSpuTracks();
                currentTrack = mMediaPlayer.getSpuTrack();
            }

            if (tracks != null) {
                int id = -1;
                ArrayList<TrackModel> track_info = new ArrayList<>();
                for (int i = 0; i < tracks.length; i++) {
                    MediaPlayer.TrackDescription description = tracks[i];
                    TrackModel trackModel = new TrackModel();
                    trackModel.setId(description.id);
                    trackModel.setName(description.name);
                    if (description.id == currentTrack) {
                        trackModel.setCurrentTrack(true);
                    } else {
                        trackModel.setCurrentTrack(false);
                    }
                    UtilMethods.LogMethod("track123_description_" + i,
                            String.valueOf(description.id + " ----" + description.name + " ----" +
                                    trackModel.isCurrentTrack()));
                    track_info.add(trackModel);
                }
                showTrackListDialog(type, track_info);
            } else {
                if (type.equals("subtitle")) {
                    Toast.makeText(mContext, "Subtitle not found.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showTrackListDialog(final String type, ArrayList<TrackModel> trackList) {
        if (trackList != null && mMediaPlayer != null) {
            CustomDialogs.showTrackInfoDialog(mContext, type, trackList,
                    new DialogInterface.trackRecyclerDialog() {
                        @Override
                        public void onTrackSelected(TrackModel trackModel) {
                            if (type.equalsIgnoreCase("video")) {
                                mMediaPlayer.setVideoTrack(trackModel.getId());
                            } else if (type.equalsIgnoreCase("audio")) {
                                mMediaPlayer.setAudioTrack(trackModel.getId());
                            } else if (type.equalsIgnoreCase("subtitle")) {
                                mMediaPlayer.setSpuTrack(trackModel.getId());
                            }
                        }
                    });
        }
    }

    public void disableControls() {
        mControlsFrame.setOnTouchListener(null);
    }

    public void moveForward() {
        Log.e(TAG, "moveForward: ");
        if (mMediaPlayer == null) return;
        long pos = (long) mMediaPlayer.getTime();
        final int dur = (int) mMediaPlayer.getLength();
        long forward_position = pos + JUMP_INTERVALFWD;
        if (forward_position > dur) {
            forward_position = dur;
        }
        mMediaPlayer.setTime(forward_position);

    }

    public void moveBackward() {
        Log.e(TAG, "moveBackward: ");
        if (mMediaPlayer == null) return;
        long pos = (long) mMediaPlayer.getTime();
        long backward_position = pos - JUMP_INTERVAL;
        if (backward_position <= 0) {
            backward_position = 0;
        }
        mMediaPlayer.setTime(backward_position);

    }

    private final Runnable mUpdateCounters = new Runnable() {
        @Override
        public void run() {
            UtilMethods.LogMethod("seek123_mUpdateCounters", String.valueOf("mUpdateCounters"));
            UtilMethods.LogMethod("seek123_mMediaPlayer", String.valueOf(mMediaPlayer));
            UtilMethods.LogMethod("seek123_mSeekHandler", String.valueOf(mSeekHandler));
            UtilMethods.LogMethod("seek123_media", String.valueOf(media));
            if (mMediaPlayer == null || mSeekHandler == null)
                return;
            long pos = (long) mMediaPlayer.getTime();
            final int dur = (int) mMediaPlayer.getLength();
            UtilMethods.LogMethod("seek123_pos", String.valueOf(pos));
            UtilMethods.LogMethod("seek123_dur", String.valueOf(dur));
            if (pos > dur) pos = dur;
            seek_position.setText(UtilMethods.convertMiliToTime(pos));
            seek_duration.setText(UtilMethods.convertMiliToTime(dur));
            int position = (int) pos;
            int duration = (int) dur;

            mSeekBar.setProgress(position);
//            animateProgression(position,mSeekBar);
            mSeekBar.setMax(duration);

//            if (mProgressCallback != null)
//                mProgressCallback.onVideoProgressUpdate((int) pos, dur);
            if (mSeekHandler != null)
                mSeekHandler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    private void animateProgression(int progress,  AppCompatSeekBar appCompatSeekBar) {
        ObjectAnimator animation =
                ObjectAnimator.ofInt(appCompatSeekBar, "progress", appCompatSeekBar.getProgress(), progress);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        appCompatSeekBar.clearAnimation();

    }

    public void setVideoScale(@NonNull MediaPlayer.ScaleType type) {
        mCurrentScaleType = type;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateVideoSurfaces();
            }
        });
       /* new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

            }
        });*/

    }

    public MediaPlayer.ScaleType getVideoScale() {
        return mCurrentScaleType;
    }

    private void changeMediaPlayerLayout(int displayW, int displayH) {
        if (mMediaPlayer.isReleased()) return;
        /* Change the video placement using the MediaPlayer API */
        switch (mCurrentScaleType) {
            case SURFACE_BEST_FIT:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_FIT_SCREEN:
            case SURFACE_FILL: {
                IMedia.VideoTrack vtrack = mMediaPlayer.getCurrentVideoTrack();
                if (vtrack == null)
                    return;
                final boolean videoSwapped = vtrack.orientation == IMedia.VideoTrack.Orientation.LeftBottom
                        || vtrack.orientation == IMedia.VideoTrack.Orientation.RightTop;
                if (mCurrentScaleType == MediaPlayer.ScaleType.SURFACE_FIT_SCREEN) {
                    int videoW = vtrack.width;
                    int videoH = vtrack.height;

                    if (videoSwapped) {
                        int swap = videoW;
                        videoW = videoH;
                        videoH = swap;
                    }
                    if (vtrack.sarNum != vtrack.sarDen)
                        videoW = videoW * vtrack.sarNum / vtrack.sarDen;

                    float ar = videoW / (float) videoH;
                    float dar = displayW / (float) displayH;

                    float scale;
                    if (dar >= ar)
                        scale = displayW / (float) videoW; /* horizontal */
                    else
                        scale = displayH / (float) videoH; /* vertical */
                    mMediaPlayer.setScale(scale);
                    mMediaPlayer.setAspectRatio(null);
                } else {
                    mMediaPlayer.setScale(0);
                    mMediaPlayer.setAspectRatio(!videoSwapped ? "" + displayW + ":" + displayH
                            : "" + displayH + ":" + displayW);
                }
                break;
            }
            case SURFACE_16_9:
                mMediaPlayer.setAspectRatio("16:9");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_4_3:
                mMediaPlayer.setAspectRatio("4:3");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_ORIGINAL:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(1);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    void updateVideoSurfaces() {
        if (mMediaPlayer == null || mMediaPlayer.isReleased() || !mMediaPlayer.getVLCVout().areViewsAttached())
            return;
        final boolean isPrimary = mDisplayManager == null || mDisplayManager.isPrimary();
        final Activity activity = isPrimary && mVideoSurfaceFrame.getContext() instanceof Activity ? (Activity) mVideoSurfaceFrame.getContext() : null;

        int sw;
        int sh;

        // get screen size
        if (activity != null) {
            sw = mVideoSurfaceFrame.getWidth();
            sh = mVideoSurfaceFrame.getHeight();
        } else if (mDisplayManager != null && mDisplayManager.getPresentation() != null && mDisplayManager.getPresentation().getWindow() != null) {
            sw = mDisplayManager.getPresentation().getWindow().getDecorView().getWidth();
            sh = mDisplayManager.getPresentation().getWindow().getDecorView().getHeight();
        } else return;

        // sanity check
        if (sw * sh == 0) {
            Log.e(TAG, "Invalid surface size");
            return;
        }

        mMediaPlayer.getVLCVout().setWindowSize(sw, sh);

        /* We will setup either the videoSurface or the videoTexture */
        View videoView = mSurfaceView;

        ViewGroup.LayoutParams lp = videoView.getLayoutParams();
        if (mVideoWidth * mVideoHeight == 0 || (AndroidUtil.isNougatOrLater && activity != null && activity.isInPictureInPictureMode())) {
            /* Case of OpenGL vouts: handles the placement of the video using MediaPlayer API */
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(lp);
            if (mSubtitleSurface != null)
                mSubtitleSurface.setLayoutParams(lp);
            lp = mVideoSurfaceFrame.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurfaceFrame.setLayoutParams(lp);
            if (mVideoWidth * mVideoHeight == 0) changeMediaPlayerLayout(sw, sh);
            return;
        }

        if (lp.width == lp.height && lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            /* We handle the placement of the video using Android View LayoutParams */
            mMediaPlayer.setAspectRatio(null);
            mMediaPlayer.setScale(0);
        }

        double dw = sw, dh = sh;
        final boolean isPortrait = isPrimary && mVideoSurfaceFrame.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // compute the aspect ratio
        double ar, vw;
        if (mVideoSarDen == mVideoSarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double) mVideoSarNum / mVideoSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;
        switch (mCurrentScaleType) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_SCREEN:
                if (dar >= ar)
                    dh = dw / ar; /* horizontal */
                else
                    dw = dh * ar; /* vertical */
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }

        // set display size
        lp.width = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        videoView.setLayoutParams(lp);
        if (mSubtitleSurface != null) mSubtitleSurface.setLayoutParams(lp);

        videoView.invalidate();
        if (mSubtitleSurface != null) mSubtitleSurface.invalidate();
    }

    @Override
    public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth,
                                 int visibleHeight, int sarNum, int sarDen) {
        UtilMethods.LogMethod("new_Video123_", "onNewVideoLayout");
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mVideoSarNum = sarNum;
        mVideoSarDen = sarDen;
        updateVideoSurfaces();
        Log.e(TAG, "mVideoVisibleWidth: " + mVideoWidth);
    }
}
