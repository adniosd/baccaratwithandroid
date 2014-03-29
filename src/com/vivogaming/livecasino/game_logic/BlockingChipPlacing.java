package com.vivogaming.livecasino.game_logic;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.game.FooterBoxWorker;

/**
 * handle block layout behaviour
 */
public abstract class BlockingChipPlacing {
    //isChipPlacing boolean used to change visibility of block layout
    private static boolean isChipPlacing = true;
    private static boolean isBetZoneVisible = true;


    /**
     * return status of chip placing
     * @return
     */
    public static final boolean isChipPlacingAllowed() {
        return isChipPlacing;
    }

    /**
     * set boolean value that handle block layout behaviour
     * @param _isActivated
     */
    public static final void setChipPlacingActivated(final boolean _isActivated) {
        isChipPlacing = _isActivated;
        isBetZoneVisible = _isActivated;
    }

    /**
     * set block layout visible
     */
    public static final void deactivateChipPlacing(final Activity _activity) {
        final RelativeLayout rlBlock_SG = (RelativeLayout) _activity.findViewById(R.id.rlBlock_SG);
        rlBlock_SG.setVisibility(View.VISIBLE);
    }

    /**
     * set block layout invisible
     */
    public static final void activateChipPlacing(final Activity _activity) {
        final RelativeLayout rlBlock_SG = (RelativeLayout) _activity.findViewById(R.id.rlBlock_SG);
        rlBlock_SG.setVisibility(View.INVISIBLE);

        final RelativeLayout rootLayout = (RelativeLayout) _activity.findViewById(R.id.rlRoot_SG);
        showBetZone(rootLayout, _activity);  //todo
    }
    private static final void showBetZone(final RelativeLayout _root, final Context _context){
        _root.findViewById(R.id.ivPlayerPair_SG).setVisibility(View.VISIBLE);

         ImageView banker = (ImageView) _root.findViewById(R.id.ivBanker_SG);
         setBottomMargin(banker, GameViewWorker.getBankerViewBottomMargin(_context));
         banker.setVisibility(View.VISIBLE);

       ImageView player = (ImageView) _root.findViewById(R.id.ivPlayer_SG);
       setBottomMargin(player, GameViewWorker.getBankerViewBottomMargin(_context));
       player.setVisibility(View.VISIBLE);


        _root.findViewById(R.id.ivTie_SG).setVisibility(View.VISIBLE);
        _root.findViewById(R.id.ivBankerPair_SG).setVisibility(View.VISIBLE);
        
        /**
         * developer Sam
         * 2014 年 3月29日
         * if show bet zone
         * need to hide footer bar
         * so, delete all chips from footer bar and hide footer bar
         */
        FooterBoxWorker.deleteAllChipsFromFooter();
        FooterBoxWorker.hideFooterBox();

    }

    private static void setBottomMargin(ImageView _view, final int _margin){
            ((RelativeLayout.LayoutParams)_view.getLayoutParams()).bottomMargin = _margin;
    }

}
