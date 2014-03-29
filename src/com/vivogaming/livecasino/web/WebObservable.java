package com.vivogaming.livecasino.web;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.vivogaming.livecasino.global.Constants.TAG_OBSERVER;

/**
 * class for notifying when requests finished
 * singleton
 */
public final class WebObservable extends Observable {
    private static volatile WebObservable instance;

    private WebObservable() {}

    public static final WebObservable getInstance() {
        if (instance == null)
            synchronized (WebObservable.class) {
                if (instance == null)
                    instance = new WebObservable();
            }
        return instance;
    }

    public static final void addMyObserver(final Activity _activity, final Observer _observer) {
        getInstance().deleteObservers();
        getInstance().addObserver(_observer);

        final int observersCount = getInstance().countObservers();
        Log.d(TAG_OBSERVER, "Add observer: " + _activity.getClass().getName() + ", observersCount = " + observersCount);
    }

    public static final void deleteMyObserver(final Activity _activity, final Observer _observer) {
        getInstance().deleteObserver(_observer);

        final int observersCount = getInstance().countObservers();
        Log.d(TAG_OBSERVER, "Delete observer: " + _activity.getClass().getName() + ", observersCount = " + observersCount);
    }

    /**
     * notifying Observers
     */
    public final void notifyMyObservers(final HashMap<String, Object> _resultMap) {
        setChanged();
        notifyObservers(_resultMap);
    }
}