package com.vivogaming.livecasino.screens.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.R.drawable;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.screens.Start;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Dialogs.dialogsForSR;
import static com.vivogaming.livecasino.global.Dialogs.show_alert;
import static com.vivogaming.livecasino.global.Variables.getMyYear;
import static com.vivogaming.livecasino.global.Variables.setMyYear;
import static com.vivogaming.livecasino.screens.register.StepValidation.editTextListeners;
import static com.vivogaming.livecasino.screens.register.ValidateDate.validateDateRegister;
import static com.vivogaming.livecasino.web.ResponseWorker.*;
import static com.vivogaming.livecasino.web.WebObservable.addMyObserver;
import static com.vivogaming.livecasino.web.WebObservable.deleteMyObserver;


public final class Register extends Activity implements View.OnClickListener, Observer {

    private final Context context = this;
    private TextView tvBirthDate_SR, tvGender_SR, tvlanguage_SR, tv_currency_SR, tvChooseCountry_SR, tv_country_choose_SR;
    private EditText etUserName_SR, etEmail_SR, etFname_SR, etLname_SR, etMobile_SR, etStreet_SR, city, postalCode, etPassword_SR, etRetypePassward_SR;
    private ImageButton ib_gender_SR;
    private ImageButton ib_currency_SR;
    private ImageButton ib_language_SR;
    private ImageButton ibChooseCountry;
    private ImageButton ib_country_SR;
    private Button btRegister_SR;
    private Switch swAgree_SR;
    int myYear = 1990, myDay = 02, myMonth = 10;
    private ScrollView scrollView;
    private static TextView textToast;
    private static Toast toast;


