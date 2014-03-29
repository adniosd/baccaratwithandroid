package com.vivogaming.livecasino.screens.register;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import com.vivogaming.livecasino.R;

import static com.vivogaming.livecasino.screens.register.Register.showToastRegister;


/**
 * Created by Sofia on 10/31/13.
 */
public abstract class StepValidation {

    /**
     * listen fail in register
     *
     * @param _etUserName_SR field
     * @param _etEmail_SR    field
     * @param _etPassword_SR field
     * @param _activity
     */
    public static final void editTextListeners(final EditText _etUserName_SR, final EditText _etEmail_SR, final EditText _etPassword_SR, final Activity _activity) {
        _etUserName_SR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _etUserName_SR.setTextColor(_activity.getResources().getColor(R.color.black));
                } else {
                    String name = _etUserName_SR.getText().toString();
                    if (name.length() < 8 || name.length() > 20) {
                        showToastRegister(_activity, R.string.length_username);
                        _etUserName_SR.setTextColor(_activity.getResources().getColor(R.color.red));
                    }
                }
            }
        });

        _etEmail_SR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _etEmail_SR.setTextColor(_activity.getResources().getColor(R.color.black));
                } else {
                    String email = _etEmail_SR.getText().toString();
                    if (!email.contains("@")) {
                        showToastRegister(_activity, R.string.valid_email);
                        _etEmail_SR.setTextColor(_activity.getResources().getColor(R.color.red));
                    }
                }
            }
        });

        _etPassword_SR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _etPassword_SR.setTextColor(_activity.getResources().getColor(R.color.black));
                } else {
                    String password = _etPassword_SR.getText().toString();
                    if (password.length() < 8 || password.length() > 40 || password.equals(_etUserName_SR.getText().toString())) {
                        showToastRegister(_activity, R.string.length_password);
                        _etPassword_SR.setTextColor(_activity.getResources().getColor(R.color.red));
                    }
                }
            }
        });
    }
}
