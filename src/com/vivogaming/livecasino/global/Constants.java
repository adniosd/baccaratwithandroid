package com.vivogaming.livecasino.global;

public abstract class Constants {
    /**
     * value's names for shared preferences
     */
    public static final String SHARED_PREFERENCES               = "preferences";

    /**
     * api names
     */
    public static final String API_LOGIN                        = "http://www.1vivo.com/flash/loginplayer.aspx?";
    public static final String API_GET_ACTIVE_TABLES            = "http://www.1vivo.com/flash/GetActiveTables.aspx?";
    public static final String API_INIT                         = "http://www.1vivo.com/Baccarat/InitBaccaratPlayer.aspx?";
    public static final String API_GET_STATUS                   = "http://www.1vivo.com/Baccarat/GetPlayerBaccaratStatus.aspx?";
    public static final String API_NEW_BETS                     = "http://www.1vivo.com/Baccarat/NewBets.aspx?";
    public static final String API_REGISTER                     = "http://www.1vivo.com/Integrations/CreateUser.aspx?";
    public static final String API_TIPS                         = "http://www.1vivo.com/flashtips/tipsmanager.aspx?";

    /**
     * request/response api parameters
     */
    public static final String LOGIN_NAME                       = "LoginName";
    public static final String PLAYER_PASSWORD                  = "PlayerPassword";
    public static final String USER_PASSWORD                    = "UserPassword";
    public static final String OPERATOR_ID                      = "OperatorID";
    public static final String PLAYER_IP                        = "PlayerIP";
    public static final String GAME_NAME                        = "Gamename";
    public static final String TABLE_ID                         = "TableID";
    public static final String CURRENCY                         = "Currency";
    public static final String PLAYER_CURRENCY                  = "PlayerCurrency";
    public static final String LIMIT_NAME                       = "LimitName";
    public static final String LIMIT_ID                         = "LimitID";
    public static final String LIMIT_MIN                        = "LimitMin";
    public static final String LIMIT_MAX                        = "LimitMax";
    public static final String DEALER_NAME                      = "DealerName";
    public static final String TABLE_STATUS                     = "TableStatus";
    public static final String RESULT_HISTORY                   = "ResaultHistory";     //wtf?
    public static final String PLAYER_TOKEN                     = "PlayerToken";
    public static final String TOKEN                            = "Token";
    public static final String OPEN                             = "OPEN";
    public static final String TABLE_LIMITS                     = "TableLimits";
    public static final String TABLE_TRN_ID                     = "TableTrnID";
    public static final String TIME_TO_BET                      = "TimeToBet";
    public static final String PLAYER_BALANCE                   = "PlayerBalance";
    public static final String OPERATOR_KEY                     = "OperatorKey";
    public static final String USER_NAME                        = "UserName";
    public static final String FIRST_NAME                        = "FirstName";
    public static final String LAST_NAME                        = "LastName";
    public static final String EMAIL                            = "Email";

    //window id and init id is the same value (wtf?)
    public static final String WINDOW_ID                        = "WINDOW_ID";
    public static final String INIT_ID                          = "InitID";

    public static final String LIVE_VIDEO_URL                   = "live_video_url";
    public static final String BUY_CHIPS_ON                     = "BUY_CHIPS_ON";
    public static final String NEXT_CALL                        = "NextCall";
    public static final String TOTAL_BETS                       = "TotalBets";
    public static final String TOTAL_WINS                       = "TotalWins";
    public static final String RESULT_SUM                       = "ResaultSum";
    public static final String NEW_BETS                         = "NewBets";
    public static final String GUID                             = "GUID";
    public static final String HASH                             = "Hash";

    public static final String PLAYER_PAIR_BOX_NUM              = "4";
    public static final String PLAYER_BOX_NUM                   = "1";
    public static final String TIE_BOX_NUM                      = "3";
    public static final String BANKER_BOX_NUM                   = "2";
    public static final String BANKER_PAIR_BOX_NUM              = "5";

    public static final String DESCRIPTION                      = "description";
    public static final String SUCCESS                          = "SUCCESS";
    public static final String FAIL                             = "FAIL";
    public static final String NEW_BALANCE                      = "newbalance";
    public static final String BALANCE                          = "Balance";
    public static final String CMD                              = "CMD";
    public static final String AMOUNT                           = "Amount";

    /**
     * for parser
     */
    public static final String NEW_LINE                         = "[NEW_LINE]";

    /**
     * temp parameters
     */
//  public static final String OPERATOR_ID_NUM                    = "1453"; //not used
    public static final String OPERATOR_ID_NUM                   = "1718";
//   public static final String OPERATOR_ID_NUM                   = "1638";
//   public static final String OPERATOR_ID_NUM                   = "1595";
    public static final String OPERATOR_KEY_NUM                 = "FLS777VGS";
    public static final String BACCARAT                         = "Baccarat";

