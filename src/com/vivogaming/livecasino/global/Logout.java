package com.vivogaming.livecasino.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.vivogaming.livecasino.screens.Start;

import java.lang.System;
import java.util.Observer;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Variables.clearUserVariables;
import static com.vivogaming.livecasino.web.WebObservable.deleteMyObserver;

/**
 * Created with IntelliJ IDEA.
 * User: Vasja
 * Date: 28.10.13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class Logout {
    /**
     * used when resuming program
     * @param _context
     * @param _logoutTime
     */
    public static final void setLogoutTime(final Context _context, final long _logoutTime) {
        final SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LOGOUT_TIME, _logoutTime);
        editor.apply();
    }

    public static final long getLogoutTime(final Context _context) {
        final SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(KEY_LOGOUT_TIME, 0);
    }

    public static final void checkLogoutTime(final Activity _activity) {
        final long logoutTime = getLogoutTime(_activity);
        if (logoutTime == 0) return;

        final long currentTime = System.currentTimeMillis();
        if (currentTime - logoutTime > LOGOUT_TIME_VAL) {
            clearUserVariables(_activity);
            final Intent intent = new Intent(_activity, Start.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            _activity.startActivity(intent);
        }
    }

    public static final void doLogout(final Activity _activity, final Observer _observer) {
        deleteMyObserver(_activity, _observer);
        clearUserVariables(_activity);
        final Intent intent = new Intent(_activity, Start.class);
        _activity.startActivity(intent);
        _activity.finish();
    }
}