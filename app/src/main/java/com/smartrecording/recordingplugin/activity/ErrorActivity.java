package com.smartrecording.recordingplugin.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static com.purple.iptv.player.utils.UtilConstant.ToastE;


public class ErrorActivity extends AppCompatActivity {

    Dialog dialog;

    public interface ResultListener {
        void onretry(int number);

        void ondismiss();
    }

    private static ResultListener resultListener;

    public static void setListener(ResultListener resultListener1) {
        resultListener = resultListener1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToastE(this, "Oops.!!! Please UnPlug/Replug USB Drive And Record Again");
        finish();
      //  bindData();

    }

    private void bindData() {
        resultListener.onretry(1);
        super.onBackPressed();
                finish();
//        dialog = new Dialog(DialogActivity.this, R.style.Theme_D1NoTitleDim) {
//            @Override
//            public void onBackPressed() {
//                super.onBackPressed();
//                finish();
//            }
//        };
//        dialog.requestWindowFeature(1);
//        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_back);
//        dialog.setContentView(R.layout.dialog_recording_no_internet_);
//        dialog.setCancelable(false);
//        TextView btn_no = dialog.findViewById(R.id.btn_cancel);
//        TextView btn_yes = dialog.findViewById(R.id.btn_ok);
//
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                resultListener.ondismiss();
//                finish();
//
//            }
//        });
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                resultListener.onretry(1);
//                finish();
//            }
//        });
//
//
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(dialog.getWindow().getAttributes());
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        layoutParams.height = -2;
//        layoutParams.gravity = 17;
//        // dialog.requestWindowFeature(1);
////        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.getWindow().setAttributes(layoutParams);
//        dialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
