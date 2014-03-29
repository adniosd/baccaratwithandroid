package com.vivogaming.livecasino.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.register.Register;

import static com.vivogaming.livecasino.global.LocationWorker.checkNetworkLocation;
import static com.vivogaming.livecasino.global.Logout.setLogoutTime;

public final class Start extends Activity implements View.OnClickListener {
    private ImageButton btnLogin_SS, btnSingUp_SS;
    /**
     * Called when the activity is first created.
     */
    @Override
    public final void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.screen_start);

//        if (!checkInternetConnection(this)) return;

        btnLogin_SS         = (ImageButton) findViewById(R.id.btnLogin_SS);
        btnSingUp_SS        = (ImageButton) findViewById(R.id.btnSingUp_SS);

        btnLogin_SS.setOnClickListener(this);
        btnSingUp_SS.setOnClickListener(this);

//        initializeLocationManager(this);
//        checkNetworkLocation(this);
    }

    @Override
    public final void onStart() {
        super.onStart();
        setLogoutTime(this, 0);
    }

    @Override
    public final void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        if (_requestCode == 1) { //request for network location
            checkNetworkLocation(this);
        }
    }

    @Override
    public final void onClick(final View _view) {
        if (_view == btnLogin_SS) {
            onClickBtnLogin_SS();
        } else if (_view == btnSingUp_SS) {
            onClickBtnSingUp_SS();
        }
    }

    private final void onClickBtnLogin_SS() {
        final Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private final void onClickBtnSingUp_SS() {
       startActivity(new Intent(this, Register.class));
        finish();
    }
}