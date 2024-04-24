package com.smartrecording.recordingplugin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purple.iptv.player.utils.UtilMethods;
import com.xunison.recordingplugin.BuildConfig;
import com.xunison.recordingplugin.R;
import com.smartrecording.recordingplugin.model.BaseModel;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.model.RecordingModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;
import com.smartrecording.recordingplugin.utils.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.purple.iptv.player.utils.UtilMethods.gettimewithplus1mins;
import static com.smartrecording.recordingplugin.fragment.RecordingFragment.getSelectorDrawableforcover;

public class RecordingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_SIMPLE = 210211;
    private static final int VIEW_SCHEDULE = 210212;
    Context mContext;
    ArrayList<BaseModel> list;
    LayoutInflater inflater;
    adapterInterface listener;
    private SimpleDateFormat simpleTimeFormat;
    private SimpleDateFormat simpleDateFormat;
    ColorModel colorModel;
    String which = "";

    public RecordingAdapter(Context context, ArrayList<BaseModel> list,
                            ColorModel colorModel, adapterInterface listener, String s) {
        this.mContext = context;
        this.list = list;
        this.colorModel = colorModel;
        this.which = s;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
        simpleTimeFormat = CommonMethods.getEPGTimeFormat(mContext);
        simpleDateFormat = CommonMethods.getEPGDateFormat(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof RecordingScheduleModel) {
            return VIEW_SCHEDULE;
        } else {
            return VIEW_SIMPLE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_SCHEDULE) {
            View v = inflater.inflate(R.layout.cardview_recording_schedulenew, parent, false);
            return new RecordingScheduleViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.cardview_recordingnew, parent, false);
            return new RecordingViewHolder(v);
        }
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 final int position) {
        UtilMethods.LogMethod("recy1212_", "onBindViewHolder");
        BaseModel baseModel = list.get(position);
        if (holder instanceof RecordingViewHolder) {
            final RecordingViewHolder recordingHolder = (RecordingViewHolder) holder;
            RecordingModel model = (RecordingModel) baseModel;
            recordingHolder.text_name.setText(model.getFileName());
            recordingHolder.text_size.setText(model.getFileSize());
            recordingHolder.text_time.setText(model.getFileDownloadDate());
            recordingHolder.text_status.setText(model.getStatus());

            recordingHolder.llcover.setBackground(getSelectorDrawableforcover(colorModel.getUnselected_categoryList(), colorModel.getSelected_categoryList(), colorModel.getSelected_color()));

        } else if (holder instanceof RecordingScheduleViewHolder) {
            final RecordingScheduleViewHolder recordingScheduleHolder =
                    (RecordingScheduleViewHolder) holder;
            RecordingScheduleModel model = (RecordingScheduleModel) baseModel;
            String up = "";
            if (position == 0 && !which.equals("") && which.equalsIgnoreCase("Today")) {
                up = "Upcoming Recording";
            } else {
                up = model.getStatus();
            }
            String time = String.format("%s - %s",
                    simpleTimeFormat.format( model.getStartTime()),
                    simpleTimeFormat.format(gettimewithplus1mins(model.getEndTime())));
            String date = simpleDateFormat.format(model.getEndTime());
            //  recordingScheduleHolder.text_time.setText(time + " (" + date + ")");
            recordingScheduleHolder.text_time.setText(time + " " + (BuildConfig.DEBUG ? model.getServicenumber() : ""));
            recordingScheduleHolder.text_time.setSelected(true);
            Spannable word = new SpannableString(model.getShowName());
            word.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Spannable wordTwo = new SpannableString(" / " + model.getChannelName());
            wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#80ffffff")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            recordingScheduleHolder.text_name.setText(word);
            recordingScheduleHolder.text_name.append(wordTwo);
            recordingScheduleHolder.text_name.setSelected(true);
            recordingScheduleHolder.text_status.setText(up);
            recordingScheduleHolder.llcover.setBackground(getSelectorDrawableforcover(colorModel.getUnselected_categoryList(), colorModel.getSelected_categoryList(), colorModel.getSelected_color()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(holder, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongPressed(holder, position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class RecordingViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_name;
        private final TextView text_size;
        private final TextView text_time;
        private final TextView text_status;
        LinearLayout llcover;

        public RecordingViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_size = (TextView) itemView.findViewById(R.id.text_size);
            text_time = (TextView) itemView.findViewById(R.id.text_time);
            text_status = (TextView) itemView.findViewById(R.id.text_status);
            llcover = itemView.findViewById(R.id.llcover);
        }
    }

    public class RecordingScheduleViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_name;
        private final TextView text_status;
        private final TextView text_time;
        LinearLayout llcover;

        public RecordingScheduleViewHolder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_status = (TextView) itemView.findViewById(R.id.text_status);
            text_time = (TextView) itemView.findViewById(R.id.text_time);
            llcover = itemView.findViewById(R.id.llcover);

        }
    }

    public interface adapterInterface {

        public void onClick(RecyclerView.ViewHolder holder, int position);

        public void onLongPressed(RecyclerView.ViewHolder holder, int position);

    }

}

