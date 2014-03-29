package com.vivogaming.livecasino.game_logic;

import android.content.Context;
import com.vivogaming.livecasino.R;

import java.util.ArrayList;

import static com.vivogaming.livecasino.game_logic.ChipStackWorker.getStackChipsSum;
import static com.vivogaming.livecasino.game_logic.ChipsPlacing.getAllStacksSum;
import static com.vivogaming.livecasino.screens.game.ToastWorker.showToast;

public abstract class LimitWorker {
    //[0] - min
    //[1] - max
    private static int[] tableLimit         = new int[2];
    private static int[] playerPairLimit    = new int[2];
    private static int[] playerLimit        = new int[2];
    private static int[] tieLimit           = new int[2];
    private static int[] bankerLimit        = new int[2];
    private static int[] bankerPairLimit    = new int[2];

    /**
     * returns used limit array by box res id
     * @param _boxResId
     * @return
     */
    public static int[] chooseUsedLimitArray(final int _boxResId) {
        switch (_boxResId) {
            case R.id.ivPlayerPair_SG:
                return playerPairLimit;

            case R.id.ivPlayer_SG:
                return playerLimit;

            case R.id.ivTie_SG:
                return tieLimit;

            case R.id.ivBanker_SG:
                return bankerLimit;

            case R.id.ivBankerPair_SG:
                return bankerPairLimit;

            default:
                return new int[2];
        }
    }

    /**
     * set box limits. get data in format "min-max"
     * than split by "-", and save as integers in appropriate arrays
     * @param _tableLimits
     * @param _playerPairLimits
     * @param _playerLimits
     * @param _tieLimits
     * @param _bankerLimits
     * @param _bankerPairLimits
     */
    public static final void setBoxGameLimits(final String _tableLimits, final String _playerPairLimits, final String _playerLimits,
                                              final String _tieLimits, final String _bankerLimits, final String _bankerPairLimits) {
        final String[] tableValues        = _tableLimits.split("-");
        final String[] playerPairValues   = _playerPairLimits.split("-");
        final String[] playerValues       = _playerLimits.split("-");
        final String[] tieValues          = _tieLimits.split("-");
        final String[] bankerValues       = _bankerLimits.split("-");
        final String[] bankerPairValues   = _bankerPairLimits.split("-");

//        tableLimit[0] = Integer.valueOf(tableValues[0]);
        tableLimit[0] = 1;  //table minimum limit is 1
        tableLimit[1] = Integer.valueOf(tableValues[1]);

        playerPairLimit[0] = Integer.valueOf(playerPairValues[0]);
        playerPairLimit[1] = Integer.valueOf(playerPairValues[1]);

        playerLimit[0] = Integer.valueOf(playerValues[0]);
        playerLimit[1] = Integer.valueOf(playerValues[1]);

        tieLimit[0] = Integer.valueOf(tieValues[0]);
        tieLimit[1] = Integer.valueOf(tieValues[1]);

        bankerLimit[0] = Integer.valueOf(bankerValues[0]);
        bankerLimit[1] = Integer.valueOf(bankerValues[1]);

        bankerPairLimit[0] = Integer.valueOf(bankerPairValues[0]);
        bankerPairLimit[1] = Integer.valueOf(bankerPairValues[1]);
    }

    /**
     * returns limit in int matrix [5][2] for limit dialog in Game screen
     * @return
     */
    public static final int[][] getBoxGameLimits() {
        final int[][] limitsMatrix = new int[5][2];

        limitsMatrix[0] = playerPairLimit;
        limitsMatrix[1] = playerLimit;
        limitsMatrix[2] = tieLimit;
        limitsMatrix[3] = bankerLimit;
        limitsMatrix[4] = bankerPairLimit;

        return limitsMatrix;
    }

    /**
     * check whole table limit
     * if exceeded table limit - shows toast and return false
     * otherwise return true
     * @param _context
     * @param _valueToAdd   value, attempted to add
     * @return
     */
    public static final boolean checkTableMaxLimit(final Context _context, final int _valueToAdd) {
        final int allStacksSum = getAllStacksSum();
        if (allStacksSum + _valueToAdd <= tableLimit[1])
            return true;

        showToast(_context.getString(R.string.reached_table_limit));
        return false;
    }

    /**
     * check table minimum limit, if bet is less then limit
     * than return and false
     * @param _context
     * @return
     */
    public static final boolean checkTableMinLimit(final Context _context) {
        final int allStacksSum = getAllStacksSum();
        if (allStacksSum >= tableLimit[0])
            return true;

        showToast(_context.getString(R.string.minimum_limit_is) + " " + tableLimit[0]);
        return false;
    }

    /**
     * check specified box limit
     * if exceeded box limit - shows toast and return false
     * otherwise return true
     * @param _context
     * @param _usedStack        box's stack
     * @param _stackLimitArray  box's limit array
     * @param _valueToAdd       value, attempted to add
     * @return
     */
    public static final boolean checkStackMaxLimit(final Context _context, final ArrayList<ChipObject> _usedStack,
                                                   final int[] _stackLimitArray, final int _valueToAdd) {
        final int stackSum = getStackChipsSum(_usedStack);
        if (stackSum + _valueToAdd <= _stackLimitArray[1])
            return true;

        showToast(_context.getString(R.string.reached_box_limit));
        return false;
    }
}