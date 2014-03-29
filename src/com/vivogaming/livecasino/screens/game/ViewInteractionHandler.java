package com.vivogaming.livecasino.screens.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.*;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.web.Request;

import static com.vivogaming.livecasino.game_logic.BlockingChipPlacing.*;
import static com.vivogaming.livecasino.game_logic.ChipData.getImageIdByChipValue;
import static com.vivogaming.livecasino.game_logic.ChipData.getTipChipValueByResId;
import static com.vivogaming.livecasino.game_logic.ChipsPlacing.*;
import static com.vivogaming.livecasino.game_logic.LimitWorker.checkTableMinLimit;
import static com.vivogaming.livecasino.game_logic.LimitWorker.getBoxGameLimits;
import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Dialogs.*;
import static com.vivogaming.livecasino.global.KeyGenerator.generateGuid;
import static com.vivogaming.livecasino.global.KeyGenerator.generateHash;
import static com.vivogaming.livecasino.global.Variables.*;
import static com.vivogaming.livecasino.screens.game.AnimationHandler.*;
import static com.vivogaming.livecasino.web.RequestPool.asyncTask;
import static com.vivogaming.livecasino.web.RequestPool.cancelRequest;

public abstract class ViewInteractionHandler {

    public static final void onClickIvFooterBox_SG(final RelativeLayout _rootLayout, final int _ivBoxId, final int _checkedChipId,
                                                   final TextView _tvBetCount_SG) {
        if (_checkedChipId == -1 || !isChipPlacingAllowed()) return;

        placingChip(_rootLayout, _ivBoxId, _checkedChipId, _tvBetCount_SG);
    }

