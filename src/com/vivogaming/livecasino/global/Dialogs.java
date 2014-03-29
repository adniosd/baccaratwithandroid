package com.vivogaming.livecasino.global;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.Login;
import com.vivogaming.livecasino.screens.register.CountryAdapter;
import com.vivogaming.livecasino.screens.topmenu.Help;
import com.vivogaming.livecasino.screens.topmenu.History;
import com.vivogaming.livecasino.screens.topmenu.Limits;
import com.vivogaming.livecasino.screens.topmenu.SettingsDialog;

import java.util.HashMap;
import java.util.Observer;

import static com.vivogaming.livecasino.global.BaccaratApp.printErrorLogToFile;
import static com.vivogaming.livecasino.global.Constants.ERROR_DESCRIPTION;
import static com.vivogaming.livecasino.global.LocationWorker.checkNetworkLocation;
import static com.vivogaming.livecasino.global.Logout.doLogout;
import static com.vivogaming.livecasino.screens.game.ViewInteractionHandler.doRequestGetActiveTables;

public abstract class Dialogs {

    /**
     * Uses to call alert dialog for language and currency
     * @param activity picked Activity
     * @param pick_title set text to tittle of dialog
     * @param content_array set array that used for dialog content view
     * @param textView button where we set text from dialog
     */
    public static void dialogsForSR(final Activity activity, int pick_title, final String[] content_array, final TextView textView){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(pick_title);
        builder.setItems(content_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                textView.setTextColor(activity.getResources().getColor(R.color.black));
                textView.setText(content_array[which]);
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Uses to call alert dialog with list for Country pickers
     * @param activity picked Activity
     * @param adapter date for dialog in adapter
     * @param textView put selected date to textView
     * @param widthDialog width of dialog equals width of button ChooseCountry
     */
    public static void show_alert(final Activity activity, final CountryAdapter adapter, final TextView textView, int widthDialog) {
        final Dialog dia = new Dialog(activity);
        dia.setContentView(R.layout.list_country);
        dia.setCancelable(true);
        final String[] text = new String[1];

        ListView list_alert = (ListView) dia.findViewById(R.id.llListCountry_RS);
        list_alert.setAdapter(adapter);
        list_alert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                text[0] = adapter.getItem(pos).name;
                textView.setTextColor(activity.getResources().getColor(R.color.black));
                textView.setText(text[0]);
                dia.dismiss();
            }
        });
        dia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dia.getWindow().setLayout(widthDialog,500);
        dia.show();
    }


    /**
     * show error dialog with "Error" title
     * @param _activity where
     * @param _errorMap statusCode, description
     */
    public static final void showErrorDialog(final Activity _activity, final HashMap<String, String> _errorMap) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DI = (TextView) dialog.findViewById(R.id.tvTitle_DI);
        final TextView tvMessage_DI = (TextView) dialog.findViewById(R.id.tvMessage_DI);
        final ImageButton btnPositive_DI = (ImageButton) dialog.findViewById(R.id.btnPositive_DI);
        final TextView tvBtnPositive_DI = (TextView) dialog.findViewById(R.id.tvBtnPositive_DI);

        tvTitle_DI.setText(R.string.error);
        final String errorDescriptionFromMap = _errorMap.get(ERROR_DESCRIPTION);
        //if bad token than show special message
        if (errorDescriptionFromMap.contains("error=2") || errorDescriptionFromMap.contains("error 101")) {
            tvMessage_DI.setText(_activity.getString(R.string.you_have_signed_in_from_another_location));
        } else {
            tvMessage_DI.setText(errorDescriptionFromMap);
        }

        tvBtnPositive_DI.setText(android.R.string.ok);

        btnPositive_DI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                dialog.dismiss();
//                if (_activity instanceof Game) {
//                    _activity.finish();
                    _activity.startActivity(new Intent(_activity, Login.class));
                    _activity.finish();
