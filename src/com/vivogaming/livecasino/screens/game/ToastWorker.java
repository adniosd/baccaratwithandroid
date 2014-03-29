package com.vivogaming.livecasino.screens.game;

import static com.vivogaming.livecasino.global.Constants.TIME_SHOWING_TOAST;
import static com.vivogaming.livecasino.global.Constants.TIME_TOAST_DELAY;
import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.ChipsPlacing;
import com.vivogaming.livecasino.game_logic.GameViewWorker;

/**
 * User: ZOG
 * Date: 11.11.13
 * Time: 11:16
 */
public abstract class ToastWorker {
    private static Toast toastWithText;
    private static Toast toastWithImage;
    private static TextView tvOnToastWithText;
    private static TextView tvOnToastWithImage;
    private static Handler toastHandler;

    /**
     * set toast gravity approximately centered on videoview
     * set custom layout to toastWithText
     * @param _activity
     * @param _metrics
     * @param _rlRightPanel_SG
     */
    public static final void prepareToastWithText(final Activity _activity, final DisplayMetrics _metrics,
                                                   final RelativeLayout _rlRightPanel_SG) {
        final int rightPanelWidth = _rlRightPanel_SG.getMeasuredWidth();
        final int screenWidth = _metrics.widthPixels;          //total screen width
        final int screenHeight = _metrics.heightPixels;          //total screen width
        final int videoViewWidth = screenWidth - rightPanelWidth;

        final LayoutInflater inflater = _activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast_message_border,
                (ViewGroup) _activity.findViewById(R.id.llToastBgrnd_TMB));
        tvOnToastWithText = (TextView) layout.findViewById(R.id.tvMessage_TMB);

        toastWithText = new Toast(_activity);
        toastWithText.setDuration(Toast.LENGTH_SHORT);
        toastWithText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT, videoViewWidth / 3, screenHeight / 4);
        toastWithText.setView(layout);

        toastHandler = new Handler();
    }

    public static final void prepareToastWithImage(final Activity _activity, final DisplayMetrics _metrics,
                                                    final RelativeLayout _rlRightPanel_SG) {
        final int rightPanelWidth = _rlRightPanel_SG.getMeasuredWidth();
        final int screenWidth = _metrics.widthPixels;          //total screen width
        final int screenHeight = _metrics.heightPixels;          //total screen width
        final int videoViewWidth = screenWidth - rightPanelWidth;

        final LayoutInflater inflater = _activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast_message,
                (ViewGroup) _activity.findViewById(R.id.llToastBgrnd_TM));
        tvOnToastWithImage = (TextView) layout.findViewById(R.id.tvMessage_TM);

        toastWithImage = new Toast(_activity);
        toastWithImage.setDuration(Toast.LENGTH_SHORT);
        toastWithImage.setGravity(Gravity.TOP | Gravity.LEFT, videoViewWidth / 4, screenHeight / 4);
        toastWithImage.setView(layout);
    }

    public static final void showToast(final String _message) {
        tvOnToastWithText.setText(_message);
        toastWithText.show();
    }

    //toastWithText with chip image
    public static final void showToastWithImage(final String _message, final int _chipImageResId) {
        tvOnToastWithImage.setText(_message);
        tvOnToastWithImage.setCompoundDrawablesWithIntrinsicBounds(_chipImageResId, 0, 0, 0);
        toastWithImage.show();
    }

    //toastWithText with indie delay time (shows winner) if player is not winner
    public static final void showToastWithDelay(final Activity _activity, final String _message,
                                                final RelativeLayout _rlRoot_SG, final TextView _tvBetCount_SG) {
        final RelativeLayout rlWinnerMessage_SG = (RelativeLayout) _activity.findViewById(R.id.rlWinnerMessage_SG);
        final TextView textWinner = (TextView)_activity.findViewById(R.id.tvMessage_SG);
        final TextView tvYouWin_SG = (TextView)_activity.findViewById(R.id.tvYouWin_SG);
        tvYouWin_SG.setVisibility(View.GONE);

        textWinner.setText(_message);
        textWinner.postDelayed(new Runnable() {
            @Override
            public final void run() {
                textWinner.setText(_message);
                rlWinnerMessage_SG.setVisibility(View.VISIBLE);
                rlWinnerMessage_SG.postDelayed(new Runnable() {
                    @Override
                    public final void run() {
                        rlWinnerMessage_SG.setVisibility(View.INVISIBLE);

                        //need clear all stacks here
                        //deleteAllChipsFormViewAndStacks(_activity);
                    }
                }, TIME_SHOWING_TOAST * 1000);
                //one seconds to display
            }
        }, (TIME_TOAST_DELAY - 1) * 1000);
    }

    //toastWithText with indie delay time (if player is winner)
    public static final void showWinnerToastWithDelay(final Activity _activity, final String _message,
                                                final RelativeLayout _rlRoot_SG, final TextView _tvBetCount_SG) {
        final RelativeLayout rlWinnerMessage_SG = (RelativeLayout) _activity.findViewById(R.id.rlWinnerMessage_SG);
        final TextView textWinner = (TextView)_activity.findViewById(R.id.tvMessage_SG);
        final TextView tvYouWin_SG = (TextView)_activity.findViewById(R.id.tvYouWin_SG);
        tvYouWin_SG.setVisibility(View.VISIBLE);

        textWinner.setText(_message);
        textWinner.postDelayed(new Runnable() {
            @Override
            public final void run() {
                textWinner.setText(_message);
                rlWinnerMessage_SG.setVisibility(View.VISIBLE);
                rlWinnerMessage_SG.postDelayed(new Runnable() {
                    @Override
                    public final void run() {
                        rlWinnerMessage_SG.setVisibility(View.INVISIBLE);

                        //need clear all stacks here
                        //deleteAllChipsFormViewAndStacks(_activity);
                    }
                }, TIME_SHOWING_TOAST * 1000);
                //one seconds to display
            }
        }, (TIME_TOAST_DELAY - 1) * 1000);
    }
    /**
	 * show winner box toast with delay
	 * @author Sam 2014年3月21日
	 * @param _activity
	 */
	public static final void showWinnerBoxToastWithDelay(final Activity _activity) {
		Runnable toastRunnable = new Runnable() {
			@Override
			public final void run() {
				/**
				 * write by Sam show WinnerBox
				 */
				WinnerWorker.showWinnerBox();
				toastHandler.postDelayed(new Runnable() {
					@Override
					public final void run() {
						/**
						 * write by Sam restore WinnerBox
						 */
						WinnerWorker.restoreWinnerBox();
						
						/**
						 * clear all stacks here
						 */
						FooterBoxWorker.deleteAllChipsFromFooter();
						ChipsPlacing.clearAllStacks();
						FooterBoxWorker.updateFooterValues();
						GameViewWorker.updateCurrentBet(Game.getGameActivity());
						// need clear all stacks here
						//deleteAllChipsFormViewAndStacks(_activity);
					}
				}, TIME_SHOWING_TOAST * 1000);
				// one seconds to display
			}
		};
		toastHandler.postDelayed(toastRunnable, (TIME_TOAST_DELAY - 1) * 1000);
	}
}