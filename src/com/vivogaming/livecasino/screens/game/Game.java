package com.vivogaming.livecasino.screens.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.FooterBoxSelector;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.screens.game.history.CustomViewScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.vivogaming.livecasino.game_logic.BlockingChipPlacing.setChipPlacingActivated;
import static com.vivogaming.livecasino.game_logic.GameLoop.*;
import static com.vivogaming.livecasino.game_logic.LimitWorker.setBoxGameLimits;
import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.Dialogs.showDialogGoToLobby;
import static com.vivogaming.livecasino.global.Logout.checkLogoutTime;
import static com.vivogaming.livecasino.global.Logout.setLogoutTime;
import static com.vivogaming.livecasino.global.System.loadVolumeSound;
import static com.vivogaming.livecasino.global.Variables.*;
import static com.vivogaming.livecasino.screens.game.AnimationHandler.animationLeftGridScore;
import static com.vivogaming.livecasino.screens.game.AnimationHandler.animationRightSlidingMenu;
import static com.vivogaming.livecasino.screens.game.LiveVideoWorker.pauseVideo;
import static com.vivogaming.livecasino.screens.game.LiveVideoWorker.playVideo;
import static com.vivogaming.livecasino.screens.game.NotificationDataWorker.getCurrencySymbolByName;
import static com.vivogaming.livecasino.screens.game.ToastWorker.prepareToastWithImage;
import static com.vivogaming.livecasino.screens.game.ToastWorker.prepareToastWithText;
import static com.vivogaming.livecasino.screens.game.ViewInteractionHandler.*;
import static com.vivogaming.livecasino.screens.game.ViewWorker.*;
import static com.vivogaming.livecasino.web.ResponseWorker.*;
import static com.vivogaming.livecasino.web.WebObservable.addMyObserver;
import static com.vivogaming.livecasino.web.WebObservable.deleteMyObserver;

