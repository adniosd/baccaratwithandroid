package com.vivogaming.livecasino.global;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import static com.vivogaming.livecasino.global.Dialogs.showDialogNoConnection;

public abstract class Network {

    /**
     * return current ip address as string
     * @param _context
     * @return
     */
    public static final String getIpAddress(final Context _context) {
        final WifiManager wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    /**
     * check existing internet connection
     * @param _context
     * @return
     */
    public static final boolean isInternetConnectionAvailable(final Context _context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * if no connection - show dialog
     */
    public static final boolean checkInternetConnection(final Activity _activity) {
        if (!isInternetConnectionAvailable(_activity)) {
            showDialogNoConnection(_activity);
            return false;
        }
        return true;
    }
}