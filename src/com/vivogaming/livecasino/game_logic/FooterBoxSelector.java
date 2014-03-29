package com.vivogaming.livecasino.game_logic;

import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vivogaming.livecasino.R;

/**
 * select footer view according to the image which the current user click
 * @author Sam 2014年3月21日
 */
public abstract class FooterBoxSelector {
	/**
	 * select tray id according to the image which the current user click
	 * @param _boxId the image's id which the current user click
	 * @return tray id
	 */
	public static final int getImgvIdByBoxId(int _boxId) {
		int ivId = 0;
		switch (_boxId) {
		case R.id.ivPlayerPair_SG:
			ivId = R.id.chipsPlayerPair_SG;
			break;
		case R.id.ivPlayer_SG:
			ivId = R.id.chipsPlayer_SG;
			break;
		case R.id.ivTie_SG:
			ivId = R.id.chipsTie_SG;
			break;
		case R.id.ivBanker_SG:
			ivId = R.id.chipsBanker_SG;
			break;
		case R.id.ivBankerPair_SG:
			ivId = R.id.chipsBankerPair_SG;
			break;
		default:
			ivId = 0;
			break;
		}
		return ivId;
	}

	/**
	 * select footer box RelativeLayout according to the image which the current user click
	 * @param _rootLayout root RelativeLayout
	 * @param _boxId the image's id which the current user click
	 * @return footer boxes RelativeLayout
	 */
	public static final RelativeLayout[] getRLFooterBoxes(LinearLayout linLayout) {
		RelativeLayout[] rlFooterBoxes = new RelativeLayout[5];
		rlFooterBoxes[0]=(RelativeLayout) linLayout.findViewById(R.id.rlPlayerPair_SG);
		rlFooterBoxes[1]=(RelativeLayout) linLayout.findViewById(R.id.rlPlayer_SG);
		rlFooterBoxes[2]=(RelativeLayout) linLayout.findViewById(R.id.rlTie_SG);
		rlFooterBoxes[3]=(RelativeLayout) linLayout.findViewById(R.id.rlBanker_SG);
		rlFooterBoxes[4]=(RelativeLayout) linLayout.findViewById(R.id.rlBankerPair_SG);
		return rlFooterBoxes;
	}

	/**
	 * select textView according to the tray id
	 * @param _rootLayout root RelativeLayout
	 * @param _boxId the tray id
	 * @return textView
	 */
	public static final TextView getTextViewByBoxId(RelativeLayout _rootLayout,
			int _boxId) {
		int tvId = 0;
		switch (_boxId) {
		case R.id.chipsPlayerPair_SG:
			tvId = R.id.tvPlayerPairValue;
			break;
		case R.id.chipsPlayer_SG:
			tvId = R.id.tvPlayerValue;
			break;
		case R.id.chipsTie_SG:
			tvId = R.id.tvTieValue;
			break;
		case R.id.chipsBanker_SG:
			tvId = R.id.tvBankerValue;
			break;
		case R.id.chipsBankerPair_SG:
			tvId = R.id.tvBankerPairValue;
			break;
		default:
			tvId = 0;
			break;
		}
		return (TextView) _rootLayout.findViewById(tvId);
	}
	
	/**
	 * get all TextViews of value from footer LinearLayout whose id is LineRoles_SG
	 * @param lineRoles_SG footer LinearLayout whose id is LineRoles_SG
	 * @return TextView[], all TextViews of value from footer LinearLayout
	 */
	public static final TextView[] getAllTextViewLineRoles(LinearLayout lineRoles_SG) {
		TextView[] textViews = new TextView[5];
		textViews[0] = (TextView) lineRoles_SG.findViewById(R.id.rlPlayerPair_SG).findViewById(R.id.tvPlayerPairValue);
		textViews[1] = (TextView) lineRoles_SG.findViewById(R.id.rlPlayer_SG).findViewById(R.id.tvPlayerValue);
		textViews[2] = (TextView) lineRoles_SG.findViewById(R.id.rlTie_SG).findViewById(R.id.tvTieValue);
		textViews[3] = (TextView) lineRoles_SG.findViewById(R.id.rlBanker_SG).findViewById(R.id.tvBankerValue);
		textViews[4] = (TextView) lineRoles_SG.findViewById(R.id.rlBankerPair_SG).findViewById(R.id.tvBankerPairValue);
		return textViews;
	}
	
	/**
	 * get all TextViews of title from footer LinearLayout whose id is LineRoles_SG
	 * @param lineRoles_SG footer LinearLayout whose id is LineRoles_SG
	 * @return TextView[], all TextViews of title from footer LinearLayout
	 */
	public static final TextView[] getAllTextViewTitleLineRoles(LinearLayout lineRoles_SG) {
		TextView[] textViews = new TextView[5];
		textViews[0] = (TextView) lineRoles_SG.findViewById(R.id.rlPlayerPair_SG).findViewById(R.id.tvPlayerPairTitle);
		textViews[1] = (TextView) lineRoles_SG.findViewById(R.id.rlPlayer_SG).findViewById(R.id.tvPlayerTitle);
		textViews[2] = (TextView) lineRoles_SG.findViewById(R.id.rlTie_SG).findViewById(R.id.tvTieTitle);
		textViews[3] = (TextView) lineRoles_SG.findViewById(R.id.rlBanker_SG).findViewById(R.id.tvBankerTitle);
		textViews[4] = (TextView) lineRoles_SG.findViewById(R.id.rlBankerPair_SG).findViewById(R.id.tvBankerPairTitle);
		return textViews;
	}
	
}
