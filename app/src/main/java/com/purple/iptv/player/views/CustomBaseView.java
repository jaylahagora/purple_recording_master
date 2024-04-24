package com.purple.iptv.player.views;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.purple.iptv.player.utils.UtilConstant;
import com.purple.iptv.player.utils.UtilMethods;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.model.RemoteConfigModel;
import com.xunison.recordingplugin.R;


public class CustomBaseView {

    private static final String TAG = "CustomBaseView";
    public final Context mContext;
    public ImageView image_background;
    private View view_layer;
    private RemoteConfigModel remoteConfig;


    public CustomBaseView(Context mContext) {
        this.mContext = mContext;

    }

    public final View getView() {
        Log.e(TAG, "getView: called" );
        View view = View.inflate(mContext, R.layout.activity_base,
                null);
        image_background = (ImageView) view.findViewById(R.id.image_background);
        view_layer = (View) view.findViewById(R.id.view_layer);




        remoteConfig = MyApplication.getInstance().getPrefManager().getRemoteConfig();
        if (remoteConfig.isBackground_auto_change() ||
                remoteConfig.isBackground_mannual_change()) {
            if (UtilConstant.currently_selected_background_image != null) {
                Glide.with(mContext)
                        .load(UtilConstant.currently_selected_background_image)
                        .into(image_background);
            }
            else if (MyApplication.getInstance().getPrefManager().getBgimage() != null && !MyApplication.getInstance().getPrefManager().getBgimage().equals("")) {
                Log.e(TAG, "getView: 3");
                Glide.with(mContext)
                        .load(MyApplication.getInstance().getPrefManager().getBgimage())
                        .into(image_background);
                //   image_background.setImageResource(R.drawable.app_bg);
            }else {
                Glide.with(mContext)
                        .load(R.drawable.app_bg)
                        .into(image_background);
            }
        } else {
            Log.e(TAG, "getView: " );
          //  CommonMethods.getBitmapFromDataBase(mContext, Config.APP_BACK_IMAGE, image_background,
              //      R.drawable.app_bg);
        }


        try {
            if (remoteConfig.getBackground_orverlay_color_code() != null) {
                String colorCode = remoteConfig.getBackground_orverlay_color_code();
                if (!colorCode.contains("#")) {
                    colorCode = "#" + colorCode;
                }
                UtilMethods.LogMethod("color123", colorCode);
                view_layer.setBackgroundColor(Color.
                        parseColor(colorCode));
            } else {
                view_layer.setBackgroundColor(mContext.getResources().
                        getColor(R.color.black_opacity_fifty));
            }
        } catch (Exception e) {
            view_layer.setBackgroundColor(mContext.getResources().
                    getColor(R.color.black_opacity_fifty));
        }

        return view;
    }


}
