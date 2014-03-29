package com.vivogaming.livecasino.screens.game;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.WinnerBoxSelector;
/**
 * winner worker
 * show and hide winner box
 * @author Sam 2014年3月21日
 */
public abstract class WinnerWorker {
	static ImageView ivWinner;
	static TextView tvTitle;
	static TextView tvValue;
	@SuppressLint("ResourceAsColor")

	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * show winner box
	 */
	public static final void showWinnerBox(){
		ivWinner.setVisibility(View.VISIBLE);
		tvTitle.setBackgroundResource(R.drawable.highlight_tv_players);
		tvTitle.setTextColor(Color.rgb(211, 180, 84));
		tvValue.setTextColor(Color.rgb(211, 180, 84));
	}
	
	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * hide winner box
	 */
	public static final void restoreWinnerBox(){
		ivWinner.setVisibility(View.INVISIBLE);
		tvTitle.setBackgroundResource(R.drawable.tv_players);
		tvTitle.setTextColor(Color.WHITE);
		tvValue.setTextColor(Color.WHITE);
	}
	
	/**
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * find winner views according to the resulSum
	 * @param _rootLayout
	 * @param resultSum
	 */
	public static final void findWinnerViews(RelativeLayout _rootLayout, int resultSum){
		String[] ids = WinnerBoxSelector.getViewIdsOfWinner(resultSum).split(",");
		int imgId = Integer.valueOf(ids[0]);
		int layoutId = Integer.valueOf(ids[1]);
		int titleId = Integer.valueOf(ids[2]);
		int valueId = Integer.valueOf(ids[3]);
		ivWinner = (ImageView)_rootLayout.findViewById(R.id.lineWinner).findViewById(imgId);
		RelativeLayout rlWinnerLayout = (RelativeLayout)_rootLayout.findViewById(R.id.lineRoles_SG).findViewById(layoutId);
		tvTitle = (TextView)rlWinnerLayout.findViewById(titleId);
		tvValue = (TextView)rlWinnerLayout.findViewById(valueId);
	}
}
