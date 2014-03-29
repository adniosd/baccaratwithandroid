package com.vivogaming.livecasino.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.web.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Network.getIpAddress;
import static com.vivogaming.livecasino.global.Variables.setPass;
import static com.vivogaming.livecasino.global.Variables.setUserName;
import static com.vivogaming.livecasino.web.RequestPool.asyncTask;
import static com.vivogaming.livecasino.web.ResponseWorker.*;
import static com.vivogaming.livecasino.web.WebObservable.addMyObserver;
import static com.vivogaming.livecasino.web.WebObservable.deleteMyObserver;

public final class Login extends Activity implements View.OnClickListener, Observer {
    private EditText etLogin_SL, etPass_SL;
    private ImageButton btnLogin_SL;

    @Override
    public final void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.screen_login);

        etLogin_SL          = (EditText) findViewById(R.id.etLogin_SL);
        etPass_SL           = (EditText) findViewById(R.id.etPass_SL);
        btnLogin_SL         = (ImageButton) findViewById(R.id.btnLogin_SL);

        btnLogin_SL.setOnClickListener(this);
    }

    @Override
    public final void onStart() {
        super.onStart();
        addMyObserver(this, this);
    }

    @Override
    public final void onClick(final View _view) {
        if (_view == btnLogin_SL) {
            onClickBtnLogin_SL(this);
        }
    }

    @Override
    public void onBackPressed() {
        deleteMyObserver(this, this);
        startActivity(new Intent(this, Start.class));
        finish();
    }

    private final void onClickBtnLogin_SL(final Context _context) {
        final String name = etLogin_SL.getText().toString();
        final String pass = etPass_SL.getText().toString();
        final String operatorKey = OPERATOR_ID_NUM;
        final String ip = getIpAddress(_context);

        setUserName(name);
        setPass(pass);      //for placing bets api call

        asyncTask = new Request(this, API_LOGIN);
        asyncTask.execute(name, pass, operatorKey, ip);
    }

    /**
     * ModelObservable invoke this method when got response from server
     * @param _observable   called Observable
     * @param _data passed data
     */
    @Override
    public final void update(final Observable _observable, final Object _data) {
        if (_data == null) return;

        final HashMap<String, Object> resultMap = (HashMap<String, Object>) _data;

        if (resultMap.containsKey(ERROR)) {
            final HashMap<String, String> errorMap = (HashMap<String, String>) resultMap.get(ERROR);
            responseError(this, errorMap);
        } else if (resultMap.containsKey(API_LOGIN)) {
            final HashMap<String, String> loginMap = (HashMap<String, String>) resultMap.get(API_LOGIN);
            responseApiLogin(this, loginMap);
        } else if (resultMap.containsKey(API_GET_ACTIVE_TABLES)) {
            final ArrayList<TableObject> activeTableList = (ArrayList<TableObject>) resultMap.get(API_GET_ACTIVE_TABLES);
            responseApiGetActiveTables(this, this, activeTableList);
        }
    }
}