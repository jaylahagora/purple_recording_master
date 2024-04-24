package com.smartrecording.recordingplugin.adapter;

import static com.purple.iptv.player.utils.UtilMethods.convertmilisectodattime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.purple.iptv.player.common.CustomDialogs;
import com.purple.iptv.player.common.CustomInterface;
import com.purple.iptv.player.utils.Config;
import com.purple.iptv.player.utils.UtilConstant;
import com.smartrecording.recordingplugin.activity.VideoPlayerActivity;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.database.DatabaseRoom;
import com.smartrecording.recordingplugin.model.BaseFakeModel;
import com.smartrecording.recordingplugin.model.BaseModel;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.model.PlayerModel;
import com.smartrecording.recordingplugin.model.RecordingModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;
import com.smartrecording.recordingplugin.service.RecordingService;
import com.smartrecording.recordingplugin.service.RecordingService2;
import com.xunison.recordingplugin.BuildConfig;
import com.xunison.recordingplugin.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Set;


public class RecordingMultiAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecordingMultiAdapter";
    adapternotify listener;

    public interface adapternotify {

        public void notifydatachanged();


    }

    private ArrayList<BaseFakeModel> baseFakeModelArrayList;
    ArrayList<BaseModel> mListnewfortoday = new ArrayList<>();
    ArrayList<BaseModel> mListnewfortommorow = new ArrayList<>();
    ArrayList<BaseModel> mListnewforfuture = new ArrayList<>();
    ArrayList<BaseModel> mListnewforalreadyrecorded = new ArrayList<>();
    Context mContext;
    ColorModel colorModel;


    public RecordingMultiAdapter(ArrayList<BaseFakeModel> currentarray, Context recordingFragment, ArrayList<BaseModel> mListnewfortoday, ArrayList<BaseModel> mListnewfortommorow, ArrayList<BaseModel> mListnewforfuture, ArrayList<BaseModel> mListnewforalreadyrecorded, ColorModel colorModel, adapternotify listener) {
        mContext = recordingFragment;
        this.baseFakeModelArrayList = currentarray;
        this.mListnewfortoday = mListnewfortoday;
        this.mListnewfortommorow = mListnewfortommorow;
        this.mListnewforfuture = mListnewforfuture;
        this.mListnewforalreadyrecorded = mListnewforalreadyrecorded;
        this.colorModel = colorModel;

        this.listener = listener;
        System.out.println("window width" + windowWidth);
//        Log.e(TAG, "RecordingMultiAdapter: called");
//        Log.e(TAG, "RecordingMultiAdapter: currentarray:" + currentarray.size());
//        Log.e(TAG, "RecordingMultiAdapter:  .getViewType():" + currentarray.get(0));
        try {
            if (recordingFragment instanceof Activity && !((Activity) mContext).isDestroyed()) {
                DisplayMetrics metrics = new DisplayMetrics();
                ((Activity) recordingFragment).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                windowHeight = metrics.heightPixels;
                windowWidth = metrics.widthPixels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ItemViewWrapper extends FrameLayout {
        private RecordingAdapter adapter;
        private RecordingAdapter adapter1;
        private RecordingAdapter adapter2;
        private RecordingAdapter adapter3;
        LinearLayout rcldashvoardvideos;


        TextView txtfreelable;
        TextView txthowmuch;
        TextView text_no_data;
        VerticalGridView recyclerView;
        private int currentSelectedPosition;
        private PopupWindow popupWindow;

        public ItemViewWrapper(final RecordingMultiAdapter adapterPortraitVideoList, @NonNull final Context context, int i) {
            super(context);
            switch (i) {
                case 100:
                    //today
                    inflate(context, R.layout.layoutrecording, this);
                    rcldashvoardvideos = findViewById(R.id.rcldashvoardvideos);
                    txtfreelable = findViewById(R.id.txtfreelable);
                    txthowmuch = findViewById(R.id.txthowmuch);
                    recyclerView = findViewById(R.id.recyclerView);
                    text_no_data = findViewById(R.id.text_no_data);
                    txthowmuch.setText(mListnewfortoday.size() + " recordings");
                    //  txthowmuch.requestFocus();
                    txtfreelable.setText("Today");
                    if (mListnewfortoday != null && !mListnewfortoday.isEmpty()) {
                        text_no_data.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Collections.sort(mListnewfortoday, new Comparator<BaseModel>() {
                            @Override
                            public int compare(BaseModel o1, BaseModel o2) {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
                                try {
                                    Date date1 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o1).getStartTime())));
                                    Date date2 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o2).getStartTime())));
                                    return date1.compareTo(date2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    if (BuildConfig.DEBUG)
                                        Log.e(TAG, "compare: catch" + e.getMessage());
                                }
                                return 0;
                            }

                        });
                        adapter = new RecordingAdapter(mContext, mListnewfortoday, colorModel,
                                new RecordingAdapter.adapterInterface() {
                                    @Override
                                    public void onClick(RecyclerView.ViewHolder holder,
                                                        int position) {
                                        onItemClick(mListnewfortoday, holder, position, adapter, recyclerView);
                                    }

                                    @Override
                                    public void onLongPressed(RecyclerView.ViewHolder holder,
                                                              int position) {
                                        BaseModel baseModel = mListnewfortoday.get(position);
                                        View clickedView = null;
                                        if (baseModel instanceof RecordingScheduleModel) {
                                            clickedView = ((RecordingAdapter.
                                                    RecordingScheduleViewHolder) holder).itemView;
                                        } else if (baseModel instanceof RecordingModel) {
                                            clickedView = ((RecordingAdapter.
                                                    RecordingViewHolder) holder).itemView;
                                        }
                                        if (clickedView != null)
                                            openPopup(clickedView, mListnewfortoday, position, adapter, recyclerView);
                                    }

                                }, "Today");

                        recyclerView.setOnChildViewHolderSelectedListener(
                                new OnChildViewHolderSelectedListener() {
                                    @Override
                                    public void onChildViewHolderSelected(RecyclerView parent,
                                                                          RecyclerView.ViewHolder child,
                                                                          int position, int subposition) {
                                        super.onChildViewHolderSelected(parent, child, position, subposition);
                                        currentSelectedPosition = position;
                                    }
                                });
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(adapter);
                    } else {
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText("No recording found for Today");
                        recyclerView.setVisibility(View.GONE);

                    }
                    break;
                case 101:
                    //tommorow
                    inflate(context, R.layout.layoutrecording, this);
                    txtfreelable = findViewById(R.id.txtfreelable);
                    txthowmuch = findViewById(R.id.txthowmuch);
                    txthowmuch.setText(mListnewfortommorow.size() + " recordings");
                    recyclerView = findViewById(R.id.recyclerView);
                    txtfreelable.setText("Tomorrow");
                    text_no_data = findViewById(R.id.text_no_data);
                    if (mListnewfortommorow != null && !mListnewfortommorow.isEmpty()) {
                        text_no_data.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Collections.sort(mListnewfortommorow, new Comparator<BaseModel>() {
                            @Override
                            public int compare(BaseModel o1, BaseModel o2) {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
                                try {
                                    Date date1 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o1).getStartTime())));
                                    Date date2 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o2).getStartTime())));
                                    return date1.compareTo(date2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    if (BuildConfig.DEBUG)
                                        Log.e(TAG, "compare: catch" + e.getMessage());
                                }
                                return 0;
                            }

                        });
                        adapter1 = new RecordingAdapter(mContext, mListnewfortommorow,
                                colorModel, new RecordingAdapter.adapterInterface() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder,
                                                int position) {
                                onItemClick(mListnewfortommorow, holder, position, adapter1, recyclerView);
                            }

                            @Override
                            public void onLongPressed(RecyclerView.ViewHolder holder,
                                                      int position) {
                                BaseModel baseModel = mListnewfortommorow.get(position);
                                View clickedView = null;
                                if (baseModel instanceof RecordingScheduleModel) {
                                    clickedView = ((RecordingAdapter.
                                            RecordingScheduleViewHolder) holder).itemView;
                                } else if (baseModel instanceof RecordingModel) {
                                    clickedView = ((RecordingAdapter.
                                            RecordingViewHolder) holder).itemView;
                                }
                                if (clickedView != null)
                                    openPopup(clickedView, mListnewfortommorow, position, adapter1, recyclerView);
                            }

                        }, "");

                        recyclerView.setOnChildViewHolderSelectedListener(
                                new OnChildViewHolderSelectedListener() {
                                    @Override
                                    public void onChildViewHolderSelected(RecyclerView parent,
                                                                          RecyclerView.ViewHolder child,
                                                                          int position, int subposition) {
                                        super.onChildViewHolderSelected(parent, child, position, subposition);
                                        currentSelectedPosition = position;
                                    }
                                });
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(adapter1);
                    } else {
                        text_no_data.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        text_no_data.setText("No recording found for Tomorrow");
                    }
                    break;
                case 102:
                    //future
                    inflate(context, R.layout.layoutrecording, this);
                    txtfreelable = findViewById(R.id.txtfreelable);
                    txthowmuch = findViewById(R.id.txthowmuch);
                    recyclerView = findViewById(R.id.recyclerView);

                    text_no_data = findViewById(R.id.text_no_data);
                    if (mListnewforfuture != null && !mListnewforfuture.isEmpty()) {
                        txthowmuch.setText(mListnewforfuture.size() + " recordings");
                        txtfreelable.setText("Future");
                        text_no_data.setVisibility(View.GONE);
                        Collections.sort(mListnewforfuture, new Comparator<BaseModel>() {
                            @Override
                            public int compare(BaseModel o1, BaseModel o2) {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
                                try {
                                    Date date1 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o1).getStartTime())));
                                    Date date2 = format.parse(convertmilisectodattime(String.valueOf(((RecordingScheduleModel) o2).getStartTime())));
                                    return date1.compareTo(date2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    if (BuildConfig.DEBUG)
                                        Log.e(TAG, "compare: catch" + e.getMessage());
                                }
                                return 0;
                            }

                        });
                        adapter2 = new RecordingAdapter(mContext, mListnewforfuture,
                                colorModel, new RecordingAdapter.adapterInterface() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder,
                                                int position) {
                                onItemClick(mListnewforfuture, holder, position, adapter2, recyclerView);
                            }

                            @Override
                            public void onLongPressed(RecyclerView.ViewHolder holder,
                                                      int position) {
                                BaseModel baseModel = mListnewforfuture.get(position);
                                View clickedView = null;
                                if (baseModel instanceof RecordingScheduleModel) {
                                    clickedView = ((RecordingAdapter.
                                            RecordingScheduleViewHolder) holder).itemView;
                                } else if (baseModel instanceof RecordingModel) {
                                    clickedView = ((RecordingAdapter.
                                            RecordingViewHolder) holder).itemView;
                                }
                                if (clickedView != null)
                                    openPopup(clickedView, mListnewforfuture, position, adapter2, recyclerView);
                            }

                        }, "");


                        recyclerView.setOnChildViewHolderSelectedListener(
                                new OnChildViewHolderSelectedListener() {
                                    @Override
                                    public void onChildViewHolderSelected(RecyclerView parent,
                                                                          RecyclerView.ViewHolder child,
                                                                          int position, int subposition) {
                                        super.onChildViewHolderSelected(parent, child, position, subposition);
                                        currentSelectedPosition = position;
                                    }
                                });
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(adapter2);

                    } else {
                        recyclerView.setVisibility(GONE);
                        txtfreelable.setVisibility(GONE);
                        txthowmuch.setVisibility(GONE);

                    }
                    break;
                case 103:
                    inflate(context, R.layout.layoutrecording, this);
                    txtfreelable = findViewById(R.id.txtfreelable);
                    txthowmuch = findViewById(R.id.txthowmuch);
                    txthowmuch.setText(mListnewforalreadyrecorded.size() + " recordings");
                    recyclerView = findViewById(R.id.recyclerView);
                    txtfreelable.setText("Recorded");
                    text_no_data = findViewById(R.id.text_no_data);
                    if (mListnewforalreadyrecorded != null && !mListnewforalreadyrecorded.isEmpty()) {
                        text_no_data.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter3 = new RecordingAdapter(mContext, mListnewforalreadyrecorded,
                                colorModel, new RecordingAdapter.adapterInterface() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder,
                                                int position) {
                                onItemClick(mListnewforalreadyrecorded, holder, position, adapter3, recyclerView);
                            }

                            @Override
                            public void onLongPressed(RecyclerView.ViewHolder holder,
                                                      int position) {
                                BaseModel baseModel = mListnewforalreadyrecorded.get(position);
                                View clickedView = null;
                                if (baseModel instanceof RecordingScheduleModel) {
                                    clickedView = ((RecordingAdapter.
                                            RecordingScheduleViewHolder) holder).itemView;
                                } else if (baseModel instanceof RecordingModel) {
                                    clickedView = ((RecordingAdapter.
                                            RecordingViewHolder) holder).itemView;
                                }
                                if (clickedView != null)
                                    openPopup(clickedView, mListnewforalreadyrecorded, position, adapter3, recyclerView);
                            }

                        }, "");

                        recyclerView.setOnChildViewHolderSelectedListener(
                                new OnChildViewHolderSelectedListener() {
                                    @Override
                                    public void onChildViewHolderSelected(RecyclerView parent,
                                                                          RecyclerView.ViewHolder child,
                                                                          int position, int subposition) {
                                        super.onChildViewHolderSelected(parent, child, position, subposition);
                                        currentSelectedPosition = position;
                                    }
                                });
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(adapter3);
                    } else {
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText("No Recording Found");
                        recyclerView.setVisibility(View.GONE);
                    }
                    break;

            }

        }

        private void onItemClick(ArrayList<BaseModel> mList, RecyclerView.ViewHolder holder,
                                 int position, RecordingAdapter adapter, VerticalGridView recyclerView) {
            BaseModel baseModel = mList.get(position);
            Log.e(TAG, "onItemClick: baseModel:" + baseModel.toString());
            if (baseModel instanceof RecordingScheduleModel) {
                View clickedView = ((RecordingAdapter.RecordingScheduleViewHolder) holder).itemView;
                openPopup(clickedView, mList, position, adapter, recyclerView);
            } else if (baseModel instanceof RecordingModel) {
                RecordingModel recordingModel = (RecordingModel) baseModel;
                playRecording(recordingModel);
            }
        }

        private void openPopup(View longPressedView, final ArrayList<BaseModel> mList,
                               final int adapterPosition, final RecordingAdapter adapter, final VerticalGridView recyclerView) {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View view = inflater.inflate(R.layout.layout_popup, null);
            RecyclerView recycler_popup =
                    (RecyclerView) view.findViewById(R.id.recycler_popup);
            recycler_popup.setLayoutManager(new LinearLayoutManager(mContext));
            popupWindow = new PopupWindow(view, (int) mContext.getResources().
                    getDimension(R.dimen.popup_dialog_width),
                    LayoutParams.WRAP_CONTENT, true);
            final BaseModel baseModel = mList.get(adapterPosition);
            Log.e(TAG, "openPopup: baseModel:" + baseModel);
            final ArrayList<String> menuList = new ArrayList<>();

            String name = "";
            if (baseModel instanceof RecordingScheduleModel) {
                menuList.add(mContext.getString(R.string.str_delete));
                name = ((RecordingScheduleModel) baseModel).getShowName();
                Log.e(TAG, "openPopup: RecordingScheduleModel baseModel:" + name);
            } else if (baseModel instanceof RecordingModel) {
                menuList.add(mContext.getString(R.string.longpressed_popup_play));
                menuList.add(mContext.getString(R.string.str_delete));
                name = ((RecordingModel) baseModel).getFileName();
                Log.e(TAG, "openPopup: RecordingModel baseModel:" + name);
                String status = ((RecordingModel) baseModel).getStatus();
                if (status != null && status.equals(Config.RECORDING_RECORDING)) {
                    menuList.add(mContext.getString(R.string.popup_stop_recording));
                }
            }
            menuList.add(mContext.getString(R.string.popup_close));
            final String finalName = name;
            PopupAdapter popupAdapter = new PopupAdapter(mContext, menuList,
                    new PopupAdapter.BluetoothClickInterface() {
                        @Override
                        public void onClick(PopupAdapter.PopupViewHolder holder, int position) {
                            String clickText = menuList.get(position);
                            if (clickText.equals(mContext.getString(R.string.longpressed_popup_play))) {
                                playRecording((RecordingModel) baseModel);
                            } else if (clickText.equals(mContext.getString(R.string.str_delete))) {
                                deleteAlertDialog(finalName, mList, adapterPosition, adapter, recyclerView);
                            } else if (clickText.equals(mContext.getString(R.string.popup_stop_recording))) {
                                Set<String> currently_recording_list = MyApplication.getInstance().
                                        getPrefManager().
                                        getCurrentlyRecordingList();
                                currently_recording_list.remove(((RecordingModel) baseModel).getFileName());
                                MyApplication.getInstance().getPrefManager().
                                        setCurrentlyRecordingList(currently_recording_list);
                                UtilConstant.ToastI(mContext, "Recording has stopped.");

                                RecordingService.shouldContinue_s1 = false;
                                //  loadData();
                            }
                            popupWindow.dismiss();

                        }
                    });
            recycler_popup.setAdapter(popupAdapter);
            if (popupWindow != null && longPressedView != null) {
                popupWindow.showAsDropDown(longPressedView,
                        longPressedView.getWidth() / 2, -longPressedView.getHeight());
            }

        }

        private void playRecording(RecordingModel recordingModel) {
            PlayerModel playerModel = new PlayerModel();
            playerModel.setMedia_name(recordingModel.getFileName());
            playerModel.setMedia_url(recordingModel.getFilePath());
            Intent i = new Intent(mContext, VideoPlayerActivity.class);
            i.putExtra("player_model", playerModel);
            mContext.startActivity(i);
        }

        private void deleteAlertDialog(String name, final ArrayList<BaseModel> mList,
                                       final int adapterPosition, final RecordingAdapter adapter, final VerticalGridView recyclerView) {

            CustomDialogs.showDeleteAlertDialog(mContext, name,
                    new CustomInterface.deleteAlertInterface() {
                        @Override
                        public void onYes(Dialog dialog) {
                            BaseModel baseModel = mList.get(adapterPosition);
                            if (baseModel instanceof RecordingScheduleModel) {
                                if (adapter != null) {
                                    mList.remove(adapterPosition);
                                    adapter.notifyDataSetChanged();
                                }
                                removeScheduleRecording((RecordingScheduleModel) baseModel);
                                //  RecordingMultiAdapter.this.notifyDataSetChanged();
                                if (listener != null) {
                                    listener.notifydatachanged();
                                }


                            } else if (baseModel instanceof RecordingModel) {
                                // boolean isDeleted = deleteFile((RecordingModel) baseModel);
                                boolean isDeleted1 = del(mContext, ((RecordingModel) baseModel));
                                Log.e(TAG, "onYes: isDeleted1:" + isDeleted1);
                                if (adapter != null && isDeleted1) {
                                    mList.remove(adapterPosition);
                                    adapter.notifyDataSetChanged();
                                    if (listener != null) {
                                        listener.notifydatachanged();
                                    }
                                    UtilConstant.ToastS(mContext, "Successfully Deleted ");
                                } else {
                                    UtilConstant.ToastE(mContext, "Can't delete this file...");

                                }
                            }

                            if (mList.size() > 0) {
//                                recycler_category.setVisibility(View.VISIBLE);
//                                text_no_data.setVisibility(View.GONE);
                            } else {
//                                recycler_category.setVisibility(View.GONE);
//                                text_no_data.setVisibility(View.VISIBLE);
//                                text_no_data.setText(mContext.getString(R.string.str_error_no_recording_found));
//                                text_no_data.requestFocus();
                            }

                        }
                    });


        }

        @SuppressLint("StaticFieldLeak")
        private void removeScheduleRecording(final RecordingScheduleModel scheduleModel) {
            if (scheduleModel != null) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        DatabaseRoom.with(mContext).deleteScheduleRecording(scheduleModel.getUid());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Log.e(TAG, "onPostExecute: scheduleModel.getUid():" + scheduleModel.getUid());
                        AlarmManager alarmManager =
                                (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                        Intent myIntent = new Intent(mContext, scheduleModel.getServicenumber().equals("s1") ? RecordingService.class : RecordingService2.class);


                        PendingIntent contentIntent = null;
                        if (Build.VERSION.SDK_INT >= 31) {
                            contentIntent = PendingIntent.getService(
                                    mContext, (int)  scheduleModel.getUid(), myIntent,
                                    PendingIntent.FLAG_MUTABLE);

                        } else {
                            contentIntent = PendingIntent.getService(mContext, (int) scheduleModel.getUid(), myIntent, PendingIntent.FLAG_IMMUTABLE);
                        }

                        if (alarmManager != null)
                            alarmManager.cancel(contentIntent);


                    }
                }.execute();
            }
        }

        public boolean del(final Context context, RecordingModel recordingModel) {
            String external_url = MyApplication.getInstance().getPrefManager().
                    getExternalStorageUri();
            Log.e(TAG, "del: external_url" + external_url);
            if (external_url != null && !recordingModel.getFilePath().contains("emulated")) {
                File dirFile = new File(external_url);
                if (dirFile.getAbsolutePath().contains("emulated") && dirFile.exists()) {
                    if (recordingModel != null && recordingModel.getFilePath() != null) {
                        File toDeleteFile = new File(recordingModel.getFilePath());
                        if (toDeleteFile.exists()) {
                            return toDeleteFile.delete();
                        }
                    }
                } else {
                    DocumentFile document = DocumentFile.fromTreeUri(context,
                            Uri.parse(external_url));
                    if (document != null) {
                        Log.e(TAG, "delete: 1");
                        try {
                            Log.e(TAG, "delete: 2");
                            DocumentFile apkFile = document.findFile(recordingModel.getFileName());
                            Log.e(TAG, "delete: 3");
                            return apkFile.delete();
                        } catch (Exception e) {
                            Log.e(TAG, "delete: 4");
                            return false;
                        }
                    }
                }

                Log.e(TAG, "delete: 5");
            } else {
                if (recordingModel != null && recordingModel.getFilePath() != null) {
                    File toDeleteFile = new File(recordingModel.getFilePath());
                    if (toDeleteFile.exists()) {
                        return toDeleteFile.delete();
                    }
                }

            }
            return false;
        }


    }


    int windowHeight, windowWidth, defaultMargin = 150;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewWrapper(new ItemViewWrapper(this, this.mContext, viewType));
    }


    @Override
    public int getItemViewType(int position) {


        // Log.e(TAG, "getItemViewType:qqq " + ((BaseFakeModel) this.baseFakeModelArrayList.get(position)).getViewType());
        // Log.e(TAG, "getItemViewType:aaa " + position);
        // //free videos
        return ((BaseFakeModel) this.baseFakeModelArrayList.get(position)).getViewType();

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        ItemViewWrapper itemViewWrapper = (ItemViewWrapper) holder.itemView;
        // Log.e(TAG, "onBindViewHolder: listPosition" + listPosition);
        if (holder.getItemViewType() == 333) {
            //  itemViewWrapper.bind(listPosition);
        } else if (holder.getItemViewType() == 111) {


        }

    }


    @Override
    public int getItemCount() {
        //Log.e(TAG, "getItemCount: " + baseFakeModelArrayList.size());
        return baseFakeModelArrayList.size();
    }


}