package com.vivogaming.livecasino.game_logic;

import static com.vivogaming.livecasino.game_logic.ChipData.getChipValueByResId;
import static com.vivogaming.livecasino.game_logic.ChipData.getChipViewResIdByValue;
import static com.vivogaming.livecasino.game_logic.ChipData.getImageIdByChipValue;
import static com.vivogaming.livecasino.game_logic.ChipStackWorker.addChipToStack;
import static com.vivogaming.livecasino.game_logic.ChipStackWorker.getStackChipsSum;
import static com.vivogaming.livecasino.game_logic.ChipStackWorker.groupChips;
import static com.vivogaming.livecasino.game_logic.GameViewWorker.calculateChipBottomOffset;
import static com.vivogaming.livecasino.game_logic.GameViewWorker.deleteAllChipsFromUiByStack;
import static com.vivogaming.livecasino.game_logic.GameViewWorker.updateCurrentBetView;
import static com.vivogaming.livecasino.game_logic.LimitWorker.checkStackMaxLimit;
import static com.vivogaming.livecasino.game_logic.LimitWorker.checkTableMaxLimit;
import static com.vivogaming.livecasino.game_logic.LimitWorker.chooseUsedLimitArray;
import static com.vivogaming.livecasino.global.Constants.BANKER_BOX_NUM;
import static com.vivogaming.livecasino.global.Constants.BANKER_PAIR_BOX_NUM;
import static com.vivogaming.livecasino.global.Constants.PLAYER_BOX_NUM;
import static com.vivogaming.livecasino.global.Constants.PLAYER_PAIR_BOX_NUM;
import static com.vivogaming.livecasino.global.Constants.TIE_BOX_NUM;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.game.FooterBoxWorker;

public abstract class ChipsPlacing {
    private static ArrayList<ChipObject> playerPairStack   = new ArrayList<ChipObject>();
    private static ArrayList<ChipObject> playerStack       = new ArrayList<ChipObject>();
    private static ArrayList<ChipObject> tieStack          = new ArrayList<ChipObject>();
    private static ArrayList<ChipObject> bankerStack       = new ArrayList<ChipObject>();
    private static ArrayList<ChipObject> bankerPairStack   = new ArrayList<ChipObject>();
    private static ArrayList<PlacingObject> placingStack   = new ArrayList<PlacingObject>();
    private static ArrayList<PlacingObject> repeatStack    = new ArrayList<PlacingObject>();

    /**
     * handle placing chip logic
     * place on ui
     * add in list
     * @param _rootLayout   layout that contains boxes
     * @param _boxResId box res id
     * @param _chipResId  checked chip res id
     */
    public static final void placingChip(final RelativeLayout _rootLayout, final int _boxResId, final int _chipResId,
                                         final TextView _tvBetCount_SG) {
        //get reference to needed listToPlace
        final ArrayList<ChipObject> stackToPlace = chooseUsedStack(_boxResId);
        final int[] stackLimitArray = chooseUsedLimitArray(_boxResId);
        final int chipValue = getChipValueByResId(_chipResId);

        if (!checkTableMaxLimit(_rootLayout.getContext(), chipValue)) return;
        if (!checkStackMaxLimit(_rootLayout.getContext(), stackToPlace, stackLimitArray, chipValue)) return;

        if (stackToPlace.isEmpty()) {
            addChipToStack(stackToPlace, chipValue);
            addChipToPlacingStack(_boxResId, chipValue);
        } else {
            addChipToStack(stackToPlace, chipValue);
            addChipToPlacingStack(_boxResId, chipValue);
            deleteAllChipsFromUiByStack(_rootLayout, stackToPlace);
            groupChips(stackToPlace);
        }

        placeChipOnUi(stackToPlace, _rootLayout, _boxResId);
        updateCurrentBetView(_tvBetCount_SG);
    }

