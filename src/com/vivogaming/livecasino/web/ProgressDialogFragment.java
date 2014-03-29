package com.vivogaming.livecasino.web;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vivogaming.livecasino.R;

import static com.vivogaming.livecasino.web.RequestPool.cancelRequest;

/**
 * class for dialog
 * need for changing orientation
 */
public final class ProgressDialogFragment extends DialogFragment {
    private static ProgressDialogFragment progressDialogFragment;

    @Override
    public final Dialog onCreateDialog(final Bundle _savedInstanceState) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading___));
        progressDialog.setCanceledOnTouchOutside(false);
        setCancelable(true);
        return progressDialog;
    }

    public final View onCreateView(final LayoutInflater _inflater, final ViewGroup _container, final Bundle _savedInstanceState) {
        final View dialogView = _inflater.inflate(R.layout.dialog_progress_with_message, null);

        final TextView tvMessage_DPWM = (TextView) dialogView.findViewById(R.id.tvMessage_DPWM);
        tvMessage_DPWM.setText(getString(R.string.loading___));

        final int width = getResources().getDimensionPixelSize(R.dimen.width_dialog_loading);
        final int height = getResources().getDimensionPixelSize(R.dimen.height_dialog_loading);
        getDialog().getWindow().setLayout(width, height);

        return dialogView;
    }

    @Override
    public final void onCancel(final DialogInterface _dialogInterface) {
        //opportunity to cancel requests to server
        cancelRequest();
    }

    /**
     * start progress dialog for indicating response in specified activity
     * @param _activity
     */
    public static final void startProgressDialog(final Activity _activity) {
        final FragmentManager fragmentManager = _activity.getFragmentManager();
        progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.show(fragmentManager, "Dialog");
    }

    public static final void stopProgressDialog() {
        try {
            if (progressDialogFragment != null) progressDialogFragment.dismiss();
        } catch (final IllegalStateException _e) {
            _e.printStackTrace();
        }
    }
}