    /**
     * Create activity call one time
     *
     * @param _savedInstanceState
     */
    @Override
    public final void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.screen_register);
        addMyObserver(this, this);

        findViews();
        setListeners();
        editTextListeners(etUserName_SR, etEmail_SR, etPassword_SR, this);

    }

    /**
     * finds all ids
     */
    private final void findViews() {

        etUserName_SR   = (EditText) findViewById(R.id.etUserName_SR);
        etEmail_SR      = (EditText) findViewById(R.id.etEmail_SR);
        etFname_SR      = (EditText) findViewById(R.id.etFname_SR);
        etLname_SR      = (EditText) findViewById(R.id.etLname_SR);
        etMobile_SR     = (EditText) findViewById(R.id.etMobile_SR);
        etStreet_SR     = (EditText) findViewById(R.id.etStreet_SR);
        city            = (EditText) findViewById(R.id.etCity_SR);
        postalCode      = (EditText) findViewById(R.id.etPostal_SR);
        etPassword_SR   = (EditText) findViewById(R.id.etPassword_SR);
        etRetypePassward_SR = (EditText) findViewById(R.id.etRetypePassward_SR);

        tvBirthDate_SR  = (TextView) findViewById(R.id.tvBirthDate_SR);
        tvGender_SR     = (TextView) findViewById(R.id.tvGender_SR);
        tvlanguage_SR   = (TextView) findViewById(R.id.tvlanguage_SR);
        tv_currency_SR  = (TextView) findViewById(R.id.tv_currency_SR);
        tvChooseCountry_SR = (TextView) findViewById(R.id.tvChooseCountry_SR);
        tv_country_choose_SR = (TextView) findViewById(R.id.tv_country_choose_SR);

        btRegister_SR   = (Button) findViewById(R.id.btRegister_RS);
        ib_gender_SR    = (ImageButton) findViewById(R.id.ib_gender_SR);
        ib_country_SR   = (ImageButton) findViewById(R.id.ib_country_SR);
        ibChooseCountry = (ImageButton) findViewById(R.id.ibChooseCountry);
        ib_currency_SR  = (ImageButton) findViewById(R.id.ib_currency_SR);
        ib_language_SR  = (ImageButton) findViewById(R.id.ib_language_SR);

        swAgree_SR      = (Switch) findViewById(R.id.swAgree_SR);
        scrollView      = (ScrollView) findViewById(R.id.scrollView);
    }

    /**
     * Convenient function to read from raw file
     *
     * @param _context
     * @return
     * @throws java.io.IOException
     */
    private final static String readFileAsString(final Context _context)
            throws java.io.IOException {

        InputStream inputStream = _context.getResources().openRawResource(
                R.raw.countries);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }


    /**
     * jsone for country flags Make ArrayList of country and id of pictures
     *
     * @return ArrayList of country and id of pictures
     * @throws IOException   no dates
     * @throws JSONException
     */

    public final ArrayList<CountryObject> country() throws IOException, JSONException {
        final String jsonStr = readFileAsString(this);

        final ArrayList<CountryObject> allCountriesList = new ArrayList<CountryObject>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        Iterator<?> keys = jsonObject.keys();

        // Add the data to all countries list
        while (keys.hasNext()) {
            final String key = (String) keys.next();

            final String drawableName = "flag_" + key.toLowerCase(Locale.ENGLISH);
            final int picId = getResId(drawableName);
            final String countryName = jsonObject.getString(key);

            allCountriesList.add(new CountryObject(picId, countryName));
        }

        // Sort the all countries list based on country name
        Collections.sort(allCountriesList, new Comparator<CountryObject>() {
            @Override
            public int compare(CountryObject countryObject, CountryObject countryObject2) {
                return countryObject.name.compareToIgnoreCase(countryObject2.name);
            }
        });

        return allCountriesList;
    }

    /**
     * The drawable image name has the format "flag_$countryCode". We need to
     * load the drawable dynamically from country code. Code from
     * http://stackoverflow.com/
     * questions/3042961/how-can-i-get-the-resource-id-of
     * -an-image-if-i-know-its-name
     *
     * @param _drawableName
     * @return
     */
    private final int getResId(final String _drawableName) {

        try {
            Class<drawable> res = R.drawable.class;
            Field field = res.getField(_drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("COUNTRYPICKER", "Failure to get drawable id.", e);
        }
        return -1;
    }

    @Override
    public final void onClick(View _view) {

        final int id = _view.getId();
        final String[] genderItems = getResources().getStringArray(R.array.gender);
        final String[] languageItems = getResources().getStringArray(R.array.languageArray);
        final String[] currencyItems = getResources().getStringArray(R.array.currencyArray);

        switch (id) {
            case R.id.ib_country_SR:
                onClickIbCountry();
                break;
            case R.id.ibChooseCountry:
                onClickIbChooseCountry();
                break;
            case R.id.btRegister_RS:
                try {
                    onClickIbRegister();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ib_gender_SR:
                dialogsForSR(Register.this, R.string.gender_picker, genderItems, tvGender_SR);
                break;
            case R.id.ib_currency_SR:
                dialogsForSR(Register.this, R.string.currency, currencyItems, tv_currency_SR);
                break;
            case R.id.ib_language_SR:
                dialogsForSR(Register.this, R.string.language, languageItems, tvlanguage_SR);
                break;
            case R.id.tvBirthDate_SR:
                onClickTvBirthDate();
                break;
        }
    }

    /**
     * set all onClickListeners
     */
    private final void setListeners() {
        swAgree_SR.setOnClickListener(this);
        ib_country_SR.setOnClickListener(this);
        ib_currency_SR.setOnClickListener(this);
        ibChooseCountry.setOnClickListener(this);
        btRegister_SR.setOnClickListener(this);
        ib_gender_SR.setOnClickListener(this);
        ib_language_SR.setOnClickListener(this);
        tvBirthDate_SR.setOnClickListener(this);
    }

    /**
     * pick country
     */
    private void onClickIbCountry() {
        int widthDialog = ib_country_SR.getWidth();
        CountryAdapter adapter2 = null;
        try {
            adapter2 = new CountryAdapter(Register.this, R.layout.list_country, country());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        show_alert(Register.this,adapter2,tv_country_choose_SR,widthDialog);
    }

    /**
     * pick country of Residents
     */
    private void onClickIbChooseCountry() {
        int widthDialog = ibChooseCountry.getWidth();
        CountryAdapter adapter1 = null;
        try {
            adapter1 = new CountryAdapter(Register.this, R.layout.list_country, country());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        show_alert(Register.this,adapter1,tvChooseCountry_SR,widthDialog);
    }

    /**
     * pass all date, validate before pass
     */
    private void onClickIbRegister() throws IOException {
        validateDateRegister(Register.this,
                etUserName_SR,
                etEmail_SR,
                etFname_SR,
                etLname_SR,
                tvGender_SR,
                tvBirthDate_SR,
                etMobile_SR,
                etStreet_SR,
                city,
                postalCode,
                tv_currency_SR,
                tvlanguage_SR,
                etPassword_SR,
                etRetypePassward_SR,
                tvChooseCountry_SR,
                tv_country_choose_SR,
                swAgree_SR,
                scrollView,
                getMyYear());
    }


    /**
     * pick birth date
     */
    private void onClickTvBirthDate() {
        DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
            int myYear = 1990
                    ,
                    myDay = 02
                    ,
                    myMonth = 10;

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myYear = year;
                myMonth = monthOfYear + 1;
                myDay = dayOfMonth;

                tvBirthDate_SR.setTextColor(getResources().getColor(R.color.black));
                setMyYear(myYear);
                tvBirthDate_SR.setText(myDay + "." + myMonth + "." + myYear);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, myCallBack, myYear, myMonth, myDay);
        datePickerDialog.show();
    }

    /**
     * prepare and show toast on register screen
     * @param _activity
     * @param _message text that contains toast
     */
    public static final void showToastRegister(final Activity _activity,final int _message) {
        final LayoutInflater inflater = _activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast_message_border,
                (ViewGroup) _activity.findViewById(R.id.llToastBgrnd_TMB));
        textToast = (TextView) layout.findViewById(R.id.tvMessage_TMB);

        toast = Toast.makeText(_activity, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,0,0);
        toast.setView(layout);
        textToast.setText(_message);
        toast.show();
    }

    /**
     * ModelObservable invoke this method when got response from server
     *
     * @param _observable called Observable
     * @param _data       passed data
     */
    @Override
    public void update(final Observable _observable, final Object _data) {
        if (_data == null) return;

        final HashMap<String, Object> resultMap = (HashMap<String, Object>) _data;

        if (resultMap.containsKey(ERROR)) {
            final HashMap<String, String> errorMap = (HashMap<String, String>) resultMap.get(ERROR);
            responseError(this, errorMap);
            if (errorMap.get(ERROR_DESCRIPTION).equals("EMAIL IN USE")) {
                scrollView.scrollTo(0, +20);
            }
            if (errorMap.get(ERROR_DESCRIPTION).equals("USERNAME IN USE")) {
                scrollView.scrollTo(0, +20);
            }
        } else if (resultMap.containsKey(API_REGISTER)) {
            responseApiRegister(this, this);
        } else if (resultMap.containsKey(API_LOGIN)) {
            final HashMap<String, String> loginMap = (HashMap<String, String>) resultMap.get(API_LOGIN);
            responseApiLogin(this, loginMap);
        } else if (resultMap.containsKey(API_GET_ACTIVE_TABLES)) {
            final ArrayList<TableObject> activeTableList = (ArrayList<TableObject>) resultMap.get(API_GET_ACTIVE_TABLES);
            responseApiGetActiveTables(this, this, activeTableList);
        }
    }

    @Override
    public void onBackPressed() {
        deleteMyObserver(this, this);
        startActivity(new Intent(this, Start.class));
        finish();
    }
}
