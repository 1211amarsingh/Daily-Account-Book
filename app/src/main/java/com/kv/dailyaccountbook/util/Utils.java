package com.kv.dailyaccountbook.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.kv.dailyaccountbook.BuildConfig;

public class Utils {
    private static final String TAG = "Retro Request : ";
    private static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void logw(String message) {

        if (DEVELOPER_MODE)
            try {
                Log.w(TAG, message);
            } catch (Exception ignore) {
            }
    }

    public static void loge(String message) {
            try {
                Log.e(TAG, message);
            } catch (Exception ignore) {
            }
    }
}
