package com.vivogaming.livecasino.game_logic;

import com.vivogaming.livecasino.R;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * select current winner views
 * according to the winner resultSam getting from api response
 * @author Sam
 * 2014年3月21日
 */
public abstract class WinnerBoxSelector {
	/**
	 * get all views' id according to the winner resultSam getting from api response
	 * @param resultSum winner resultSam getting from api response
	 * @return a String contains all views' id of current winner
	 */
	public static final String getViewIdsOfWinner(int resultSum){
		String ids = "";
		switch (resultSum) {
		case 1:
			ids = R.id.ivPlayerwin +","+ R.id.rlPlayer_SG + "," + R.id.tvPlayerTitle + "," + R.id.tvPlayerValue;
			break;
		case 2:
			ids = R.id.ivBankerwin +","+ R.id.rlBanker_SG + "," + R.id.tvBankerTitle + "," + R.id.tvBankerValue;
			break;
		case 3:
			ids = R.id.ivTiewin +","+ R.id.rlTie_SG + "," + R.id.tvTieTitle + "," + R.id.tvTieValue;
			break;
		case 4:
			ids = R.id.ivPlayerPairwin +","+ R.id.rlPlayerPair_SG + "," + R.id.tvPlayerPairTitle + "," + R.id.tvPlayerPairValue;
			break;
		case 5:
			ids = R.id.ivBankerPairwin +","+ R.id.rlBankerPair_SG + "," + R.id.tvBankerPairTitle + "," + R.id.tvBankerPairValue;
			break;
		default:
			ids = "";
			break;
		}
		return ids;
	}
	
}
