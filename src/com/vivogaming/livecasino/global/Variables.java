package com.vivogaming.livecasino.global;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.vivogaming.livecasino.screens.Login;
import com.vivogaming.livecasino.screens.choose_table.ChooseTable;
import com.vivogaming.livecasino.screens.game.Game;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Encoder.base64ToString;
import static com.vivogaming.livecasino.global.Encoder.stringToBase64;

public abstract class Variables {
    private static String operatorId;
    private static String currency;
    private static String userName;
    private static String playerToken;
    private static String tableId;
    private static String liveVideoUrl;
    private static String buyChipsUrl;
    private static String tableLimits;
    private static String[] boxLimits;
    private static String tableTrnId;
    private static String initId;
    private static String pass;
    private static String balance;

    private static int progressVolume;
    private static boolean switchSound;

    public static final void setOperatorID(final String _operatorId) {
        operatorId = _operatorId;
    }

    public static final String getOperatorId() {
        return operatorId;
    }

    public static final void setCurrency(final String _currency) {
        currency = _currency;
    }

    public static final String getCurrency() {
        return currency;
    }

    public static final void setUserName(final String _userName) {
        userName = _userName;
    }

    public static final String getUserName() {
        return userName;
    }

    public static final void setPlayerToken(final String _playerToken) {
        playerToken = _playerToken;
    }

    public static final String getPlayerToken() {
        return playerToken;
    }

    public static final void setTableId(final String _tableId) {
        tableId = _tableId;
    }

    public static final String getTableId() {
        return tableId;
    }

    public static final void setLiveVideoUrl(final String _liveVideoUrl) {
        liveVideoUrl = _liveVideoUrl;
    }

    public static final String getLiveVideoUrl() {
        return liveVideoUrl;
    }

    public static final void setBuyChipsUrl(final String _buyChipsUrl) {
        buyChipsUrl = _buyChipsUrl;
    }

    public static final String getBuyChipsUrl() {
        return buyChipsUrl;
    }

    public static final void setTableLimits(final String _tableLimits) {
        tableLimits = _tableLimits;
    }

    public static final String getTableLimits() {
        return tableLimits;
    }

    public static final void setProgressVolume(final int _progressVolume) {
        progressVolume = _progressVolume;
    }

    public static final int getProgressVolume() {
        Log.d(TAG_VOLUME, "getProgressVolume(): progressVolume = " + progressVolume);
        return progressVolume;
    }

    public static final void setSwitchSound(final Boolean _switchSound) {
        switchSound = _switchSound;
    }

    public static final boolean getSwitchSound() {
        Log.d(TAG_VOLUME, "getSwitchSound(): switchSound = " + switchSound);
        return switchSound;
    }

    public static final void setBoxLimits(final String _playerPairLimit, final String _playerLimit, final String _tieLimit,
                                          final String _bankerLimit, final String _bankerPairLimit) {
        if (boxLimits == null) boxLimits = new String[5];
        boxLimits[0] = _playerPairLimit;
        boxLimits[1] = _playerLimit;
        boxLimits[2] = _tieLimit;
        boxLimits[3] = _bankerLimit;
        boxLimits[4] = _bankerPairLimit;
    }

    public static final String[] getBoxLimits() {
        return boxLimits;
    }

    public static final void setTableTrnId(final String _tableTrnId) {
        tableTrnId = _tableTrnId;
    }

    public static final String getTableTrnId() {
        return tableTrnId;
    }

    public static final void setInitId(final String _initId) {
        initId = _initId;
    }

    public static final String getInitId() {
        return initId;
    }

    public static final void setPass(final String _pass) {
        pass = _pass;
    }

    public static final String getPass() {
        return pass;
    }

    public static final void setBalance(final String _balance) {
        balance = _balance;
    }

    public static final String getBalance() {
        return balance;
    }

    /**
     * save all variables in shared preferences
     * @param _activity
     */
    public static final void saveVariables(final Activity _activity) {
        final SharedPreferences sharedPreferences = _activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (_activity instanceof Login) {
            editor.putString(USER_NAME, userName);
            editor.putString(PLAYER_PASSWORD, stringToBase64(pass));
            editor.putString(OPERATOR_ID, operatorId);
            editor.putString(CURRENCY, currency);
            editor.putString(PLAYER_TOKEN, playerToken);
            editor.putString(BALANCE, balance);
        } else if (_activity instanceof ChooseTable) {
            editor.putString(TABLE_ID, tableId);
            editor.putString(LIVE_VIDEO_URL, liveVideoUrl);
            editor.putString(BUY_CHIPS_ON, buyChipsUrl);
            editor.putString(TABLE_LIMITS, tableLimits);
            editor.putString(PLAYER_PAIR_BOX_NUM, boxLimits[0]);
            editor.putString(PLAYER_BOX_NUM, boxLimits[1]);
            editor.putString(TIE_BOX_NUM, boxLimits[2]);
            editor.putString(BANKER_BOX_NUM, boxLimits[3]);
            editor.putString(BANKER_PAIR_BOX_NUM, boxLimits[4]);
            editor.putString(INIT_ID, initId);
        } else if (_activity instanceof Game) {
            editor.putString(TABLE_TRN_ID, tableTrnId);

            editor.putInt(KEY_VOLUME, progressVolume);
            Log.d(TAG_VOLUME, "saveVariables(): progressVolume = " + progressVolume);
            editor.putBoolean(KEY_SOUND, switchSound);
            Log.d(TAG_VOLUME, "saveVariables(): switchSound = " + switchSound);

            //delays
            editor.putInt(KEY_TIME_TOAST_DELAY, TIME_TOAST_DELAY);
            editor.putInt(KEY_TIME_ADDITIONAL_DELAY, TIME_ADDITIONAL_DELAY);
        }

        editor.apply();
    }

