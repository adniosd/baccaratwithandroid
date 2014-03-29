package com.vivogaming.livecasino.screens.game;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.ChipObject;
import com.vivogaming.livecasino.game_logic.ChipStackWorker;
import com.vivogaming.livecasino.game_logic.ChipsPlacing;
import com.vivogaming.livecasino.game_logic.FooterBoxSelector;
import com.vivogaming.livecasino.game_logic.GameViewWorker;

/**
 * 
 * @author Sam
 * 2014年3月29日
 */
public abstract class FooterBoxWorker {
	static RelativeLayout[] rlFooterBoxes;
	static TextView[] footer_Values;
	static TextView[] footer_Titles;
	static LinearLayout lineRoles_SG;
	static SurfaceView videoView;
	static LinearLayout lineVideo;

	static int cc=0;
	
	/**
	 * developer Sam
     * 2014 年 3月29日
     * 
	 * reset layout params for video
	 */
	private static final void setLayoutParamsForVideo(Integer status){
		android.widget.LinearLayout.LayoutParams sufaceviewParams = (android.widget.LinearLayout.LayoutParams) videoView
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, lineRoles_SG.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, lineRoles_SG.getId());
        layoutParams.addRule(RelativeLayout.BELOW, R.id.rlUpper_SG);
		if(status==null){
			layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,lineRoles_SG.getId());
		}else{
			layoutParams.addRule(RelativeLayout.ABOVE, lineRoles_SG.getId());
		}
		lineVideo.setLayoutParams(layoutParams);
		sufaceviewParams.height = android.widget.LinearLayout.LayoutParams.FILL_PARENT;
        videoView.setLayoutParams(sufaceviewParams);
	}
	
	/**
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * hide footer bar
	 */
	public static final void hideFooterBox() {
		lineRoles_SG.setVisibility(View.INVISIBLE);
		setLayoutParamsForVideo(null);
	}

	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * show footer bar
	 */
	public static final void showFooterBox() {
		lineRoles_SG.setVisibility(View.VISIBLE);
		setLayoutParamsForVideo(1);
	}

	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * find footer bar views
	 * @param _rootLayout
	 * @param _footerId
	 */
	public static final void getFooterBox(RelativeLayout _rootLayout,
			int _footerId) {
		lineRoles_SG = (LinearLayout) _rootLayout.findViewById(_footerId);
		footer_Values = FooterBoxSelector.getAllTextViewLineRoles(lineRoles_SG);
		footer_Titles = FooterBoxSelector
				.getAllTextViewTitleLineRoles(lineRoles_SG);
		rlFooterBoxes = FooterBoxSelector.getRLFooterBoxes(lineRoles_SG);
		lineVideo = (LinearLayout)Game.getGameActivity().findViewById(R.id.rlRoot_SG).findViewById(R.id.lineVideo);
		videoView = (SurfaceView)lineVideo.findViewById(R.id.vvStreamVideo_SMG);
	}
	
	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * init Footer Chips according to placing stacks
	 */
	@SuppressWarnings("unchecked")
	public static final void initFooterChips() {
		ChipsPlacing.deleteAllChipsFormTraies(Game.getGameActivity());
		placingChipsOnFooter(ChipsPlacing.getAllStacks());
		showFooterBox();
		updateFooterValues();
	}
	
	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * pdate Footer Values
	 */
	public static final void updateFooterValues(){
		List<Object>_listStacks = ChipsPlacing.getAllStacks();
		for(int i = footer_Values.length - 1; i >=0; i--){
			GameViewWorker.updateBetValueByBoxId(footer_Values[i],ChipStackWorker.getStackChipsSum((ArrayList<ChipObject>)_listStacks.get(i)));
		}
	}
	
	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * placing Chips On Footer bar
	 * @param _listStacks all stacks
	 */
	@SuppressWarnings("unchecked")
	public static final void placingChipsOnFooter(List<Object> _listStacks){
		ChipsPlacing.placeChipOnFooter((ArrayList<ChipObject>) _listStacks.get(0), 
				rlFooterBoxes[0], 
				FooterBoxSelector.getImgvIdByBoxId(R.id.ivPlayerPair_SG)
				);
		
		ChipsPlacing.placeChipOnFooter((ArrayList<ChipObject>) _listStacks.get(1), 
				rlFooterBoxes[1], 
				FooterBoxSelector.getImgvIdByBoxId(R.id.ivPlayer_SG)
				);
		
		ChipsPlacing.placeChipOnFooter((ArrayList<ChipObject>) _listStacks.get(2), 
				rlFooterBoxes[2], 
				FooterBoxSelector.getImgvIdByBoxId(R.id.ivTie_SG)
				);
		
		ChipsPlacing.placeChipOnFooter((ArrayList<ChipObject>) _listStacks.get(3), 
				rlFooterBoxes[3], 
				FooterBoxSelector.getImgvIdByBoxId(R.id.ivBanker_SG)
				);
		
		ChipsPlacing.placeChipOnFooter((ArrayList<ChipObject>) _listStacks.get(4), 
				rlFooterBoxes[4], 
				FooterBoxSelector.getImgvIdByBoxId(R.id.ivBankerPair_SG)
				);
	}
	
	/**
	 * 
	 * Developer Sam
	 * 2014年3月29日
	 * 
	 * delete all Chips From Footer bar
	 */
	@SuppressWarnings("unchecked")
	public static final void deleteAllChipsFromFooter() {
		List<Object>_listStacks = ChipsPlacing.getAllStacks();
		for(int i = rlFooterBoxes.length - 1; i >= 0; i--){
			GameViewWorker.deleteAllChipsFromUiByStack(rlFooterBoxes[i], (ArrayList<ChipObject>)_listStacks.get(i));
		}
	}

	public static final TextView[] getFooterTitles() {
		return footer_Titles;
	}

	public static final TextView[] getFooterValues() {
		return footer_Values;
	}

}
