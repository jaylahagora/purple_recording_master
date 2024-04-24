package com.smartrecording.recordingplugin.app;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.xunison.recordingplugin.BuildConfig;
import com.smartrecording.recordingplugin.networking.SSLCertificateHandler;
import com.smartrecording.recordingplugin.utils.MyPreferenceManager;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;


/**
 * Created by Admin on 17-08-2017.
 */
@Keep
public class MyApplication extends MultiDexApplication {

    private static final String TAG = "MyApplication";
    private MyPreferenceManager pref;
    private static MyApplication mInstance;

    public static Context getAppContext() {
        return MyApplication.mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
        AppCenter.start(this, "85a7dc65-090d-4f63-91e8-b837eed71176",
                Analytics.class, Crashes.class);
        initializeSSLContext(this);
        //      CommonMethods.setApplicationLanguage(mInstance);


    }

    public static void initializeSSLContext(Context mContext) {
        try {
//            SSLCertificateHandler.nuke();
            SSLContext.getInstance("TLSv1.2");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mInstance.getApplicationContext() != null ?
                mInstance.getApplicationContext() : mInstance;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }

    @Keep
    public static String fjaoiigjeusirgn() {
        return BuildConfig.gkje__00hu;
    }

}
