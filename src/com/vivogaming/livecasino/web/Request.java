package com.vivogaming.livecasino.web;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.Login;
import com.vivogaming.livecasino.screens.choose_table.ChooseTable;
import com.vivogaming.livecasino.screens.game.Game;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.ErrorHandler.createErrorMapWithStatusCode;
import static com.vivogaming.livecasino.global.Network.isInternetConnectionAvailable;
import static com.vivogaming.livecasino.web.ApiWorker.*;
import static com.vivogaming.livecasino.web.ProgressDialogFragment.startProgressDialog;
import static com.vivogaming.livecasino.web.ProgressDialogFragment.stopProgressDialog;

/**
 * class for sending request to server
 */
public final class Request extends AsyncTask<Object, Void, HashMap<String, Object>> {
    private String g_api;
    private Activity g_activity;

    /**
     * constructor for request
     * @param _api  api name for observers
     */
    public Request(final Activity _activity, final String _api) {
        g_api = _api;
        g_activity = _activity;
    }

    @Override
    protected final HashMap<String, Object> doInBackground(final Object... _objects) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        if (isCancelled()) return null;

        try {
            if (g_api.equals(API_LOGIN)) {
                resultMap = apiLogin((String) _objects[0], (String) _objects[1], (String) _objects[2], (String) _objects[3]);
            } else if (g_api.equals(API_GET_ACTIVE_TABLES)) {
                resultMap = apiGetActiveTables((String) _objects[0], (String) _objects[1]);
            } else if (g_api.equals(API_INIT)) {
                resultMap = apiInit((String) _objects[0], (String) _objects[1], (String) _objects[2]);
            } else if (g_api.equals(API_GET_STATUS)) {
                resultMap = apiGetStatus((String) _objects[0], (String) _objects[1], (String) _objects[2],
                        (String) _objects[3], (String) _objects[4]);
            } else if (g_api.equals(API_NEW_BETS)) {
                resultMap = apiNewBets((String) _objects[0], (String) _objects[1], (String) _objects[2],
                        (String) _objects[3], (String) _objects[4], (String) _objects[5], (String) _objects[6]);
            } else if (g_api.equals(API_REGISTER)){
                resultMap = apiRegister((String) _objects[0], (String) _objects[1], (String) _objects[2],
                        (String) _objects[3], (String) _objects[4], (String) _objects[5], (String) _objects[6]);
            } else if (g_api.equals(API_TIPS)) {
                resultMap = apiTips((String) _objects[0], (String) _objects[1], (String) _objects[2],
                        (String) _objects[3], (String) _objects[4], (String) _objects[5]);
            }
        } catch (final IOException _e) {
            handleError(_e, resultMap);
        } catch (final URISyntaxException _e) {
            handleError(_e, resultMap);
        } finally {
            return resultMap;
        }
    }

    @Override
    public final void onPreExecute() {
        if (!isInternetConnectionAvailable(g_activity)) return;

        //no need starting dialog in call API_GET_ACTIVE_TABLES, because  login and active tables invokes successively
        //dialog shows at start login and destroyed at finishing after get active tables or exception
        if (g_activity instanceof Login && g_api.equals(API_GET_ACTIVE_TABLES)) return;
        //in choose table screen API_GET_STATUS invoke after API_INIT, no need showing dialog
        if (g_activity instanceof ChooseTable && g_api.equals(API_GET_STATUS)) return;
        //no need showing loading dialog in calls API_GET_STATUS and API_NEW_BETS (on Game screen)
        if (g_activity instanceof Game && (g_api.equals(API_GET_STATUS) || g_api.equals(API_NEW_BETS) ||
                g_api.equals(API_TIPS))) return;

        startProgressDialog(g_activity);
    }

    @Override
    public final void onPostExecute(final HashMap<String, Object> _resultMap) {
        WebObservable.getInstance().notifyMyObservers(_resultMap);

        if ((g_api.equals(API_LOGIN) || g_api.equals(API_INIT)) && !_resultMap.containsKey(ERROR)) return;       //explained above

        stopProgressDialog();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d(TAG_THREAD, "Request: onCancelled(), api = " + g_api + ", isCancelled(): " + isCancelled());
    }

    private final void handleError(final Exception _e, final HashMap<String, Object> _resultMap) {
        _e.printStackTrace();
        _resultMap.put(ERROR, createErrorMapWithStatusCode(-1,
                g_activity.getResources().getString(R.string.network_problem_please_make_sure)));
        stopProgressDialog();
    }
}