//                }
            }
        });

		//if error appears and activity is destroyed
		try {
        	dialog.show();
		} catch (final WindowManager.BadTokenException _e) {
			_e.printStackTrace();
            printErrorLogToFile(_e, "showing dialog error");
		}
    }

    public static final void showBetOrTipFailDialog(final Activity _activity, final String _message) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DI = (TextView) dialog.findViewById(R.id.tvTitle_DI);
        final TextView tvMessage_DI = (TextView) dialog.findViewById(R.id.tvMessage_DI);
        final ImageButton btnPositive_DI = (ImageButton) dialog.findViewById(R.id.btnPositive_DI);
        final TextView tvBtnPositive_DI = (TextView) dialog.findViewById(R.id.tvBtnPositive_DI);

        tvTitle_DI.setText(R.string.error);
        tvMessage_DI.setText(_message);
        tvBtnPositive_DI.setText(android.R.string.ok);

        btnPositive_DI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View _view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

	public static final void showDialogLimitsContent(final Activity _activity, final int[][] _limits){
        final Limits limitsContent = new Limits(_activity);
        limitsContent.show();
        limitsContent.setMinMaxDPlayer(_limits[0]);
        limitsContent.setMinMaxPlayer(_limits[1]);
        limitsContent.setMinMaxTie(_limits[2]);
        limitsContent.setMinMaxBanker(_limits[3]);
        limitsContent.setMinMaxDBanker(_limits[4]);
    }

    public static final void showDialogSettingsContent(final Activity _activity){
        final SettingsDialog settingsContent = new SettingsDialog(_activity);
        settingsContent.show();
    }

    public static final void showDialogHelpContent(final Activity _activity){
        final Help helpContent = new Help(_activity);
        helpContent.show();
    }

    public static final void showDialogHistoryContent(final Activity _activity, final String _resultGamePlayerHistory){
        final History historyContent = new History(_activity, _resultGamePlayerHistory);
        historyContent.show();
    }

    /**
     * non-cancelable, closes activity when pressing on button
     * @param _activity
     */
    public static final void showDialogNoConnection(final Activity _activity){
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DI = (TextView) dialog.findViewById(R.id.tvTitle_DI);
        final TextView tvMessage_DI = (TextView) dialog.findViewById(R.id.tvMessage_DI);
        final TextView tvBtnPositive_DI = (TextView) dialog.findViewById(R.id.tvBtnPositive_DI);
        tvBtnPositive_DI.setText(android.R.string.ok);
        tvTitle_DI.setText(_activity.getString(R.string.warning));
        tvMessage_DI.setText(_activity.getString(R.string.no_internet_connection));

        final ImageButton btnPositive_DI = (ImageButton) dialog.findViewById(R.id.btnPositive_DI);
        btnPositive_DI.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                _activity.finish();
            }
        });

        dialog.show();
    }

    public static final void showDialogNetworkLocation(final Activity _activity) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final TextView tvTitle_DYN = (TextView) dialog.findViewById(R.id.tvTitle_DYN);
        final TextView tvQuestion_DYN = (TextView) dialog.findViewById(R.id.tvMessage_DYN);
        final TextView tvBtnNegative_DYN = (TextView) dialog.findViewById(R.id.tvBtnNegative_DYN);
        final TextView tvBtnPositive_DYN = (TextView) dialog.findViewById(R.id.tvBtnPositive_DYN);
        tvBtnNegative_DYN.setText(android.R.string.no);
        tvBtnPositive_DYN.setText(android.R.string.yes);
        tvTitle_DYN.setText(_activity.getString(R.string.network_location));
        tvQuestion_DYN.setText(_activity.getString(R.string.turn_on_network_location_question));

        final ImageButton btnNegative_DYN = (ImageButton) dialog.findViewById(R.id.btnNegative_DYN);
        btnNegative_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                _activity.finish();
            }
        });

        final ImageButton btnPositive_DYN = (ImageButton) dialog.findViewById(R.id.btnPositive_DYN);

        btnPositive_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                _activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * finishing activity when ok pressed
     * @param _activity
     */
    public static final void showDialogProhibitedCounty(final Activity _activity) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DI = (TextView) dialog.findViewById(R.id.tvTitle_DI);
        final TextView tvMessage_DI = (TextView) dialog.findViewById(R.id.tvMessage_DI);
        final TextView tvBtnPositive_DI = (TextView) dialog.findViewById(R.id.tvBtnPositive_DI);
        tvBtnPositive_DI.setText(android.R.string.ok);
        tvTitle_DI.setText(_activity.getString(R.string.warning));
        tvMessage_DI.setText(_activity.getString(R.string.casino_prohibited_in_your_country));

        final ImageButton btnPositive_DI = (ImageButton) dialog.findViewById(R.id.btnPositive_DI);
        btnPositive_DI.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                _activity.finish();
            }
        });

        dialog.show();
    }

    public static final void showDialogUnableToIdentifyLocation(final Activity _activity) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DYN = (TextView) dialog.findViewById(R.id.tvTitle_DYN);
        final TextView tvQuestion_DYN = (TextView) dialog.findViewById(R.id.tvMessage_DYN);
        final TextView tvBtnNegative_DYN = (TextView) dialog.findViewById(R.id.tvBtnNegative_DYN);
        final TextView tvBtnPositive_DYN = (TextView) dialog.findViewById(R.id.tvBtnPositive_DYN);
        tvBtnNegative_DYN.setText(android.R.string.cancel);
        tvBtnPositive_DYN.setText(R.string.retry);
        tvTitle_DYN.setText(_activity.getString(R.string.unable_to_identify_location));
        tvQuestion_DYN.setText(_activity.getString(R.string.access_declined));

        final ImageButton btnPositive_DYN = (ImageButton) dialog.findViewById(R.id.btnPositive_DYN);
        btnPositive_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                dialog.dismiss();
                checkNetworkLocation(_activity);
            }
        });

        final ImageButton btnNegative_DYN = (ImageButton) dialog.findViewById(R.id.btnNegative_DYN);
        btnNegative_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                _activity.finish();
            }
        });

        dialog.show();
    }

    /**
     * shows in choose table screen
     * @param _activity
     * @param _observer
     */
    public static final void showDialogLogout(final Activity _activity, final Observer _observer) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DYN = (TextView) dialog.findViewById(R.id.tvTitle_DYN);
        final TextView tvQuestion_DYN = (TextView) dialog.findViewById(R.id.tvMessage_DYN);
        final TextView tvBtnNegative_DYN = (TextView) dialog.findViewById(R.id.tvBtnNegative_DYN);
        final TextView tvBtnPositive_DYN = (TextView) dialog.findViewById(R.id.tvBtnPositive_DYN);
        tvBtnNegative_DYN.setText(R.string.no);
        tvBtnPositive_DYN.setText(R.string.yes);
        tvTitle_DYN.setText(_activity.getString(R.string.log_out));
        tvQuestion_DYN.setText(_activity.getString(R.string.log_out_and_exit_the_app_question));

        final ImageButton btnPositive_DYN = (ImageButton) dialog.findViewById(R.id.btnPositive_DYN);
        btnPositive_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                dialog.dismiss();
                doLogout(_activity, _observer);
            }
        });

        final ImageButton btnNegative_DYN = (ImageButton) dialog.findViewById(R.id.btnNegative_DYN);
        btnNegative_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static final void showDialogGoToLobby(final Activity _activity) {
        final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvTitle_DYN = (TextView) dialog.findViewById(R.id.tvTitle_DYN);
        final TextView tvQuestion_DYN = (TextView) dialog.findViewById(R.id.tvMessage_DYN);
        final TextView tvBtnNegative_DYN = (TextView) dialog.findViewById(R.id.tvBtnNegative_DYN);
        final TextView tvBtnPositive_DYN = (TextView) dialog.findViewById(R.id.tvBtnPositive_DYN);
        tvBtnNegative_DYN.setText(R.string.no);
        tvBtnPositive_DYN.setText(R.string.yes);
        tvTitle_DYN.setText(_activity.getString(R.string.lobby));
        tvQuestion_DYN.setText(_activity.getString(R.string.go_back_to_mail_lobby_question));

        final ImageButton btnPositive_DYN = (ImageButton) dialog.findViewById(R.id.btnPositive_DYN);
        btnPositive_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                dialog.dismiss();
                doRequestGetActiveTables(_activity);
            }
        });

        final ImageButton btnNegative_DYN = (ImageButton) dialog.findViewById(R.id.btnNegative_DYN);
        btnNegative_DYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View _view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
