package com.fullsail.android.safetravels;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class UtilsHelper {

    public static boolean getConnectivityStatusString(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mgr != null) {
            NetworkInfo activeNetwork = mgr.getActiveNetworkInfo();

            if (activeNetwork != null){
                return activeNetwork.isConnected();
            }

        }

        return false;
    }

}
