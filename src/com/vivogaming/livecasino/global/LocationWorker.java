package com.vivogaming.livecasino.global;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.vivogaming.livecasino.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.vivogaming.livecasino.global.Dialogs.*;

/**
 * User: Vasja
 * Date: 30.10.13
 * Time: 9:21
 */
public abstract class LocationWorker {
    private static final String locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
    private static LocationManager locationManager;

    /**
     * create new instance of LocationManager
     * @param _context
     */
    public static final void initializeLocationManager(final Context _context) {
        locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * check is network location is enabled in settings
     * @return
     */
    public static final boolean isNetworkLocationEnabled() {
        return locationManager.isProviderEnabled(locationProviderNetwork);
    }

    /**
     * starts dialog-indicator and get location
     * when got location - dismiss dialog, get country code
     * if code == USA or Israel - show prohibited dialog
     * @param _activity
     */
    public static final void getLocation(final Activity _activity) {
        //start dialog
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_progress_with_message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView tvMessage_DPWM = (TextView) dialog.findViewById(R.id.tvMessage_DPWM);
        tvMessage_DPWM.setText(_activity.getString(R.string.checking_location));

        final LocationListener locationListener = new LocationListener() {
            public final void onLocationChanged(final Location _location) {
                locationManager.removeUpdates(this);
                final String countryCode = getCurrentCountyCode(_activity, _location);
                checkCountryCode(_activity, countryCode);
                dialog.dismiss();
            }

            public final void onStatusChanged(final String _provider, final int _status, final Bundle _extras) {Log.d("tag", "onStatusChanged");}
            public final void onProviderEnabled(final String _provider) {Log.d("tag", "onProviderEnabled");}
            public final void onProviderDisabled(final String _provider) {Log.d("tag", "onProviderDisabled");}
        };

        locationManager.requestLocationUpdates(locationProviderNetwork, 0, 0, locationListener);
    }

    /**
     * got curent county code from location object
     * @param _context
     * @param _location
     * @return country code or empty string
     */
    private static final String getCurrentCountyCode(final Context _context, final Location _location) {
        final double latitude = _location.getLatitude();
        final double longitude = _location.getLongitude();
        final Geocoder geocoder = new Geocoder(_context, Locale.getDefault());
        try {
            final List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            final Address currentAddress = addresses.get(0);
            final String countryCode = currentAddress.getCountryCode();
            return countryCode;
        } catch (final IOException _e) {
            _e.printStackTrace();
        }

        return "";
    }

    /**
     * if code == USA or Israel - show prohibited dialog
     * @param _activity
     * @param _countyCode
     */
    private static final void checkCountryCode(final Activity _activity, final String _countyCode) {
        if (_countyCode.isEmpty()) {
            showDialogUnableToIdentifyLocation(_activity);
            return;
        }

        if (_countyCode.equals("US") || _countyCode.equals("IL"))
            showDialogProhibitedCounty(_activity);
    }

    /**
     * if network location enabled - get location
     * else - show dialog
     */
    public static final void checkNetworkLocation(final Activity _activity) {
        if (isNetworkLocationEnabled()) {
            getLocation(_activity);
        } else {
            showDialogNetworkLocation(_activity);
        }
    }
}