    /**
     * load all variables from shared preferences
     * if null or empty than load
     * @param _activity
     */
    public static final void loadVariables(final Activity _activity) {
        final SharedPreferences sharedPreferences = _activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        if (_activity instanceof ChooseTable) {
            if (tableId == null)
                tableId = sharedPreferences.getString(TABLE_ID, "");
            if (playerToken == null)
                playerToken = sharedPreferences.getString(PLAYER_TOKEN, "");
            if (operatorId == null)
                operatorId = sharedPreferences.getString(OPERATOR_ID, "");
            if (tableTrnId == null)
                tableTrnId = sharedPreferences.getString(TABLE_TRN_ID, "");
            if (tableLimits == null)
                tableLimits = sharedPreferences.getString(TABLE_LIMITS, "");
            if (boxLimits == null)
                boxLimits = new String[5];
            if (boxLimits[0] == null)
                boxLimits[0] = sharedPreferences.getString(PLAYER_PAIR_BOX_NUM, "");
            if (boxLimits[1] == null)
                boxLimits[1] = sharedPreferences.getString(PLAYER_BOX_NUM, "");
            if (boxLimits[2] == null)
                boxLimits[2] = sharedPreferences.getString(TIE_BOX_NUM, "");
            if (boxLimits[3] == null)
                boxLimits[3] = sharedPreferences.getString(BANKER_BOX_NUM, "");
            if (boxLimits[4] == null)
                boxLimits[4] = sharedPreferences.getString(BANKER_PAIR_BOX_NUM, "");
            if (userName == null)
                userName = sharedPreferences.getString(USER_NAME, "");
            if (balance == null)
                balance = sharedPreferences.getString(BALANCE, "");
        } else if (_activity instanceof Game) {
            if (tableId == null)
                tableId = sharedPreferences.getString(TABLE_ID, "");
            if (playerToken == null)
                playerToken = sharedPreferences.getString(PLAYER_TOKEN, "");
            if (operatorId == null)
                operatorId = sharedPreferences.getString(OPERATOR_ID, "");
            if (initId == null)
                initId = sharedPreferences.getString(INIT_ID, "");
            if (tableTrnId == null)
                tableTrnId = sharedPreferences.getString(TABLE_TRN_ID, "");
            if (liveVideoUrl == null)
                liveVideoUrl = sharedPreferences.getString(LIVE_VIDEO_URL, "");
            if (pass == null) {
                pass = sharedPreferences.getString(PLAYER_PASSWORD, "");
                pass = base64ToString(pass);
            }
            if (currency == null)
                currency = sharedPreferences.getString(CURRENCY, "");
            if (buyChipsUrl == null)
                buyChipsUrl = sharedPreferences.getString(BUY_CHIPS_ON, "");

            progressVolume = sharedPreferences.getInt(KEY_VOLUME, 10);
            Log.d(TAG_VOLUME, "loadVariables(): progressVolume = " + progressVolume);
            switchSound = sharedPreferences.getBoolean(KEY_SOUND, true);
            Log.d(TAG_VOLUME, "loadVariables(): switchSound = " + switchSound);

            //delays
            TIME_TOAST_DELAY = sharedPreferences.getInt(KEY_TIME_TOAST_DELAY, TIME_TOAST_DELAY_DEFAULT);
            TIME_ADDITIONAL_DELAY = sharedPreferences.getInt(KEY_TIME_ADDITIONAL_DELAY, TIME_ADDITIONAL_DELAY_DEFAULT);
        }
    }

    public static final void clearUserVariables(final Context _context) {
        operatorId = currency = userName = playerToken = tableId = liveVideoUrl = buyChipsUrl =
        tableLimits = tableTrnId = initId = pass = balance = null;
        boxLimits = null;

        final SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private static int myYear;

    public static final int getMyYear(){
        return myYear;
    }

    public static final void setMyYear(int _myYear){
        myYear = _myYear;
    }
}
