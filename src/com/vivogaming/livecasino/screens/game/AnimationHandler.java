package com.vivogaming.livecasino.screens.game;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.vivogaming.livecasino.R;

import static com.vivogaming.livecasino.global.Constants.LEFT_SLIDING_MENU_ANIMATION;
import static com.vivogaming.livecasino.global.Constants.RIGHT_SLIDING_MENU_ANIMATION;

public abstract class AnimationHandler {
    /**
     *
     * @param _rlHeadMenu It starts and closes main menu
     * @param _imbTopMenu SetVisible - serves as information about whether or not Accessed
     * @param _context Context
     *
     * During the animation button is in a state disabled
     */

    public static final void startOpenAnimationMenu(final RelativeLayout _rlHeadMenu,
                                                   final ImageButton _imbTopMenu,
                                                   final Context _context){

        final Animation animtranslate = AnimationUtils.loadAnimation(_context, R.anim.translate_top_menu_open);
        animtranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public final void onAnimationStart(final Animation animation) {
                _rlHeadMenu.setVisibility(View.VISIBLE);
                _imbTopMenu.setEnabled(false);
            }

            @Override
            public final void onAnimationEnd(final Animation animation) {
                _imbTopMenu.setEnabled(true);
            }

            @Override
            public final void onAnimationRepeat(final Animation animation) {
            }
        });
        _rlHeadMenu.startAnimation(animtranslate);
    }

    public static final void startCloseAnimationMenu(final RelativeLayout _rlHeadMenu,
                                                   final ImageButton _imbTopMenu,
                                                   final Context _context){

        final Animation  animtranslate = AnimationUtils.loadAnimation(_context, R.anim.translate_top_menu_closed);
        animtranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public final void onAnimationStart(final Animation animation) {
                _imbTopMenu.setEnabled(false);
            }

            @Override
            public final void onAnimationEnd(final Animation animation) {
                _rlHeadMenu.setVisibility(View.INVISIBLE);
                _imbTopMenu.setEnabled(true);
            }

            @Override
            public final void onAnimationRepeat(final Animation animation) {
            }
        });
        _rlHeadMenu.startAnimation(animtranslate);
    }

    public static final void startOpenAnimationMenuMoney(final LinearLayout _llHeadMenuMoney,
                                                    final ImageButton _imbTopMenuMoney,
                                                    final Context _context){

        final Animation animtranslate = AnimationUtils.loadAnimation(_context, R.anim.translate_top_menu_money_open);
        animtranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public final void onAnimationStart(final Animation animation) {
                _llHeadMenuMoney.setVisibility(View.VISIBLE);
                _imbTopMenuMoney.setEnabled(false);
            }

            @Override
            public final void onAnimationEnd(final Animation animation) {
                _imbTopMenuMoney.setEnabled(true);
            }

            @Override
            public final void onAnimationRepeat(final Animation animation) {
            }
        });
        _llHeadMenuMoney.startAnimation(animtranslate);
    }

    public static final void startCloseAnimationMenuMoney(final LinearLayout _llHeadMenuMoney,
                                                     final ImageButton _imbTopMenuMoney,
                                                     final Context _context){

        final Animation  animtranslate = AnimationUtils.loadAnimation(_context, R.anim.translate_top_menu_money_closed);
        animtranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public final void onAnimationStart(final Animation animation) {
                _imbTopMenuMoney.setEnabled(false);
            }

            @Override
            public final void onAnimationEnd(final Animation animation) {
                _llHeadMenuMoney.setVisibility(View.INVISIBLE);
                _imbTopMenuMoney.setEnabled(true);
            }

            @Override
            public final void onAnimationRepeat(final Animation animation) {
            }
        });
        _llHeadMenuMoney.startAnimation(animtranslate);
    }

    /**
     * Animation of left and right slide of leftSlidingMenu
     * @param _rlLeftPanelGrid_SG relativeLayout of leftSlidingMenu (object of animation)
     * @param _leftToLeft coordinates continue of animation
     * @param _widthSliderLeft coordinates start of animation
     */
    public static final void animationLeftGridScore(View _rlLeftPanelGrid_SG, int _leftToLeft,int _widthSliderLeft) {
        int dest = 0 - _leftToLeft;
        if (_rlLeftPanelGrid_SG.getX() < 0 - _widthSliderLeft) {
            dest = 0 - _widthSliderLeft;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(_rlLeftPanelGrid_SG, "x", dest);
        animator.setDuration(LEFT_SLIDING_MENU_ANIMATION);
        animator.start();
    }


    /**
     * Animation of left and right slide of rightSlidingMenu
     * @param _rlRightSlidingMenu_SG relativeLayout of rightSlidingMenu (object of animation)
     * @param _rightToRight + _widthSliderRight : coordinates continue of animation
     * @param _widthSliderRight coordinates start of animation
     */
    public static final void animationRightSlidingMenu(final View _rlRightSlidingMenu_SG,int _rightToRight, int _widthSliderRight){
        int dest = _widthSliderRight + _rightToRight;

        if (_rlRightSlidingMenu_SG.getX() > _widthSliderRight) {
            dest = _widthSliderRight;
        }

        final ObjectAnimator animator = ObjectAnimator.ofFloat(_rlRightSlidingMenu_SG, "x", dest);
        animator.setDuration(RIGHT_SLIDING_MENU_ANIMATION);
        animator.start();
    }
}
