package com.mentorandroid.myapplication;

/**
 * Created by BrunoHauck on 3/19/16.
 */
import android.content.Context;
import android.net.ConnectivityManager;

public class Util {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conectivtyManager.getActiveNetworkInfo() != null && conectivtyManager.getActiveNetworkInfo().isAvailable();
    }

}