    /**
     * width and distance coefficients
     * for calculating size of footer panel on game screen
     */
    public final static double WIDTH_COEFF_BANKER_PAIR          = 0.13;
    public final static double WIDTH_COEFF_BANKER               = 0.21;
    public final static double WIDTH_COEFF_TIE                  = 0.1;
    public final static double WIDTH_COEFF_PLAYER               = 0.21;
    public final static double WIDTH_COEFF_PLAYER_PAIR          = 0.13;

    //in percents
    public final static double OFFSET_X_PLAYER_BOX              = 0.15;
    public final static double OFFSET_X_BANKER_BOX              = 1 - OFFSET_X_PLAYER_BOX;
    public final static double OFFSET_Y_PLAYER_BANKER_BOX       = 1 - 0.11;
    public final static double OFFSET_Y_TIE_BOX                 = 1 - 0.07;
    public final static double OFFSET_Y_PAIR_BOX                = 1 - 0.25;

    public final static double HEIGHT_COEFF_PLAYER_BOX          = 0.39;
    public final static double HEIGHT_COEFF_PLAYER_PAIR_BOX     = 0.58;
    public final static double HEIGHT_COEFF_TIE_BOX             = 0.88;
    public final static double HEIGHT_COEFF_BANKER_PAIR_BOX     = 0.58;
    public final static double HEIGHT_COEFF_BANKER_BOX          = 0.39;

    /**
     * width coefficients
     * for CustomViewScore
     */
    public final static double WIDTH_LINER_SCORE                = 0.98;
    public final static double COEFF_RADIUS                     = 0.6;
    public final static float PERCENT                           = 0.7f;
    public final static int HORIZONTAL_COUNT_TO_CELL            = 16;
    public final static int VERTICAL_COUNT_TO_CELL              = 6;
    public static int HORIZONTAL_COUNT_OF_CELL                  = 13;
    public static int VERTICAL_COUNT_OF_CELL                    = 6;
    public final static double TEXT_SIZE_IN_CIRCLE              = 1.17;

    /**
     * chips values
     */
    public static final int[] USED_CHIP_VALUES                  = { 1, 5, 10, 25, 100, 500, 1000 };

    /**
     * animation duration
     */
    public static final int LEFT_SLIDING_MENU_ANIMATION         = 200;
    public static final int RIGHT_SLIDING_MENU_ANIMATION        = 150;

    /**
     * game
     */
    public static final int TIME_FOR_PLACING_CARDS              = 10;
    public static final int TIME_FOR_PLACING_BETS               = 30;

    /**
     * delays
     */
    public static final int TIME_SHOWING_TOAST                  = 3;
    public static final int TIME_DELAY_AFTER_TOAST              = 1;

    public static final int TIME_TOAST_DELAY_DEFAULT            = 10;
    public static final int TIME_ADDITIONAL_DELAY_DEFAULT       = 3;
    public static final String KEY_TIME_TOAST_DELAY             = "key_time_toast_delay";
    public static final String KEY_TIME_ADDITIONAL_DELAY        = "key_additional_delay";
    public static int TIME_TOAST_DELAY                          = 10;
    public static int TIME_ADDITIONAL_DELAY                     = 3;

    /**
     * settings
     */
    public static final String KEY_VOLUME                       = "Volume";
    public static final String KEY_SOUND                        = "Sound";
    public static final String KEY_LOGOUT_TIME                  = "logout_time";

    /**
     * debug tags
     */
    public static final String TAG_OBSERVER                     = "tag_observer";
    public static final String TAG_VIDEO                        = "tag_video";
    public static final String TAG_ERROR                        = "tag_error";
    public static final String TAG_UNHANDLED_ERROR              = "tag_unhandled_error";
    public static final String TAG_TIMINGS                      = "tag_timings";
    public static final String TAG_THREAD                       = "tag_thread";
    public static final String TAG_VOLUME                       = "tag_volume";

    public static final int LOGOUT_TIME_VAL                     = 180000;   //in ms, 3 minutes

    /**
     * for error handling
     */
    public static final String ERROR                            = "error";
    public static final String ERROR_103                        = "error 103";
    public static final String ERROR_DESCRIPTION                = "error_description";
    public static final String ERROR_NUM                        = "error_num";
    public static final String STATUS_CODE                      = "statuscode";

    /**
     * for roulette play table drawing
     */
    public static final int SQUARE_COLUMN_COUNT                 = 12;
    public static final int SQUARE_ROW_COUNT                    = 3;
    public static final float COEFF_BASE_SIDE                   = 0.074f;
    public static final int MULTIPLIER_MEDIUM_RECT_WIDTH        = 2;
    public static final int MULTIPLIER_BIG_RECT_WIDTH           = 4;
    public static final int MULTIPLIER_ZERO_SHAPE_HEIGHT        = 3;
}