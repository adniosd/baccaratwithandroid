package com.vivogaming.livecasino.screens.choose_table;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.screens.choose_table.indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Dialogs.showDialogLogout;
import static com.vivogaming.livecasino.global.Logout.*;
import static com.vivogaming.livecasino.global.Variables.*;
import static com.vivogaming.livecasino.screens.game.NotificationDataWorker.getCurrencySymbolByName;
import static com.vivogaming.livecasino.web.ResponseWorker.*;
import static com.vivogaming.livecasino.web.WebObservable.addMyObserver;

public final class ChooseTable extends Activity implements View.OnClickListener, Observer {
    private TextView tvWelcome_SCT, tvBalance_SCT;
    private Button btnLogout_SCT;
    private ViewPager vpChoose_SCT;

    @Override
    public final void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.screen_choose_table);

        tvWelcome_SCT       = (TextView) findViewById(R.id.tvWelcome_SCT);
        tvBalance_SCT       = (TextView) findViewById(R.id.tvBalance_SCT);
        btnLogout_SCT       = (Button) findViewById(R.id.btnLogout_SCT);
        vpChoose_SCT        = (ViewPager) findViewById(R.id.vp_page_SCT);

        loadVariables(this);

        prepareViewPager();
        prepareWelcomeAndBalanceText();

        btnLogout_SCT.setOnClickListener(this);
    }

    @Override
    public final void onStart() {
        super.onStart();
        Log.d("tag", "ChooseTable: onStart");
        addMyObserver(this, this);
        checkLogoutTime(this);
        RefreshTable.startPeriodicRefresh(this);
    }

    @Override
    public final void onStop() {
        super.onStop();
        setLogoutTime(this, System.currentTimeMillis());

        RefreshTable.stopPeriodicRefresh();
    }

    @Override
    public final void onClick(final View _view) {
        if (_view == btnLogout_SCT) {
            onClickBtnLogout_SCT();
        }
    }

    private final void onClickBtnLogout_SCT() {
        doLogout(this, this);
    }

    @Override
    public final void onBackPressed() {
        showDialogLogout(this, this);
    }

    private final void prepareViewPager() {
        final ArrayList<TableObject> tableList = (ArrayList<TableObject>) getIntent().getSerializableExtra("tableList");

        final TableLobbyAdapter pagerAdapter = new TableLobbyAdapter(this, tableList);
        vpChoose_SCT.setAdapter(pagerAdapter);

        final CirclePageIndicator pagerIndicator = (CirclePageIndicator) findViewById(R.id.indicator_SCT);
        pagerIndicator.setViewPager(vpChoose_SCT);
    }


    private final void prepareWelcomeAndBalanceText() {
        final String welcome = getString(R.string.welcome) + " " + getUserName();
        tvWelcome_SCT.setText(welcome);

        final float playerBalance = Float.valueOf(getBalance());
//        tvBalance_SCT.setText(getString(R.string.balance) + " " + playerBalance);
        tvBalance_SCT.setText(getCurrencySymbolByName(getCurrency()) + playerBalance);
    }

    @Override
    public final void update(final Observable _observable, final Object _data) {
        if (_data == null) return;

        final HashMap<String, Object> resultMap = (HashMap<String, Object>) _data;

        if (resultMap.containsKey(ERROR)) {
            final HashMap<String, String> errorMap = (HashMap<String, String>) resultMap.get(ERROR);
            responseError(this, errorMap);
        } else if (resultMap.containsKey(API_INIT)) {
            final HashMap<String, String> initMap = (HashMap<String, String>) resultMap.get(API_INIT);
            responseApiInit(this, initMap);
        } else if (resultMap.containsKey(API_GET_STATUS)) {
            final HashMap<String, String> statusMap = (HashMap<String, String>) resultMap.get(API_GET_STATUS);
            responseApiGetStatusFirst(this, this, statusMap);
        }
    }
}