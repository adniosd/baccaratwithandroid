package com.vivogaming.livecasino.screens.game;

import android.app.Activity;
import android.widget.*;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.game.history.CustomViewHistory;
import com.vivogaming.livecasino.screens.game.history.CustomViewScore;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Variables.getCurrency;
import static com.vivogaming.livecasino.screens.game.Game.getGameActivity;
import static com.vivogaming.livecasino.screens.game.NotificationDataWorker.getCurrencySymbolByName;
import static com.vivogaming.livecasino.screens.game.NotificationDataWorker.getWinnerString;
import static com.vivogaming.livecasino.screens.game.ToastWorker.*;

public abstract class ViewWorker {
    /**
     * set history in view and invalidate
     * @param _customViewScore  history view
     * @param _history  string with history from server
     */
    public static final void drawGamePlayerHistory(final CustomViewHistory _customViewScore, final String _history) {
        _customViewScore.setHistory(_history);
        _customViewScore.invalidate();
    }

    public static final void drawHistory(final CustomViewScore _customViewScore, final String _history) {
        _customViewScore.setHistory(_history);
        _customViewScore.invalidate();
    }

    /**
     * set time on text view in format "5 sec"
     * @param _timeToBet
     */
    public static final void updateTimerView(final Activity _activity, final int _timeToBet) {
        final TextView tvTimer_GS = (TextView) _activity.findViewById(R.id.tvTimer_GS);
        final ProgressBar pbTimeProgress_SG = (ProgressBar) _activity.findViewById(R.id.pbTimeProgress_SG);
        tvTimer_GS.setText(_timeToBet + " " + tvTimer_GS.getContext().getString(R.string.sec));
        pbTimeProgress_SG.setProgress(_timeToBet);
        final int pbMax = pbTimeProgress_SG.getMax();

        final int[] colorGradient = _activity.getResources().getIntArray(R.array.color_gradient_array);
        final int colorNum = Math.min(colorGradient.length - 1, Math.round(((float) _timeToBet / pbMax) * (colorGradient.length - 1)));
        tvTimer_GS.setTextColor(colorGradient[colorNum]);
    }

    /**
     * set balance on text view in format "Balance: 520.0"
     * @param _activity
     * @param _playerBalance
     */
    public static final void updateBalanceView(final Activity _activity, final float _playerBalance) {
        final TextView _tvBalance_SG = (TextView) _activity.findViewById(R.id.tvBalance_SG);
        _tvBalance_SG.setText(getCurrencySymbolByName(getCurrency()) + _playerBalance);
    }

    /**
     * set data on limits views in right sliding panel
     * @param _tvLimitsValueRightPanel_SG
     * @param _tableLimits
     */
    public static final void updateTableLimitsView(final TextView _tvLimitsValueRightPanel_SG, String _tableLimits) {
        //deleting first symbol "-"
        if (_tableLimits.startsWith("-"))
            _tableLimits = _tableLimits.replace("-", "");
        _tvLimitsValueRightPanel_SG.setText(_tableLimits);
    }

    /**
     * set data on bets views in right sliding panel: last bet (total bets),
     * bet (total wins)
     * @param _tvLastBetValueRightPanel_SG
     * @param _totalBets
     * @param _tvBetValueRightPanel_SG
     * @param _totalWins
     */
    public static final void updateRightPanelBetsView(final TextView _tvLastBetValueRightPanel_SG, final String _totalBets,
                                                      final TextView _tvBetValueRightPanel_SG, final String _totalWins) {
        _tvLastBetValueRightPanel_SG.setText(_totalBets);
        _tvBetValueRightPanel_SG.setText(_totalWins);
    }

    /**
     * set text on tvBetAction_SG in Game screen
     * @param _actionStr
     */
    public static final void updateBetActionView(final Activity _activity, final String _actionStr) {
        final TextView tvBetAction_SG = (TextView) _activity.findViewById(R.id.tvBetAction_SG);
        tvBetAction_SG.setText(_actionStr);
    }

    /**
     * show winner in toast message
     * @param _resultSum
     */
    public static final void showWinner(final String _resultSum, final String _amount,
                                        final RelativeLayout _rlRoot_SG, final TextView _tvBetCount_SG) {
        if (_resultSum.equals("0")) return;
        /**
         * @author Sam 2014年3月21日
         * show winner
         */
        WinnerWorker.findWinnerViews(_rlRoot_SG, Integer.valueOf(_resultSum));
        showWinnerBoxToastWithDelay(getGameActivity());
        
        /**
         * delete by Sam
         */
        //if (_amount.equals("0"))
        //    showToastWithDelay(getGameActivity(), getWinnerString(_resultSum, _amount), _rlRoot_SG, _tvBetCount_SG);
        //else
        //    showWinnerToastWithDelay(getGameActivity(), getWinnerString(_resultSum, _amount), _rlRoot_SG, _tvBetCount_SG);
    }

