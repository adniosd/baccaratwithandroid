package com.vivogaming.livecasino.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.screens.Login;
import com.vivogaming.livecasino.screens.choose_table.ChooseTable;
import com.vivogaming.livecasino.screens.game.Game;
import com.vivogaming.livecasino.screens.register.Register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import static com.vivogaming.livecasino.game_logic.BlockingChipPlacing.activateChipPlacing;
import static com.vivogaming.livecasino.game_logic.BlockingChipPlacing.setChipPlacingActivated;
import static com.vivogaming.livecasino.game_logic.ChipsPlacing.deleteAllChipsFormViewAndStacks;
import static com.vivogaming.livecasino.game_logic.ChipsPlacing.updateRepeatStack;
import static com.vivogaming.livecasino.game_logic.GameLoop.*;
import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Dialogs.showBetOrTipFailDialog;
import static com.vivogaming.livecasino.global.Dialogs.showErrorDialog;
import static com.vivogaming.livecasino.global.Logout.setLogoutTime;
import static com.vivogaming.livecasino.global.Network.getIpAddress;
import static com.vivogaming.livecasino.global.Variables.*;
import static com.vivogaming.livecasino.screens.game.Game.updateMainPlayingDataCommon;
import static com.vivogaming.livecasino.screens.game.LiveVideoWorker.pauseVideo;
import static com.vivogaming.livecasino.screens.game.ToastWorker.showToast;
import static com.vivogaming.livecasino.screens.game.ViewWorker.showThankYou;
import static com.vivogaming.livecasino.screens.game.ViewWorker.updateBalanceView;
import static com.vivogaming.livecasino.web.Parser.parseNextCallError;
import static com.vivogaming.livecasino.web.RequestPool.asyncTask;
import static com.vivogaming.livecasino.web.WebObservable.deleteMyObserver;

public abstract class ResponseWorker {

    /**
     * when error is appears
     * if error is critical - delete observer, finish activity
     * otherwise just close dialog
     * @param _activity
     */
    public static final void responseError(final Activity _activity, final HashMap<String, String> _errorMap) {
        showErrorDialog(_activity, _errorMap);
    }

    public static final void responseApiLogin(final Activity _activity, final HashMap<String, String> _loginMap) {
        final String operatorId = _loginMap.get(OPERATOR_ID);
        final String currency = _loginMap.get(CURRENCY);
        final String playerToken = _loginMap.get(TOKEN);
        final String balance = _loginMap.get(BALANCE);

        setOperatorID(operatorId);
        setCurrency(currency);
        setPlayerToken(playerToken);
        setBalance(balance);    //for displaying on ChooseTable screen

        //send request for active tables
        //
        asyncTask = new Request(_activity, API_GET_ACTIVE_TABLES);
        asyncTask.execute(operatorId, currency);
    }

    public static final void responseApiGetActiveTables(final Activity _activity, final Observer _observer,
                                                        final ArrayList<TableObject> _activeTableList) {
        //delete observer only in Login screen; no need deleting in Game screen
        //due to deleting it in onStop()
        if (_activity instanceof Login || _activity instanceof Register) {
            deleteMyObserver(_activity, _observer);
        }

        if (_activity instanceof Game) {
            setLogoutTime(_activity, 0);
            stopGameLoop();
            pauseVideo();
            setTableTrnId("");
            deleteAllChipsFormViewAndStacks(_activity);
        }
        saveVariables(_activity);

        final Intent intent = new Intent(_activity, ChooseTable.class);
        intent.putExtra("tableList", _activeTableList);
        _activity.startActivity(intent);
        _activity.finish();
    }

    public static final void responseApiInit(final Activity _activity, final HashMap<String, String> _initMap) {
        final String liveVideoUrl       = _initMap.get(LIVE_VIDEO_URL);
        final String buyChipsUrl        = _initMap.get(BUY_CHIPS_ON);
        final String tableLimits        = _initMap.get(TABLE_LIMITS);
        final String playerPairLimit    = _initMap.get(PLAYER_PAIR_BOX_NUM);
        final String playerLimit        = _initMap.get(PLAYER_BOX_NUM);
        final String tieLimit           = _initMap.get(TIE_BOX_NUM);
        final String bankerLimit        = _initMap.get(BANKER_BOX_NUM);
        final String bankerPairLimit    = _initMap.get(BANKER_PAIR_BOX_NUM);

        setLiveVideoUrl(liveVideoUrl);  //for video view on Game screen
        setBuyChipsUrl(buyChipsUrl);    //for button Cashier on Game screen
        setTableLimits(tableLimits);
        setBoxLimits(playerPairLimit, playerLimit, tieLimit, bankerLimit, bankerPairLimit);

        final String tableId = getTableId();
        final String playerToken = getPlayerToken();
        final String operatorId = getOperatorId();
        final String initId = _initMap.get(WINDOW_ID);
        final String tableTrnId = getTableTrnId();

        setInitId(initId);

        //send get status request
        asyncTask = new Request(_activity, API_GET_STATUS);
        asyncTask.execute(tableId, playerToken, operatorId, initId, tableTrnId);
    }

