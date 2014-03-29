package com.vivogaming.livecasino.global;

import android.content.Context;
import android.media.AudioManager;

import static com.vivogaming.livecasino.global.Variables.getProgressVolume;
import static com.vivogaming.livecasino.global.Variables.getSwitchSound;

/**
 * User: Assasin
 * Date: 28.10.13
 * Time: 9:29
 */
public abstract class System {

    public static final void loadVolumeSound(final Context _context) {
        final AudioManager audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);

        if (getSwitchSound()) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getProgressVolume(), 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }
}