    /**
     * place chip on specified box
     * drawable selected by checked chip id
     * @param _rootLayout   layout
     * @param _boxResId box's id
     */
    //todo: calculate offset to center and offset for each box (10dp bottom for "pair" boxes)
    private static final void placeChipOnUi(final ArrayList<ChipObject> _listChipsToPlace, final RelativeLayout _rootLayout,
                                            final int _boxResId) {
        for (int i = 0; i < _listChipsToPlace.size(); i++) {
            final int chipValue = _listChipsToPlace.get(i).value;
            final int chipImageId = getImageIdByChipValue(chipValue);

            final float chipBottomOffset = calculateChipBottomOffset(_rootLayout, _boxResId);

            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, _boxResId);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, _boxResId);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, _boxResId);
            layoutParams.setMargins(0, 0, 0, (int) (chipBottomOffset + 10f * i));

            final ImageView ivChip = new ImageView(_rootLayout.getContext());
            ivChip.setScaleType(ImageView.ScaleType.CENTER);
            ivChip.setImageResource(chipImageId);
            ivChip.setLayoutParams(layoutParams);

            _listChipsToPlace.get(i).imageView = ivChip;
            _rootLayout.addView(ivChip);
        }
    }

    /**
     * returns used list by box res id
     * @param _boxResId
     * @return
     */
    private static final ArrayList<ChipObject> chooseUsedStack(final int _boxResId) {
        switch (_boxResId) {
            case R.id.ivPlayerPair_SG:
                return playerPairStack;

            case R.id.ivPlayer_SG:
                return playerStack;

            case R.id.ivTie_SG:
                return tieStack;

            case R.id.ivBanker_SG:
                return bankerStack;

            case R.id.ivBankerPair_SG:
                return bankerPairStack;

            default:
                return new ArrayList<ChipObject>();
        }
    }

    public static final void clearAllStacks() {
        playerPairStack.clear();
        playerStack.clear();
        tieStack.clear();
        bankerStack.clear();
        bankerPairStack.clear();

        placingStack.clear();
    }

    public static final void clearAllBoxes(final RelativeLayout _rootLayout) {
        deleteAllChipsFromUiByStack(_rootLayout, playerPairStack);
        deleteAllChipsFromUiByStack(_rootLayout, playerStack);
        deleteAllChipsFromUiByStack(_rootLayout, tieStack);
        deleteAllChipsFromUiByStack(_rootLayout, bankerStack);
        deleteAllChipsFromUiByStack(_rootLayout, bankerPairStack);
    }

    private static final void addChipToPlacingStack(final int _boxResId, final int _chipValue) {
        placingStack.add(new PlacingObject(_boxResId, _chipValue));
    }

    /**
     * handle undo button logic
     * choose needed stack by box res id
     * add to this stack (0 - value), value get from placingStack
     * than recalculating, grouping and placing on ui
     * @param _rootLayout
     */
    public static final void undoPreviousPlacing(final RelativeLayout _rootLayout, final TextView _tvBetCount_SG) {
        final int stackSize = placingStack.size();
        if (stackSize == 0) return;

        final PlacingObject placingObject = placingStack.get(stackSize - 1);

        final int boxResId = placingObject.boxResId;
        final int lastPlacedValue = placingObject.chipValue;

        final ArrayList<ChipObject> lastUsedStack = chooseUsedStack(boxResId);

        deleteAllChipsFromUiByStack(_rootLayout, lastUsedStack);
        lastUsedStack.add(new ChipObject(null, 0 - lastPlacedValue));
        groupChips(lastUsedStack);
        placeChipOnUi(lastUsedStack, _rootLayout, boxResId);
        updateCurrentBetView(_tvBetCount_SG);

        placingStack.remove(stackSize - 1);
    }

    /**
     * get sum of values from all box's stacks
     * @return
     */
    public static final int getAllStacksSum() {
        int sum = 0;
        sum += getStackChipsSum(playerPairStack);
        sum += getStackChipsSum(playerStack);
        sum += getStackChipsSum(tieStack);
        sum += getStackChipsSum(bankerStack);
        sum += getStackChipsSum(bankerPairStack);
        return sum;
    }

    /**
     * get all placed chips
     * prepare string for new bets api
     * format "1-4,2-6,4-5"
     * @return
     */
    public static final String getNewBetsString() {
        String newBets = "";

        int sum = getStackChipsSum(playerStack);
        if (sum != 0) newBets += PLAYER_BOX_NUM + "-" + sum + ",";

        sum = getStackChipsSum(bankerStack);
        if (sum != 0) newBets += BANKER_BOX_NUM + "-" + sum + ",";

        sum = getStackChipsSum(tieStack);
        if (sum != 0) newBets += TIE_BOX_NUM + "-" + sum + ",";

        sum = getStackChipsSum(playerPairStack);
        if (sum != 0) newBets += PLAYER_PAIR_BOX_NUM + "-" + sum + ",";

        sum = getStackChipsSum(bankerPairStack);
        if (sum != 0) newBets += BANKER_PAIR_BOX_NUM + "-" + sum + ",";

        //delete last delimiter
        if (newBets.length() > 0)
            newBets = newBets.substring(0, newBets.length() - 1);

        return newBets;
    }

    /**
     * clear view and stacks from chip after new bets confirmed
     * and winner is new round started
     */
    public static final void deleteAllChipsFormViewAndStacks(final Activity _activity) {
        final RelativeLayout rootLayout = (RelativeLayout) _activity.findViewById(R.id.rlRoot_SG);
        final TextView tvBetCount_SG = (TextView) _activity.findViewById(R.id.tvBetCount_SG);
        clearAllBoxes(rootLayout);
        clearAllStacks();
        updateCurrentBetView(tvBetCount_SG);
    }

    private static final void moveViewToBottom(ImageView _view){
        ((RelativeLayout.LayoutParams)_view.getLayoutParams()).bottomMargin = 0;
    }

    public static final void hideAndMoveBetZone(final Activity _activity){
        final RelativeLayout root = (RelativeLayout) _activity.findViewById(R.id.rlRoot_SG);
        root.findViewById(R.id.ivPlayerPair_SG).setVisibility(View.INVISIBLE);

        ImageView banker = (ImageView) root.findViewById(R.id.ivBanker_SG);
        banker.setVisibility(View.INVISIBLE) ;
        moveViewToBottom(banker);

        ImageView player = (ImageView) root.findViewById(R.id.ivPlayer_SG);
                  player.setVisibility(View.INVISIBLE);
        moveViewToBottom(player);

        root.findViewById(R.id.ivTie_SG).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.ivBankerPair_SG).setVisibility(View.INVISIBLE);
        
        /**
    	 * developer Sam
    	 * 2014 年 3月29日
    	 * if hide the bet zone, need to show bottom bar,
    	 * delete old chips and init chips on bottom bar according to placing stacks
    	 */
        FooterBoxWorker.deleteAllChipsFromFooter();
        FooterBoxWorker.initFooterChips();
        FooterBoxWorker.showFooterBox();

    }
    /**
     * need save current placing stack due to cleaning
     */
    public static final void updateRepeatStack() {
        repeatStack.clear();
        repeatStack.addAll(placingStack);
    }

    /**
     * delete all chips in ui and place last
     * @param _rootLayout
     * @param _tvBetCount_SG
     */
    public static final void placeLastBetOnView(final Activity _activity, final RelativeLayout _rootLayout, final TextView _tvBetCount_SG) {
        if (repeatStack.size() == 0) return;

        deleteAllChipsFormViewAndStacks(_activity);

        for (int i = 0; i < repeatStack.size(); i++) {
            final int boxResId = repeatStack.get(i).boxResId;
            final int chipResId = getChipViewResIdByValue(repeatStack.get(i).chipValue);

            placingChip(_rootLayout, boxResId, chipResId, _tvBetCount_SG);
        }
    }
    
    /**
     * get all stacks
     * Developer Sam
     * 2014年3月29日
     * @return List<Object> a list contains all using stacks
     */
    public static final List<Object> getAllStacks(){
    	List<Object>  _listStacks = new ArrayList<Object>();
        _listStacks.add(playerPairStack);
        _listStacks.add(playerStack);
        _listStacks.add(tieStack);
        _listStacks.add(bankerStack);
        _listStacks.add(bankerPairStack);
        return _listStacks;
    }

    /**
     * place Chips On Footer bar, because I don't know whether I can update the placeChipOnUi() function
     * so, I write another one, but I think the best way is to update the placeChipOnUi() function which was written by you
     * Developer Sam
     * 2014年3月29日
     * @param _listChipsToPlace
     * @param _rootLayout
     * @param _boxResId
     */
    public static final void placeChipOnFooter(final ArrayList<ChipObject> _listChipsToPlace, final RelativeLayout _rootLayout,
            final int _boxResId) {   
    	
    	/**
         * Developer Sam
         * 2014年3月29日
         * if there are more than 6 chips in the _listChips
         * replace with one “magic chip” 
         */
        if(_listChipsToPlace.size()>6){
    		int tempSum = getStackChipsSum(_listChipsToPlace);
    		_listChipsToPlace.clear();
    		_listChipsToPlace.add(new ChipObject(null,tempSum));
    	}  
        
    	for (int i = 0; i < _listChipsToPlace.size(); i++) {
            final int chipValue = _listChipsToPlace.get(i).value;
            final int chipImageId = getImageIdByChipValue(chipValue);

            final float chipBottomOffset = calculateChipBottomOffset(_rootLayout, _boxResId);

            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, _boxResId);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, _boxResId);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, _boxResId);
            layoutParams.setMargins(0, 0, 0, (int) (chipBottomOffset + 5f * i));

            final ImageView ivChip = new ImageView(_rootLayout.getContext());
            ivChip.setScaleType(ImageView.ScaleType.FIT_END);
            ivChip.setImageResource(chipImageId);
            ivChip.setLayoutParams(layoutParams);

            _listChipsToPlace.get(i).imageView = ivChip;
            _rootLayout.addView(ivChip);
        }
    }
    
    /**
     * delete all chips from bet zones
     * 
     * Developer Sam
     * 2014年3月29日
     * @param _activity
     */
    public static final void deleteAllChipsFormTraies(final Activity _activity) {
        final RelativeLayout rootLayout = (RelativeLayout) _activity.findViewById(R.id.rlRoot_SG);
        final TextView tvBetCount_SG = (TextView) _activity.findViewById(R.id.tvBetCount_SG);
        clearAllBoxes(rootLayout);
    }
    
}