    /**
     * call when get response from fist call getStatus (in ChooseTable's update)
     * adding to map some extra data
     * @param _activity
     * @param _statusMap
     */
    public static final void responseApiGetStatusFirst(final Activity _activity, final Observer _observer,
                                                       final HashMap<String, String> _statusMap) {
        deleteMyObserver(_activity, _observer);
        saveVariables(_activity);
        setLogoutTime(_activity, 0);

//        final String liveVideoUrl       = getLiveVideoUrl();
        final String tableLimits        = getTableLimits();
        final String[] boxLimits        = getBoxLimits();
        final String playerPairLimit    = boxLimits[0];
        final String playerLimit        = boxLimits[1];
        final String tieLimit           = boxLimits[2];
        final String bankerLimit        = boxLimits[3];
        final String bankerPairLimit    = boxLimits[4];

//        _statusMap.put(LIVE_VIDEO_URL, liveVideoUrl);
        //limits
        _statusMap.put(TABLE_LIMITS, tableLimits);
        _statusMap.put(PLAYER_PAIR_BOX_NUM, playerPairLimit);
        _statusMap.put(PLAYER_BOX_NUM, playerLimit);
        _statusMap.put(TIE_BOX_NUM, tieLimit);
        _statusMap.put(BANKER_BOX_NUM, bankerLimit);
        _statusMap.put(BANKER_PAIR_BOX_NUM, bankerPairLimit);

        //in first time get status, we don't need timer values
        //and winner
        _statusMap.put(TIME_TO_BET, "0");
        _statusMap.put(NEXT_CALL, "0");
        _statusMap.put(RESULT_SUM, "0");

        final Intent intent = new Intent(_activity, Game.class);
        intent.putExtra("statusMap", _statusMap);
        _activity.startActivity(intent);
        _activity.finish();
    }

    /**
     * handle response api get status on main screen in game loop
     * @param _statusMap
     * @param _rlRoot_SG
     * @param _tvBetCount_SG
     */
    public static final void responseApiGetStatusCommon(final HashMap<String, String> _statusMap, final RelativeLayout _rlRoot_SG,
                                                        final TextView _tvBetCount_SG) {
        updateMainPlayingDataCommon(_statusMap);
        setChipPlacingActivated(true);
        startGameLoop();
    }

    /**
     * Response from server to Register screen;
     * @param _activity
     * @param _context
     */
    public static final void responseApiRegister(final Activity _activity, final Context _context){
        final String name = getUserName();
        final String pass = getPass();
        final String operatorKey = OPERATOR_ID_NUM;
        final String ip = getIpAddress(_context);

        // send request for login after register
        new Request(_activity, API_LOGIN).execute(name, pass, operatorKey, ip);
    }

    /**
     * handle api new bets response
     *
     * @param _activity
     * @param _newBetsMap
     */
    public static final void responseApiNewBets(final Activity _activity, final HashMap<String, String> _newBetsMap) {
        if (_newBetsMap.containsKey(FAIL)) {
            //unblock chip placing, show dialog and proceed game
            setChipPlacingActivated(true);
            activateChipPlacing(_activity);
            final String placingBetsError = _activity.getString(R.string.your_bet_could_not_be_approved);
//            showBetOrTipFailDialog(_activity, _newBetsMap.get(DESCRIPTION));
            showBetOrTipFailDialog(_activity, placingBetsError);
        } else {
            updateRepeatStack();
            showToast(_activity.getString(R.string.your_bet_is_confirmed));
            updateBalanceView(_activity, Float.valueOf(_newBetsMap.get(NEW_BALANCE)));
        }
    }

    /**
     * handle api tips response
     * if fail - show dialog and proceed game
     * else - udpate balance text and show toast
     * @param _activity
     * @param _tipsMap
     */
    public static final void responseApiTips(final Activity _activity, final HashMap<String, String> _tipsMap) {
        if (_tipsMap.containsKey(FAIL)) {
            showBetOrTipFailDialog(_activity, _tipsMap.get(DESCRIPTION));
        } else {
            updateBalanceView(_activity, Float.valueOf(_tipsMap.get(NEW_BALANCE)));
            showThankYou(_activity, Game.tipForDealerImageResId);
        }
    }

    /**
     * handle response errors on game screen
     * if status code != 200 than it's critical error, stop game
     * else if it's 103 error - handle next call, proceed game
     * @param _activity
     * @param _errorMap
     */
    public static final void responseGameError(final Activity _activity, final Observer _observer, final HashMap<String, String> _errorMap) {
        final String statusCode = _errorMap.get(STATUS_CODE);

        if (statusCode.equals("200")) {
            final String errorDescription = _errorMap.get(ERROR_DESCRIPTION);

            //next call error?
            if (errorDescription.contains(ERROR_103)) {
                final HashMap<String, String> gameErrorMap = parseNextCallError(errorDescription);
                handleNextCall(_activity, gameErrorMap);
                return;
            }
        }

        //critical error
        deleteMyObserver(_activity, _observer);
        stopGameLoop();
        pauseVideo();
        responseError(_activity, _errorMap);
    }
}