public final class Game extends Activity implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener,
                                            Observer {
    private static String resultGamePlayerHistory;
    /**
     * recalculating layout under CustomViewScore grid to various screens
     */
    private static ChipsRadioGroup rgChips_GS;
    private static CustomViewScore customViewScore;
    private static RelativeLayout rlRoot_SG;
    private RelativeLayout rlRightPanel_SG ,rlLeftPanelGrid_SG;
    private ImageView ivPlayerPair_SG, ivPlayer_SG, ivTie_SG, ivBanker_SG, ivBankerPair_SG, ivCloseLeftMenu_GS ,ivCloseRightMenu_SG;
    private ImageButton imbTopMenu_SG, imbTipMoney_SG, btnTipChip1_SG, btnTipChip5_SG, btnTipChip10_SG, btnTipChip25_SG,
            btnConfirm_SG, btnUndo_SG, btnRepeat_SG;
    private RelativeLayout rlHeadMenu_SG, rlUpper_SG, rlRightSlidingMenu_SG, rlAboveVideo_SG;
    private static RelativeLayout rlBlock_SG;
    private Button btnLimits_SG, btnHistory_SG, btnSettings_SG, btnHelp_SG, btnLobby_SG, btnCashier_SG;
    private LinearLayout llHeadMenuMoney_SG;
    private static TextView tvBalance_SG, tvLimitsValueRightPanel_SG, tvLastBetValueRightPanel_SG, tvBetValueRightPanel_SG,
        tvTimer_GS, tvBetCount_SG, tvBetAction_SG;
    private static ProgressBar pbTimeProgress_SG;

    private int leftToLeft, widthSliderLeft, rightToRight, widthSliderRight;
    private static Activity gActivity;

    public static int tipForDealerImageResId = 0;

    @Override
    public final void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.screen_game);

        gActivity = this;

        findViews();
        setListeners();

        /**
         * Developer Sam
         * 2014年3月29日
         * 
         * find footer bar
         */
        FooterBoxWorker.getFooterBox(rlRoot_SG, R.id.lineRoles_SG);
        /**
         * Developer Sam
         * 2014年3月29日
         * 
         * set tvBetCount_SG and tvBalance_SG font type and Currency Symbol
         */
        setFontTypeAndCurrencySymbol();
        
        loadVariables(this);

        final HashMap<String, String> statusMap = (HashMap<String, String>) getIntent().getSerializableExtra("statusMap");
        updateExtraPlayingData(statusMap);
        updateMainPlayingDataFirst(statusMap);

        rlRightSlidingMenu_SG.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected final void onStart() {
        super.onStart();
        
        /**
         * Developer Sam
         * 2014年3月29日
         * hide Footer Box height
         */
        FooterBoxWorker.hideFooterBox();
        
        loadVolumeSound(this);
        checkLogoutTime(this);
        addMyObserver(this, this);
        startGameLoop();
        setChipPlacingActivated(true);
        playVideo(this, getLiveVideoUrl());
        swipePanels();
    }

    @Override
    public final void onStop() {
        super.onStop();
        deleteMyObserver(this, this);
        stopGameLoop();

        setLogoutTime(this, System.currentTimeMillis());

        //set timers to zero and next time when screen appears, it send request get status
        updateTimeToBet(0);
        updateNextCall(0);
        updateTimerView(this, 0);

        saveVariables(this);
        pauseVideo();
    }

    private final void findViews() {
        //menu elements
        btnLimits_SG            = (Button) findViewById(R.id.btnLimits_SG);
        btnHelp_SG              = (Button) findViewById(R.id.btnHelp_SG);
        btnHistory_SG           = (Button) findViewById(R.id.btnHistory_SG);
        btnSettings_SG          = (Button) findViewById(R.id.btnSettings_SG);
        btnLobby_SG             = (Button) findViewById(R.id.btnLobby_SG);

        //tip elements
        btnTipChip1_SG          = (ImageButton) findViewById(R.id.btnTipChip1_SG);
        btnTipChip5_SG          = (ImageButton) findViewById(R.id.btnTipChip5_SG);
        btnTipChip10_SG         = (ImageButton) findViewById(R.id.btnTipChip10_SG);
        btnTipChip25_SG         = (ImageButton) findViewById(R.id.btnTipChip25_SG);

        rgChips_GS              = (ChipsRadioGroup) findViewById(R.id.rgChips_GS);
        rlUpper_SG              = (RelativeLayout) findViewById(R.id.rlUpper_SG);
        rlRightPanel_SG         = (RelativeLayout) findViewById(R.id.rlRightPanel_SG);
        rlRoot_SG               = (RelativeLayout) findViewById(R.id.rlRoot_SG);
        rlHeadMenu_SG           = (RelativeLayout) findViewById(R.id.top_head_menu_SG);
        imbTopMenu_SG           = (ImageButton) findViewById(R.id.btnMenu_GS);
        imbTipMoney_SG          = (ImageButton) findViewById(R.id.btnMoneyMenu_GS);
        llHeadMenuMoney_SG      = (LinearLayout) findViewById(R.id.top_head_menu_money_SG);
        rlRightPanel_SG         = (RelativeLayout) findViewById(R.id.rlRightPanel_SG);

        //footer box elements
        ivPlayerPair_SG         = (ImageView) findViewById(R.id.ivPlayerPair_SG);
        ivPlayer_SG             = (ImageView) findViewById(R.id.ivPlayer_SG);
        ivTie_SG                = (ImageView) findViewById(R.id.ivTie_SG);
        ivBanker_SG             = (ImageView) findViewById(R.id.ivBanker_SG);
        ivBankerPair_SG         = (ImageView) findViewById(R.id.ivBankerPair_SG);

        //sliding menu element
        rlLeftPanelGrid_SG      = (RelativeLayout) findViewById(R.id.rlLeftPanelGrid_SG);
        rlRightSlidingMenu_SG   = (RelativeLayout) findViewById(R.id.rlRightSlidingMenu_SG);
        ivCloseRightMenu_SG     = (ImageView) findViewById(R.id.ivCloseRightMenu_SG);
        ivCloseLeftMenu_GS      = (ImageView) findViewById(R.id.ivCloseLeftMenu_GS);
        customViewScore         = (CustomViewScore) findViewById(R.id.cgHistory_GS);

        btnConfirm_SG           = (ImageButton) findViewById(R.id.btnConfirm_SG);
        btnUndo_SG              = (ImageButton) findViewById(R.id.btnUndo_SG);
        btnRepeat_SG            = (ImageButton) findViewById(R.id.btnRepeat_SG);

        tvBalance_SG                    = (TextView) findViewById(R.id.tvBalance_SG);
        tvLimitsValueRightPanel_SG      = (TextView) findViewById(R.id.tvLimitsValueRightPanel_SG);
        tvLastBetValueRightPanel_SG     = (TextView) findViewById(R.id.tvLastBetValueRightPanel_SG);
        tvBetValueRightPanel_SG         = (TextView) findViewById(R.id.tvBetValueRightPanel_SG);
        tvTimer_GS                      = (TextView) findViewById(R.id.tvTimer_GS);
        tvBetCount_SG                   = (TextView) findViewById(R.id.tvBetCount_SG);
        tvBetAction_SG                  = (TextView) findViewById(R.id.tvBetAction_SG);

        rlBlock_SG              = (RelativeLayout) findViewById(R.id.rlBlock_SG);
        rlAboveVideo_SG         = (RelativeLayout) findViewById(R.id.rlAboveVideo_SG);
        btnCashier_SG           = (Button) findViewById(R.id.btnCashier_SG);

        pbTimeProgress_SG       = (ProgressBar) findViewById(R.id.pbTimeProgress_SG);
    }

    private final void setListeners() {
        btnConfirm_SG.setOnClickListener(this);
        btnUndo_SG.setOnClickListener(this);

        ivPlayerPair_SG.setOnClickListener(this);
        ivPlayer_SG.setOnClickListener(this);
        ivTie_SG.setOnClickListener(this);
        ivBanker_SG.setOnClickListener(this);
        ivBankerPair_SG.setOnClickListener(this);

        imbTopMenu_SG.setOnClickListener(this);
        btnLimits_SG.setOnClickListener(this);
        btnHelp_SG.setOnClickListener(this);
        btnHistory_SG.setOnClickListener(this);
        btnSettings_SG.setOnClickListener(this);
        btnLobby_SG.setOnClickListener(this);

        imbTipMoney_SG.setOnClickListener(this);
        btnTipChip1_SG.setOnClickListener(this);
        btnTipChip5_SG.setOnClickListener(this);
        btnTipChip10_SG.setOnClickListener(this);
        btnTipChip25_SG.setOnClickListener(this);

        ivCloseLeftMenu_GS.setOnClickListener(this);
        ivCloseRightMenu_SG.setOnClickListener(this);

        btnCashier_SG.setOnClickListener(this);
        btnRepeat_SG.setOnClickListener(this);
    }

    /**
     * invoke when layouts size is calculated
     */
    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
    @Override
    public final void onGlobalLayout() {
        if (Build.VERSION.SDK_INT < 16) {
            rlRightPanel_SG.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            rlRightPanel_SG.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recalculateLeftAndRightSlideMenu(metrics);
//        calculateAndSetSizeAndPositionsToBetZones(rlAboveVideo_SG, rlUpper_SG, ivPlayerPair_SG, ivPlayer_SG, ivTie_SG, ivBanker_SG, ivBankerPair_SG);
        recalculateTipMenuSize(imbTopMenu_SG, imbTipMoney_SG, llHeadMenuMoney_SG, rlUpper_SG);

        prepareToastWithText(this, metrics, rlRightPanel_SG);
        prepareToastWithImage(this, metrics, rlRightPanel_SG);
    }

    /**
     * recalculate left and right slide menu for animation
     */
    private final void recalculateLeftAndRightSlideMenu(final DisplayMetrics _metrics) {
        // animation recalculate
        final float density          = _metrics.density;                //screen`s density

        final int widthCustomScore   = customViewScore.getWidth();
        final int tempWidthRightIv   = ivCloseRightMenu_SG.getWidth();
        final int tempWidthRightRl   = rlRightSlidingMenu_SG .getWidth();

        final int widthRightRL       = rlLeftPanelGrid_SG.getWidth();
        final int widthRightIV       = ivCloseLeftMenu_GS.getWidth();
        int location[] = new int[2];
        ivCloseLeftMenu_GS.getLocationOnScreen(location);

        rightToRight                 = tempWidthRightRl - tempWidthRightIv;
        widthSliderRight             = (int) (widthRightRL + getResources().getDimension(R.dimen.margin_sliding_menu) * density);
        leftToLeft                   = widthRightRL - widthRightIV;
        widthSliderLeft              = leftToLeft - widthCustomScore;

        //first run of view with coordinate
        rlLeftPanelGrid_SG.setX(-leftToLeft);
        rlRightSlidingMenu_SG.setX(widthSliderRight + rightToRight);
    }

    public static final Activity getGameActivity() {
        return gActivity;
    }

    /**
     * handle click events
     * @param _view
     */
    @Override
    public final void onClick(final View _view) {
        final int id = _view.getId();

        switch (id) {
            //boxes
            case R.id.ivPlayerPair_SG:
            case R.id.ivPlayer_SG:
            case R.id.ivTie_SG:
            case R.id.ivBanker_SG:
            case R.id.ivBankerPair_SG:
                onClickIvFooterBox_SG(rlRoot_SG, id, rgChips_GS.getCheckedRadioButtonId(), tvBetCount_SG);
                break;

            //top menu
            case R.id.btnMenu_GS:
                startAnimationOpenCloseMainMenu(this, rlHeadMenu_SG, imbTopMenu_SG, llHeadMenuMoney_SG, imbTipMoney_SG);
                break;
            case R.id.btnLimits_SG:
                onClickBtnLimits_SG(this, rlHeadMenu_SG, imbTopMenu_SG, llHeadMenuMoney_SG, imbTipMoney_SG);
                break;
            case R.id.btnHelp_SG:
                onClickBtnHelp_SG(this, rlHeadMenu_SG, imbTopMenu_SG, llHeadMenuMoney_SG, imbTipMoney_SG);
                break;
            case R.id.btnSettings_SG:
                onClickBtnSettings_SG(this, rlHeadMenu_SG, imbTopMenu_SG, llHeadMenuMoney_SG, imbTipMoney_SG);
                break;
            case R.id.btnHistory_SG:
                onClickBtnHistory_SG(this, rlHeadMenu_SG, imbTopMenu_SG, llHeadMenuMoney_SG, imbTipMoney_SG, resultGamePlayerHistory);
                break;
            case R.id.btnLobby_SG:
                onClickBtnLobby_SG(this, rlHeadMenu_SG, imbTopMenu_SG, llHeadMenuMoney_SG, imbTipMoney_SG);
                break;

            //top chip menu
            case R.id.btnMoneyMenu_GS:
                startAnimationOpenCloseTipsMenu(this, llHeadMenuMoney_SG, imbTipMoney_SG, rlHeadMenu_SG, imbTopMenu_SG);
                break;
            case R.id.btnTipChip1_SG:
            case R.id.btnTipChip5_SG:
            case R.id.btnTipChip10_SG:
            case R.id.btnTipChip25_SG:
                onClickTipChips(this, id, llHeadMenuMoney_SG, imbTipMoney_SG, rlHeadMenu_SG, imbTopMenu_SG);
                break;

            case R.id.btnConfirm_SG:
                onClickBtnConfirm_SG(this);
                break;
            case R.id.btnUndo_SG:
                onClickBtnUndo_SG(rlRoot_SG, tvBetCount_SG);
                break;
            case R.id.btnRepeat_SG:
                onClickBtnRepeat_SG(this, rlRoot_SG, tvBetCount_SG);
                break;

            case R.id.btnCashier_SG:
                onClickBtnCashier_SG(this);
                break;
        }
    }

    /**
     * extra data, added after fist getStatus response
     * @param _statusMap
     */
    private final void updateExtraPlayingData(final HashMap<String, String> _statusMap) {
        final String tableLimits        = _statusMap.get(TABLE_LIMITS);
        final String playerPairLimits   = _statusMap.get(PLAYER_PAIR_BOX_NUM);
        final String playerLimits       = _statusMap.get(PLAYER_BOX_NUM);
        final String tieLimits          = _statusMap.get(TIE_BOX_NUM);
        final String bankerLimits       = _statusMap.get(BANKER_BOX_NUM);
        final String bankerPairLimits   = _statusMap.get(BANKER_PAIR_BOX_NUM);

        updateTableLimitsView(tvLimitsValueRightPanel_SG, tableLimits);
        setBoxGameLimits(tableLimits, playerPairLimits, playerLimits, tieLimits, bankerLimits, bankerPairLimits);
    }

    /**
     * main data, got from getStatus response
     * @param _statusMap
     */
    public static final void updateMainPlayingDataFirst(final HashMap<String, String> _statusMap) {
        final String tableTrnId = _statusMap.get(TABLE_TRN_ID);
        setTableTrnId(tableTrnId);      //need save tableTrnId for future requests

        final float playerBalance = Float.valueOf(_statusMap.get(PLAYER_BALANCE));
        updateBalanceView(gActivity, playerBalance);

        final String totalBets = _statusMap.get(TOTAL_BETS);
        final String totalWins = _statusMap.get(TOTAL_WINS);
        updateRightPanelBetsView(tvLastBetValueRightPanel_SG, totalBets, tvBetValueRightPanel_SG, totalWins);

        final String resultHistory = _statusMap.get(RESULT_HISTORY);
        drawHistory(customViewScore, resultHistory);

        resultGamePlayerHistory = resultHistory;

        //if values is zero than it's a first launch, no need to change values
        //otherwise need add additional time
        int nextCall = Integer.valueOf(_statusMap.get(NEXT_CALL));
        int timeToBet = Integer.valueOf(_statusMap.get(TIME_TO_BET));
        nextCall = nextCall == 0 ? 0 : nextCall + TIME_ADDITIONAL_DELAY;
        timeToBet = timeToBet == 0 ? 0 : timeToBet + TIME_ADDITIONAL_DELAY;
        final int myTimeToBet = timeToBet == 0 ? 0 : timeToBet - TIME_TOAST_DELAY - TIME_SHOWING_TOAST - TIME_DELAY_AFTER_TOAST;

        updateNextCall(nextCall);
        updateTimeToBet(timeToBet);

        updateMyTimeToBet(myTimeToBet);
        //first time timeToBet == 0, must set some number to max
        pbTimeProgress_SG.setMax(timeToBet == 0 ? 23 : myTimeToBet);
        pbTimeProgress_SG.setProgress(0);

        //winner
//        final String resultSum = _statusMap.get(RESULT_SUM);
//        showWinner(resultSum, totalWins, rlRoot_SG, tvBetCount_SG);
    }

    /**
     * main data, got from getStatus response
     * @param _statusMap
     */
    public static final void updateMainPlayingDataCommon(final HashMap<String, String> _statusMap) {
        final String tableTrnId = _statusMap.get(TABLE_TRN_ID);
        setTableTrnId(tableTrnId);      //need save tableTrnId for future requests

        final float playerBalance = Float.valueOf(_statusMap.get(PLAYER_BALANCE));
        final String totalBets = _statusMap.get(TOTAL_BETS);
        final String totalWins = _statusMap.get(TOTAL_WINS);
        final String resultHistory = _statusMap.get(RESULT_HISTORY);

        //need update player balance and history when win notification showing
        tvLastBetValueRightPanel_SG.postDelayed(new Runnable() {
            @Override
            public final void run() {
                updateBalanceView(gActivity, playerBalance);
                updateRightPanelBetsView(tvLastBetValueRightPanel_SG, totalBets, tvBetValueRightPanel_SG, totalWins);
                drawHistory(customViewScore, resultHistory);
                resultGamePlayerHistory = resultHistory;
            }
        }, (TIME_TOAST_DELAY - 1) * 1000);

        //if values is zero than it's a first launch, no need to change values
        //otherwise need add additional time
        int nextCall = Integer.valueOf(_statusMap.get(NEXT_CALL));
        int timeToBet = Integer.valueOf(_statusMap.get(TIME_TO_BET));
        Log.d(TAG_TIMINGS, "---- Before -----");
        Log.d(TAG_TIMINGS, "nextCall = " + nextCall);
        Log.d(TAG_TIMINGS, "timeToBet = " + timeToBet);
        nextCall = nextCall == 0 ? 0 : nextCall + TIME_ADDITIONAL_DELAY;
        timeToBet = timeToBet == 0 ? 0 : timeToBet + TIME_ADDITIONAL_DELAY;
        final int myTimeToBet = timeToBet == 0 ? 0 : timeToBet - TIME_TOAST_DELAY - TIME_SHOWING_TOAST - TIME_DELAY_AFTER_TOAST;
        Log.d(TAG_TIMINGS, "---- After -----");
        Log.d(TAG_TIMINGS, "nextCall = " + nextCall);
        Log.d(TAG_TIMINGS, "timeToBet = " + timeToBet);
        Log.d(TAG_TIMINGS, "myTimeToBet = " + myTimeToBet);
        Log.d(TAG_TIMINGS, "-----------------");

        updateNextCall(nextCall);
        updateTimeToBet(timeToBet);

        updateMyTimeToBet(myTimeToBet);
        //first time timeToBet == 0, must set some number to max
        pbTimeProgress_SG.setMax(timeToBet == 0 ? 23 : myTimeToBet);
        pbTimeProgress_SG.setProgress(0);

        //winner
        final String resultSum = _statusMap.get(RESULT_SUM);
//        showWinner(resultSum, totalWins, rlRoot_SG, tvBetCount_SG);
        
        /**
         * Developer Sam
         * 2014年3月29日
         * 
         * show winner
         */
        LinearLayout line = (LinearLayout)rlRoot_SG.findViewById(R.id.lineRoles_SG);
        line.setVisibility(View.VISIBLE);
        showWinner(resultSum, totalWins, rlRoot_SG, tvBetCount_SG);
    }

    @Override
    public final void onBackPressed() {
        showDialogGoToLobby(this);
    }

    private void swipePanels(){
        rlRightSlidingMenu_SG.setOnTouchListener(new GestureListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                if (rlRightSlidingMenu_SG.getX() == widthSliderRight + rightToRight) {
                    animationRightSlidingMenu(rlRightSlidingMenu_SG, rightToRight, widthSliderRight);
                    changeImageCloseRightMenu(widthSliderRight, rightToRight, ivCloseRightMenu_SG, rlRightSlidingMenu_SG);
                }
            }

            @Override
            public void onSwipeRight() {

                if (rlRightSlidingMenu_SG.getX() == widthSliderRight) {
                    animationRightSlidingMenu(rlRightSlidingMenu_SG, rightToRight, widthSliderRight);
                    changeImageCloseRightMenu(widthSliderRight, rightToRight, ivCloseRightMenu_SG, rlRightSlidingMenu_SG);
                }
            }
        });
        rlLeftPanelGrid_SG.setOnTouchListener(new GestureListener(getApplicationContext())
        {
            @Override
            public void onSwipeRight() {
                if(rlLeftPanelGrid_SG.getX()==-leftToLeft) {
                    animationLeftGridScore(rlLeftPanelGrid_SG, leftToLeft, widthSliderLeft);
                }
            }

            @Override
            public void onSwipeLeft() {
                if(rlLeftPanelGrid_SG.getX() == -widthSliderLeft) {
                    animationLeftGridScore(rlLeftPanelGrid_SG, leftToLeft, widthSliderLeft);
                }
            }
        });

        ivCloseLeftMenu_GS.setOnTouchListener(new GestureListener(getApplicationContext())
        {
            @Override
            public void onClickAnim() {
                animationLeftGridScore(rlLeftPanelGrid_SG, leftToLeft, widthSliderLeft);
            }
        }
        );

        ivCloseRightMenu_SG.setOnTouchListener(new GestureListener(getApplicationContext()) {
            @Override
            public void onClickAnim() {
                animationRightSlidingMenu(rlRightSlidingMenu_SG, rightToRight, widthSliderRight);
                changeImageCloseRightMenu(widthSliderRight, rightToRight, ivCloseRightMenu_SG, rlRightSlidingMenu_SG);
            }
        });
    }

    @Override
    public final void update(final Observable _observable, final Object _data) {
        if (_data == null) return;

        final HashMap<String, Object> resultMap = (HashMap<String, Object>) _data;

        if (resultMap.containsKey(ERROR)) {
            final HashMap<String, String> errorMap = (HashMap<String, String>) resultMap.get(ERROR);
            responseGameError(this, this, errorMap);
        } else if (resultMap.containsKey(API_GET_STATUS)) {
            final HashMap<String, String> statusMap = (HashMap<String, String>) resultMap.get(API_GET_STATUS);
            responseApiGetStatusCommon(statusMap, rlRoot_SG, tvBetCount_SG);
        } else if (resultMap.containsKey(API_NEW_BETS)) {
            final HashMap<String, String> newBetsMap = (HashMap<String, String>) resultMap.get(API_NEW_BETS);
            responseApiNewBets(this, newBetsMap);
        } else if (resultMap.containsKey(API_TIPS)) {
            final HashMap<String, String> tipsMap = (HashMap<String, String>) resultMap.get(API_TIPS);
            responseApiTips(this, tipsMap);
        } else if (resultMap.containsKey(API_GET_ACTIVE_TABLES)) {
            final ArrayList<TableObject> activeTableList = (ArrayList<TableObject>) resultMap.get(API_GET_ACTIVE_TABLES);
            responseApiGetActiveTables(this, this, activeTableList);
        }
    }
    
	/**
     * Developer Sam
     * 2014年3月29日
     * 
     * set Currency Symbol
     */
    public final void setCurrencySymbol(){
    	tvBetCount_SG.setText(getCurrencySymbolByName(getCurrency()) + "0");
    }
    
    /**
     * Developer Sam
     * 2014年3月29日
     * 
     * set TextView typeface
     * @param textView 
     */
    public final void setTextViewTypeface(TextView textView, String font){
    	Typeface typeFace = Typeface.createFromAsset(getAssets(), font);
    	textView.setTypeface(typeFace);
    }
    
    /**
     * Developer Sam
     * 2014年3月29日
     * 
     * set tvBetCount_SG, tvTimer_GS and tvBalance_SG font type and Currency Symbol
     */
    public final void setFontTypeAndCurrencySymbol(){
    	//set tvBetCount_SG, tvTimer_GS and tvBalance_SG font type and Currency Symbol
	    setTextViewTypeface(tvBetCount_SG,"fonts/ProximaNova-Bold.otf");
	    setCurrencySymbol();
	    setTextViewTypeface(tvBalance_SG,"fonts/ProximaNova-Bold.otf");
	    setTextViewTypeface(tvTimer_GS,"fonts/ProximaNova-Bold.otf");
	    
	    //set footer box font type
	    LinearLayout lineRoles_SG = (LinearLayout) rlRoot_SG.findViewById(R.id.lineRoles_SG);
	    TextView[] tvTitles = FooterBoxSelector.getAllTextViewTitleLineRoles(lineRoles_SG);
	    TextView[] tvValues = FooterBoxSelector.getAllTextViewLineRoles(lineRoles_SG);
	    for(int i = tvTitles.length - 1; i >= 0; i--){
	    	Log.i("info", "Size : " + i);
	    	// set Title font type
	    	setTextViewTypeface(tvTitles[i],"fonts/MyriadPro-Regular.otf");
	    	// set Value font type
	    	setTextViewTypeface(tvValues[i],"fonts/MyriadPro-Regular.otf");
	    }    
    }
    
}