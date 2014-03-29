package com.vivogaming.livecasino.screens.register;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.web.Request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static com.vivogaming.livecasino.global.Constants.API_REGISTER;
import static com.vivogaming.livecasino.global.Constants.OPERATOR_KEY_NUM;
import static com.vivogaming.livecasino.global.Variables.setPass;
import static com.vivogaming.livecasino.global.Variables.setUserName;
import static com.vivogaming.livecasino.screens.register.Register.showToastRegister;


/**
 * Created by Sofia on 10/18/13.
 */
public abstract class ValidateDate {


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static final void validateDateRegister(final Activity _activity,
                                                  final EditText _etUserName_SR,
                                                  final EditText _etEmail_SR,
                                                  final EditText _etFname_SR,
                                                  final EditText _etLname_SR,
                                                  final TextView _tvGender_SR,
                                                  final TextView _tvBirthDate_SR,
                                                  final EditText _etMobile_SR,
                                                  final EditText _etStreet_SR,
                                                  final EditText _city,
                                                  final EditText _postalCode,
                                                  final TextView _tv_currency_SR,
                                                  final TextView _tvlanguage_SR,
                                                  final EditText _etPassword_SR,
                                                  final EditText _etRetypePassward_SR,
                                                  final TextView _tvChooseCountry_SR,
                                                  final TextView _tv_country_choose_SR,
                                                  final Switch _swAgree_SR,
                                                  final ScrollView scrollView,
                                                  final int _year) {




        final String name,email, fName, lName, gender, bd, mobile, street, cityT, postalCodeC, currency, language, password, retypePassword, country, countryOfResidents;

            email = _etEmail_SR.getText().toString();
            name = _etUserName_SR.getText().toString();
            fName = _etFname_SR.getText().toString();
            lName = _etLname_SR.getText().toString();
            gender = _tvGender_SR.getText().toString();
            bd = _tvBirthDate_SR.getText().toString();
            mobile = _etMobile_SR.getText().toString();
            street = _etStreet_SR.getText().toString();
            cityT = _city.getText().toString();
            postalCodeC = _postalCode.getText().toString();
            currency = _tv_currency_SR.getText().toString();
            language = _tvlanguage_SR.getText().toString();
            password = _etPassword_SR.getText().toString();
            retypePassword = _etRetypePassward_SR.getText().toString();
            country = _tvChooseCountry_SR.getText().toString();
            countryOfResidents = _tv_country_choose_SR.getText().toString();

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] dateSplit = date.split("-");
        int year = Integer.valueOf(dateSplit[0]) - _year;


        if (name.equals("") || email.equals("") || fName.equals("") || lName.equals("") || gender.equals("Gender") || bd.equals("Birth date") || mobile.equals("") || street.equals("") || cityT.equals("") || postalCodeC.equals("") || currency.equals("Currency") || language.equals("Language") || password.equals("") || retypePassword.equals("") || country.equals("Choose country:") || countryOfResidents.equals("Choose country:")) {
            showToastRegister(_activity, R.string.fill_all);
            scrollView.scrollTo(_etUserName_SR.getScrollX(), _etUserName_SR.getScrollY());
        } else if (!password.equals(retypePassword)) {
            checkDate(_activity, _etPassword_SR, R.string.check_pass);
        } else if (name.length() < 8 || name.length() > 20) {
            checkDate(_activity, _etUserName_SR, R.string.length_username);
            scrollView.scrollTo(_etUserName_SR.getScrollX(), _etUserName_SR.getScrollY());
        } else if (password.length() < 8 || password.length() > 40 || password.equals(name)) {
            checkDate(_activity, _etPassword_SR, R.string.length_password);
            scrollView.scrollTo(_etPassword_SR.getScrollX(), _etPassword_SR.getScrollY());
        } else if (!checkEmail(email)) {
            checkDate(_activity, _etEmail_SR, R.string.valid_email);
        } else if (!_swAgree_SR.isChecked()) {
            checkDate(_activity, _swAgree_SR, R.string.valid_agree);
            _swAgree_SR.getResources().getColor(R.color.black);
        } else if (year < 18) {
            checkDate(_activity, _tvBirthDate_SR, R.string.valid_age);
        } else {
            final String operatorKey = OPERATOR_KEY_NUM;
            setUserName(name);
            setPass(password);

            new Request(_activity, API_REGISTER).execute(name, password, operatorKey, fName, lName, currency, email);
        }
    }

    private static void checkDate(Activity _activity, EditText _view, int _toastText) {
        _view.setTextColor(_activity.getResources().getColor(R.color.red));
        showToastRegister(_activity, _toastText);
    }

    private static void checkDate(Activity _activity, TextView _view, int _toastText) {
        _view.setTextColor(_activity.getResources().getColor(R.color.red));
        showToastRegister(_activity, _toastText);
    }

    private static void checkDate(Activity _activity, Switch _view, int _toastText) {
        _view.setTextColor(_activity.getResources().getColor(R.color.red));
        showToastRegister(_activity, _toastText);
    }

    private static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

}