    /**
     * recalculating layout with and height TIPMenu to screen
     */
    public static final void recalculateTipMenuSize(final ImageButton _imbTopMenu_SG, final ImageButton _imbTipMoney_SG,
                                                    final LinearLayout _llHeadMenuMoney_SG, final RelativeLayout _rlUpper_SG) {
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int marginLeftForTipMenu = 5 + 10 + _imbTopMenu_SG.getWidth() + (_imbTipMoney_SG.getWidth() / 2)
                - (_llHeadMenuMoney_SG.getWidth() / 2);
        layoutParams.setMargins(marginLeftForTipMenu, _rlUpper_SG.getHeight(), 0, 0);
        _llHeadMenuMoney_SG.setLayoutParams(layoutParams);
    }

    public static final void calculateAndSetSizeAndPositionsToBetZones(final RelativeLayout _rlAboveVideo_SG,
                                                                       final RelativeLayout _rlUpper_SG, final ImageView _ivPlayerPair_SG,
                                                                       final ImageView _ivPlayer_SG, final ImageView _ivTie_SG,
                                                                       final ImageView _ivBanker_SG, final ImageView _ivBankerPair_SG) {
        //V - video, U - upper bar
        //P - player, PP - player pair
        //T - tie
        //B - banker, BP - banker pair

        //w - width, h - height
        final int wV = _rlAboveVideo_SG.getWidth();
        final int hV = _rlAboveVideo_SG.getHeight();
        final int hU = _rlUpper_SG.getHeight();

        final int wP    = (int) (wV * WIDTH_COEFF_PLAYER);
        final int wPP   = (int) (wV * WIDTH_COEFF_PLAYER_PAIR);
        final int wT    = (int) (wV * WIDTH_COEFF_TIE);
        final int wBP   = (int) (wV * WIDTH_COEFF_BANKER_PAIR);
        final int wB    = (int) (wV * WIDTH_COEFF_BANKER);

        final int hP    = (int) (wP * HEIGHT_COEFF_PLAYER_BOX);
        final int hPP   = (int) (wPP * HEIGHT_COEFF_PLAYER_PAIR_BOX);
        final int hT    = (int) (wT * HEIGHT_COEFF_TIE_BOX);
        final int hBP   = (int) (wBP * HEIGHT_COEFF_BANKER_PAIR_BOX);
        final int hB    = (int) (wB * HEIGHT_COEFF_BANKER_BOX);

        final int centerX = wV / 2;

        //ox = offset X, oy = offset Y
        final int oxP   = (int) (wV * OFFSET_X_PLAYER_BOX);
        final int oyP   = (int) (hU + hV * OFFSET_Y_PLAYER_BANKER_BOX - hP);

        final int oxPP  = (int) (centerX - wPP);
        final int oyPP  = (int) (hU + hV * OFFSET_Y_PAIR_BOX - hPP);

        final int oxT   = (int) (centerX - wT / 2);
        final int oyT   = (int) (hU + hV * OFFSET_Y_TIE_BOX - hT);

        final int oxBP  = (int) (centerX);
        final int oyBP  = (int) (hU + hV * OFFSET_Y_PAIR_BOX - hBP);

        final int oxB   = (int) (wV * OFFSET_X_BANKER_BOX - wB);
        final int oyB   = (int) (hU + hV * OFFSET_Y_PLAYER_BANKER_BOX - hB);

        _ivPlayer_SG.setLayoutParams        (createLayoutParams(wP, hP, oxP, oyP));
        _ivPlayerPair_SG.setLayoutParams    (createLayoutParams(wPP, hPP, oxPP, oyPP));
        _ivTie_SG.setLayoutParams           (createLayoutParams(wT, hT, oxT, oyT));
        _ivBankerPair_SG.setLayoutParams    (createLayoutParams(wBP, hBP, oxBP, oyBP));
        _ivBanker_SG.setLayoutParams        (createLayoutParams(wB, hB, oxB, oyB));
    }

    private static final RelativeLayout.LayoutParams createLayoutParams(final int _width, final int _height,
                                                                        final int _offsetX, final int _offsetY) {
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(_width, _height);
        layoutParams.leftMargin = _offsetX;
        layoutParams.topMargin = _offsetY;
        return layoutParams;
    }

    /**
     * show "thank you" message
     * @param _activity
     */
    public static final void showThankYou(final Activity _activity, final int _chipImageResId) {
        showToastWithImage(_activity.getString(R.string.thank_you), _chipImageResId);
        Game.tipForDealerImageResId = 0;
    }




}