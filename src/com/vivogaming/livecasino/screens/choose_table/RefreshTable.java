package com.vivogaming.livecasino.screens.choose_table;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import com.vivogaming.livecasino.global.Constants;
import com.vivogaming.livecasino.global.Variables;
import com.vivogaming.livecasino.web.Request;

import static com.vivogaming.livecasino.global.Constants.API_GET_ACTIVE_TABLES;
import static com.vivogaming.livecasino.web.RequestPool.asyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 1/28/14
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RefreshTable {

    private static Handler mRefreshHandler = new Handler() ;
    public static final int ONE_MINUTE = 60000;
    private static Activity mParentActivity;


    public static void startPeriodicRefresh(final Activity _parentActivity){
       mParentActivity = _parentActivity;
       mRefreshHandler.postDelayed(mRefreshChooseTable, 2 * ONE_MINUTE);
    }

    private static Runnable mRefreshChooseTable = new Runnable() {
        public void run() {

            asyncTask = new Request(mParentActivity, API_GET_ACTIVE_TABLES);
            asyncTask.execute(Constants.OPERATOR_ID_NUM, Variables.getCurrency());
            mRefreshHandler.postDelayed(this, 2 * ONE_MINUTE);
        }
    };

    public static  void stopPeriodicRefresh(){
            mRefreshHandler.removeCallbacks(mRefreshChooseTable);
    }
}
