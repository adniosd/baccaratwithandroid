package com.vivogaming.livecasino.game_logic;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vivogaming.livecasino.R;

import java.util.ArrayList;

import static com.vivogaming.livecasino.game_logic.ChipsPlacing.getAllStacksSum;
import static com.vivogaming.livecasino.global.Variables.getCurrency;
import static com.vivogaming.livecasino.screens.game.NotificationDataWorker.getCurrencySymbolByName;

public abstract class GameViewWorker {

    /**
     * delete chips in list from UI
     * @param _rootLayout
     * @param _listChips
     */
    public static final void deleteAllChipsFromUiByStack(final RelativeLayout _rootLayout, final ArrayList<ChipObject> _listChips) {
        for (int i = 0; i < _listChips.size(); i++)
            _rootLayout.removeView(_listChips.get(i).imageView);
    }

    /**
     * set bet count text on "Current bet" view
     * @param _tvBetCount_SG
     */
    public static final void updateCurrentBetView(final TextView _tvBetCount_SG) {
        final int sum = getAllStacksSum();
        _tvBetCount_SG.setText(getCurrencySymbolByName(getCurrency()) + sum);
    }

    /**
     * calculate chip bottom offset by specified box
     * get density
     * get bottom padding if exist
     * calculate offset
     * @param _boxResId
     */
    public static final float calculateChipBottomOffset(final RelativeLayout _rootLayout, final int _boxResId) {
        final DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) _rootLayout.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float density = metrics.density;                //screen`s density

        final ImageView ivCurrentBox = (ImageView) _rootLayout.findViewById(_boxResId);
        final int boxHeight = ivCurrentBox.getHeight();

        final float boxPadding;
        if (_boxResId == R.id.ivPlayerPair_SG || _boxResId == R.id.ivBankerPair_SG) {
            boxPadding = _rootLayout.getResources().getDimension(R.dimen.pair_box_padding_bottom) * density;
        } else {
            boxPadding = 0;
        }

        final float chipOffset = boxPadding + (float) boxHeight / 8;

        return chipOffset;
    }

    public static int getPlayerViewBottomMargin(Context _context){
        return (int) Math.floor(_context.getResources().getDimension(R.dimen.temp_player_bottom_offset));
    }
    public static int getBankerViewBottomMargin(Context _context){
        return (int) Math.floor(_context.getResources().getDimension(R.dimen.temp_banker_bottom_offset));
    }
    
    /**
     * Developer Sam
     * 2014年3月29日
     * 
     * update value of box which current user placed chip on to
     * @param _tvBetValue the textView whose value you want to update
     * @param sum new value 
     */
    public static final void updateBetValueByBoxId(TextView _tvBetValue, final Integer sum){
    	/**
    	 * if a new round start
    	 * replace with "" 
    	 */
    	String value = sum > 0 ? sum.toString() : "";
    	_tvBetValue.setText(value);
    }
    
    /**
     * 
     * Developer Sam
     * 2014年3月29日
     * 
     * update Current Bet
     * @param _activity
     */
    public static final void updateCurrentBet(Activity _activity){
    	updateCurrentBetView((TextView)_activity.findViewById(R.id.rlRightPanel_SG).findViewById(R.id.rlCurrentBet_SG).findViewById(R.id.tvBetCount_SG));
    }
    
}
