package com.vivogaming.livecasino.web;

import android.util.Log;

import static com.vivogaming.livecasino.global.Constants.TAG_THREAD;

/**
 * User: Vasja
 * Date: 04.11.13
 * Time: 16:14
 */
public abstract class RequestPool {
    //use this for cancelling get status request on game screen when going to choose table screen
    public static Request asyncTask;

    public static final void cancelRequest() {
        if (asyncTask != null) {
            asyncTask.cancel(false);
            Log.d(TAG_THREAD, "RequestPool: cancelRequest(), asyncTask.cancel(false);");
        }
    }
}