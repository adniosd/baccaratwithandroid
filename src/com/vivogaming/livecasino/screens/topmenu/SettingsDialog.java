package com.vivogaming.livecasino.screens.topmenu;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import com.vivogaming.livecasino.R;

import static com.vivogaming.livecasino.global.Constants.TIME_ADDITIONAL_DELAY;
import static com.vivogaming.livecasino.global.Constants.TIME_TOAST_DELAY;
import static com.vivogaming.livecasino.global.Variables.*;

public class SettingsDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    private SeekBar sbVolume;
    private Switch  swOnOffVolume;
    private AudioManager audioManager;
    private Context context;

    private SeekBar sbWinNotifDelay_DMS, sbAdditionalDelay_DMS;
    private TextView tvWinDelay_DMS, tvAdditionalDelayVal_DMS;

    public SettingsDialog(final Context _context)
    {
        // Set your theme here
        super(_context, R.style.MenuContentThemes);
        context = _context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.dialog_menu_settings);

        final double width = getContext().getResources().getDimension(R.dimen.menu_content_width);
        final double height = getContext().getResources().getDisplayMetrics().heightPixels * 0.9;
        getWindow().setLayout((int) Math.round(width), (int) Math.round(height));

        sbVolume = (SeekBar) findViewById(R.id.sbVolume_SMS);
        swOnOffVolume = (Switch) findViewById(R.id.swOnOff_SMS);

        prepareSoundControl();

        //register OnSeekBarChangeListener, so that the seek bar can change the volume
        sbVolume.setOnSeekBarChangeListener(this);
        swOnOffVolume.setOnCheckedChangeListener(this);

        sbWinNotifDelay_DMS         = (SeekBar) findViewById(R.id.sbWinNotifDelay_DMS);
        sbAdditionalDelay_DMS       = (SeekBar) findViewById(R.id.sbAdditionalDelay_DMS);
        tvWinDelay_DMS              = (TextView) findViewById(R.id.tvWinDelay_DMS);
        tvAdditionalDelayVal_DMS    = (TextView) findViewById(R.id.tvAdditionalDelayVal_DMS);

        sbWinNotifDelay_DMS.setMax(15);
        sbAdditionalDelay_DMS.setMax(5);

        sbWinNotifDelay_DMS.setProgress(TIME_TOAST_DELAY);
        sbAdditionalDelay_DMS.setProgress(TIME_ADDITIONAL_DELAY);

        tvWinDelay_DMS.setText(TIME_TOAST_DELAY + " sec");
        tvAdditionalDelayVal_DMS.setText(TIME_ADDITIONAL_DELAY + " sec");

        sbWinNotifDelay_DMS.setOnSeekBarChangeListener(winNotificationDelayListener);
        sbAdditionalDelay_DMS.setOnSeekBarChangeListener(additionalDelayListener);

        findViewById(R.id.btnCancelForSettings_SMS).setOnClickListener(this);
    }

    private final void prepareSoundControl() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        sbVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        setSwitchStatus();

        if (!getSwitchSound()) {
            sbVolume.setEnabled(false);
            sbVolume.setProgress(0);
            setSteamMusicVolume(0);
        } else {
            final int volumeLevel = getProgressVolume();
            sbVolume.setEnabled(true);
            setSteamMusicVolume(volumeLevel);
            sbVolume.setProgress(volumeLevel);
        }
    }

    @Override
    public final void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public final void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public final void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (fromUser) {
            //change the volume, displaying a toast message containing the current volume and playing a feedback sound
            setSteamMusicVolume(progress);
            setProgressVolume(progress);
        }
    }

    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        //if one of the volume keys were pressed
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            //change the seek bar progress indicator position
            final int volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            setProgressVolume(volumeLevel);
            sbVolume.setProgress(volumeLevel);
        }
        //propagate the key event
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            setSteamMusicVolume(0);
            sbVolume.setProgress(0);
            sbVolume.setEnabled(false);
            setSwitchSound(isChecked);
        } else {
            final int volumeLevel = getProgressVolume();
            setSteamMusicVolume(volumeLevel);
            sbVolume.setEnabled(true);
            sbVolume.setProgress(volumeLevel);
            setProgressVolume(volumeLevel);
            setSwitchSound(isChecked);
        }
    }


    public void onClick(View v) {
        dismiss();
    }

    public void setSteamMusicVolume(final int _volume) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, _volume, 0);
    }

    public void setSwitchStatus() {
        swOnOffVolume.setChecked(getSwitchSound());
    }

    private final SeekBar.OnSeekBarChangeListener winNotificationDelayListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int _progress, boolean _fromUser) {
            if (!_fromUser) return;

            TIME_TOAST_DELAY = _progress;
            tvWinDelay_DMS.setText(_progress + " sec");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private final SeekBar.OnSeekBarChangeListener additionalDelayListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int _progress, boolean _fromUser) {
            if (!_fromUser) return;

            TIME_ADDITIONAL_DELAY = _progress;
            tvAdditionalDelayVal_DMS.setText(_progress + " sec");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };
}