    public static final void startAnimationOpenCloseMainMenu(final Context _context,
                                                             final RelativeLayout _rlHeadMenu,
                                                             final ImageButton _imbTopMenu,
                                                             final LinearLayout _llOther,
                                                             final ImageButton _imbOther) {

        if (_rlHeadMenu.getVisibility() == View.INVISIBLE && _llOther.getVisibility() == View.INVISIBLE) {
            _imbTopMenu.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu_pressed));
            startOpenAnimationMenu(_rlHeadMenu, _imbTopMenu, _context);
        } else {
            if (_rlHeadMenu.getVisibility() == View.INVISIBLE && _llOther.getVisibility() == View.VISIBLE) {

                _llOther.post(new Runnable() {
                    public void run() {
                        try {
                            _imbOther.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu_money));
                            startCloseAnimationMenuMoney(_llOther, _imbOther, _context);

                            _rlHeadMenu.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    _imbTopMenu.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu_pressed));
                                    startOpenAnimationMenu(_rlHeadMenu, _imbTopMenu, _context);
                                }
                            }, 200);
                        } catch (Exception e) {
                        }
                    }
                });

            } else {
                _imbTopMenu.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu));
                startCloseAnimationMenu(_rlHeadMenu, _imbTopMenu, _context);
            }
        }
    }

    public static final void startAnimationOpenCloseTipsMenu(final Context _context,
                                                             final LinearLayout _llHeadMenuMoney,
                                                             final ImageButton _imbTopMenuMoney,
                                                             final RelativeLayout _rlOther,
                                                             final ImageButton _imbOther) {
        if (_llHeadMenuMoney.getVisibility() == View.INVISIBLE && _rlOther.getVisibility() == View.INVISIBLE) {

            _imbTopMenuMoney.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu_money_pressed));
            startOpenAnimationMenuMoney(_llHeadMenuMoney, _imbTopMenuMoney, _context);
        } else {
            if (_llHeadMenuMoney.getVisibility() == View.INVISIBLE && _rlOther.getVisibility() == View.VISIBLE) {

                _rlOther.post(new Runnable() {
                    public void run() {
                        try {
                            _imbOther.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu));
                            startCloseAnimationMenu(_rlOther, _imbOther, _context);

                            _llHeadMenuMoney.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    _imbTopMenuMoney.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu_money_pressed));
                                    startOpenAnimationMenuMoney(_llHeadMenuMoney, _imbTopMenuMoney, _context);
                                }
                            }, 200);
                        } catch (Exception e) {
                        }
                    }
                });

            } else {
                _imbTopMenuMoney.setImageDrawable(_context.getResources().getDrawable(R.drawable.btn_menu_money));
                startCloseAnimationMenuMoney(_llHeadMenuMoney, _imbTopMenuMoney, _context);
            }
        }
    }

    /**
     * check minimum limit, if ok
     * get needed data for placing best api and execute request
     */
    public static final void onClickBtnConfirm_SG(final Activity _activity) {
        if (!checkTableMinLimit(_activity)) return;

        final String tableId       = getTableId();
        final String playerToken   = getPlayerToken();
        final String operatorId    = getOperatorId();
        final String initId        = getInitId();
        final String newBets       = getNewBetsString();
        final String guid          = generateGuid();
        final String pass          = getPass();
        final String hash          = generateHash(tableId + playerToken + operatorId + guid + pass);

        setChipPlacingActivated(false);
        deactivateChipPlacing(_activity);
        new Request(_activity, API_NEW_BETS).execute(tableId, playerToken, operatorId, initId, newBets, guid, hash);
    }

    public static final void onClickBtnUndo_SG(final RelativeLayout _rootLayout, final TextView _tvBetCount_SG) {
        undoPreviousPlacing(_rootLayout, _tvBetCount_SG);
    }

    public static final void onClickBtnRepeat_SG(final Activity _activity,
                                                 final RelativeLayout _rootLayout, final TextView _tvBetCount_SG) {
        placeLastBetOnView(_activity, _rootLayout, _tvBetCount_SG);
    }

    /**
     * change image on click on RightSlidingMenu
     */
    public static final void changeImageCloseRightMenu(final int _widthSliderRight, final  int _rightToRight,
                                                       final ImageView ivCloseRightMenu_SG,
                                                       final RelativeLayout rlRightSlidingMenu_SG) {
        final int tpCoor = (int) rlRightSlidingMenu_SG.getX();
        if (tpCoor == _widthSliderRight + _rightToRight) {
            ivCloseRightMenu_SG.setImageResource(R.drawable.arrow_right);
        } else {
            ivCloseRightMenu_SG.setImageResource(R.drawable.arrow);
        }
    }

    /**
     * if link exist - start browser
     * @param _activity
     */
    public static final void onClickBtnCashier_SG(final Activity _activity) {
        final String buyChipsUrl = getBuyChipsUrl();
        if (buyChipsUrl == null || buyChipsUrl.isEmpty()) return;

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(buyChipsUrl));
        _activity.startActivity(intent);
    }

    /**
     * handle tip menu logic
     * @param _activity
     */
    public static final void onClickTipChips(final Activity _activity, final int _btnId, final LinearLayout _llHeadMenuMoney,
                                             final ImageButton _imbTopMenuMoney, final RelativeLayout _rlOther, final ImageButton _imbOther) {
        final String playerToken = getPlayerToken();
        final String operatorId = getOperatorId();
        final String tableId = getTableId();
        final String initId = getInitId();
        final String cmd = "PUT";
        final String amount = String.valueOf(getTipChipValueByResId(_btnId));

        //for toast; get chip number by button id, than get image resource id by that number
        Game.tipForDealerImageResId = getImageIdByChipValue(getTipChipValueByResId(_btnId));

        new Request(_activity, API_TIPS).execute(playerToken, operatorId, tableId, initId, cmd, amount);

        startAnimationOpenCloseTipsMenu(_activity, _llHeadMenuMoney, _imbTopMenuMoney, _rlOther, _imbOther);
    }

    public static final void onClickBtnLimits_SG(final Activity _activity,
                                                 final RelativeLayout _rlHeadMenu,
                                                 final ImageButton _imbTopMenu,
                                                 final LinearLayout _llOther,
                                                 final ImageButton _imbOther) {
        startAnimationOpenCloseMainMenu(_activity, _rlHeadMenu, _imbTopMenu, _llOther, _imbOther);
        final int[][] limitMatrix = getBoxGameLimits();
        showDialogLimitsContent(_activity, limitMatrix);
    }

    public static final void onClickBtnHelp_SG(final Activity _activity,
                                               final RelativeLayout _rlHeadMenu,
                                               final ImageButton _imbTopMenu,
                                               final LinearLayout _llOther,
                                               final ImageButton _imbOther) {
        startAnimationOpenCloseMainMenu(_activity, _rlHeadMenu, _imbTopMenu, _llOther, _imbOther);
        showDialogHelpContent(_activity);
    }

    public static final void onClickBtnSettings_SG(final Activity _activity,
                                                   final RelativeLayout _rlHeadMenu,
                                                   final ImageButton _imbTopMenu,
                                                   final LinearLayout _llOther,
                                                   final ImageButton _imbOther) {
        startAnimationOpenCloseMainMenu(_activity, _rlHeadMenu, _imbTopMenu, _llOther, _imbOther);
        showDialogSettingsContent(_activity);
    }

    public static final void onClickBtnHistory_SG(final Activity _activity,
                                                  final RelativeLayout _rlHeadMenu,
                                                  final ImageButton _imbTopMenu,
                                                  final LinearLayout _llOther,
                                                  final ImageButton _imbOther,
                                                  final String _resultGamePlayerHistory) {
        startAnimationOpenCloseMainMenu(_activity, _rlHeadMenu, _imbTopMenu, _llOther, _imbOther);
        showDialogHistoryContent(_activity, _resultGamePlayerHistory);
    }

    public static final void onClickBtnLobby_SG(final Activity _activity,
                                                final RelativeLayout _rlHeadMenu,
                                                final ImageButton _imbTopMenu,
                                                final LinearLayout _llOther,
                                                final ImageButton _imbOther) {
        doRequestGetActiveTables(_activity);
        startAnimationOpenCloseMainMenu(_activity, _rlHeadMenu, _imbTopMenu, _llOther, _imbOther);
    }

    public static final void doRequestGetActiveTables(final Activity _activity) {
        //need stop request due to get status response getStatus appears on ChooseTable screen, but started in Game screen
        cancelRequest();

        final String operatorId = getOperatorId();
        final String currency = getCurrency();

        //send request for active tables
        asyncTask = new Request(_activity, API_GET_ACTIVE_TABLES);
        asyncTask.execute(operatorId, currency);
    }
}