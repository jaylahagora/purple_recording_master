package com.smartrecording.recordingplugin.networking;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLCertificateHandler {

    protected static final String TAG = "NukeSSLCerts";

    /**
     * Enables https connections
     */
//    public static void nuke() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//            } };
//
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String arg0, SSLSession arg1) {
//                    return true;
//                }
//            });
//        } catch (Exception e) {
//        }
//    }

}