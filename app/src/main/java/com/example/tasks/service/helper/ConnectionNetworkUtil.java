package com.example.tasks.service.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class ConnectionNetworkUtil {

    public static Boolean isConnectionAvailable(Context context) {
        boolean result = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network networkCapabilities = cm.getActiveNetwork();
                if (networkCapabilities == null) {
                    return false;
                }

                NetworkCapabilities actNw = cm.getNetworkCapabilities(networkCapabilities);
                if (actNw == null) {
                    return false;
                }

                result = (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

            } else {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    int type = networkInfo.getType();
                    result = ((type == ConnectivityManager.TYPE_WIFI)
                            || (type == ConnectivityManager.TYPE_MOBILE)
                            || (type == ConnectivityManager.TYPE_ETHERNET));
                }
            }
        }

        return result;
    }
}
