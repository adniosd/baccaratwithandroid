package com.vivogaming.livecasino.game_logic;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.game.FooterBoxWorker;
import com.vivogaming.livecasino.web.Request;

import java.util.HashMap;

import static com.vivogaming.livecasino.game_logic.BlockingChipPlacing.*;
import static com.vivogaming.livecasino.game_logic.ChipsPlacing.deleteAllChipsFormViewAndStacks;
import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Variables.*;
import static com.vivogaming.livecasino.screens.game.Game.getGameActivity;
import static com.vivogaming.livecasino.screens.game.ViewWorker.updateBetActionView;
import static com.vivogaming.livecasino.screens.game.ViewWorker.updateTimerView;
import static com.vivogaming.livecasino.web.RequestPool.asyncTask;

/**
 * class for handling game loop
 * timer, requests, blocking layout etc.
 */
public abstract class GameLoop {
    /*
        handler and runnable for handling getStatus requests
        timeToBet and nextCall ints used to handle
        placing bet and send request timers
     */
    private static int gTimeToBetFromServer = 0;
    private static int gMyTimeToBet = 0;        //gTimeToBetFromServer - Toast delay (10 sec) + 3
    private static int gNextCall = 0;

    static boolean initFooter = false;
    
    private static final Handler statusHandler = new Handler();
    public static final Runnable statusRunnable = new Runnable() {
        @Override
        public final void run() {
            final Activity activity = getGameActivity();

            if (gNextCall > 0) {
                gNextCall--;

                statusHandler.postDelayed(statusRunnable, 1000);
            } else {
                final String tableId = getTableId();
                final String playerToken = getPlayerToken();
                final String operatorId = getOperatorId();
                final String initId = getInitId();
                final String tableTrnId = getTableTrnId();

                asyncTask = new Request(activity, API_GET_STATUS);
                asyncTask.execute(tableId, playerToken, operatorId, initId, tableTrnId);
            }

            if (gTimeToBetFromServer > 0) {
                if (gTimeToBetFromServer > gMyTimeToBet) {   //this time layout is blocked
                    gTimeToBetFromServer--;
                    updateBetActionView(activity, activity.getString(R.string.no_more_bets));
                    setChipPlacingActivated(false); //chip placing must be blocked
                    //if must begin game time, activate chip placing, update text and timer
                    //called once
                    if (gTimeToBetFromServer == gMyTimeToBet) {
                        setChipPlacingActivated(true);
                        
                        /**
                         * Developer Sam
                         * 2014年3月29日
                         * hide and clear Footer Box 
                         */
                        FooterBoxWorker.hideFooterBox();
                        FooterBoxWorker.deleteAllChipsFromFooter();
                        ChipsPlacing.clearAllStacks();
                        FooterBoxWorker.updateFooterValues();
                        GameViewWorker.updateCurrentBet(activity);
                        
                        //deleteAllChipsFormViewAndStacks(activity);
                        updateBetActionView(activity, activity.getString(R.string.place_your_bets));
                        updateTimerView(activity, gTimeToBetFromServer);
                    }
                } else {
                    gTimeToBetFromServer--;
                    updateBetActionView(activity, activity.getString(R.string.place_your_bets));
                    updateTimerView(activity, gTimeToBetFromServer);
                }
            } else {
                updateBetActionView(activity, activity.getString(R.string.no_more_bets));
                //if time is over, chip are placed on boxes and player not confirm bets - need delete chips from table
                ChipsPlacing.hideAndMoveBetZone(activity);
                
                /**
                 * Developer Sam
                 * 2014年3月29日
                 * 
                 * hide and clear Footer Box ()
                 */
                FooterBoxWorker.deleteAllChipsFromFooter();
                FooterBoxWorker.initFooterChips();
                GameViewWorker.updateCurrentBet(activity);
                
                setChipPlacingActivated(false);
            }

            if (isChipPlacingAllowed()) {
                activateChipPlacing(activity);
            } else {
                deactivateChipPlacing(activity);
            }
        }
    };

    public static final void startGameLoop() {
        statusHandler.post(statusRunnable);
    }

    public static final void startGameLoopDelayed(final int _nextCall) {
        statusHandler.postDelayed(statusRunnable, _nextCall);
    }

    public static final void stopGameLoop() {
        statusHandler.removeCallbacks(statusRunnable);
    }

    public static final void updateTimeToBet(final int _timeToBet) {
        gTimeToBetFromServer = _timeToBet;
    }

    public static final void updateNextCall(final int _nextCall) {
        gNextCall = _nextCall;
    }

    public static final void updateMyTimeToBet(final int _myTimeToBet) {
        gMyTimeToBet = _myTimeToBet;
    }

    /**
     * handle next call logic
     * get next call time, choose bigger: next call or 2
     * if next call > TIME_FOR_PLACE_BETS (10) - start game loop normally
     * unblock chip placing and layout
     * otherwise block placing bets and start delayed, post runnable with this time
     * @param _errorMap
     */
    public static final void handleNextCall(final Activity _activity, final HashMap<String, String> _errorMap) {
        //delay between get status requests must be minimum 2 seconds
        final int nextCall = Math.max(2, Integer.valueOf(_errorMap.get(NEXT_CALL)));
        if (nextCall > TIME_FOR_PLACING_CARDS) { // todo: add calculating myTimeToBet
            updateNextCall(nextCall);
            updateTimeToBet(nextCall - TIME_FOR_PLACING_CARDS);
            updateMyTimeToBet(23);
            startGameLoop();
            activateChipPlacing(_activity);
            setChipPlacingActivated(true);
        } else {
            updateNextCall(nextCall);
            startGameLoopDelayed(nextCall);
            deactivateChipPlacing(_activity);
            setChipPlacingActivated(false);
        }
    }
}