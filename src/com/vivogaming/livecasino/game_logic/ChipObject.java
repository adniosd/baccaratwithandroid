package com.vivogaming.livecasino.game_logic;

import android.widget.ImageView;

public final class ChipObject {
    public ImageView imageView;
    public int value;

    public ChipObject(final ImageView _imageView, final int _value) {
        imageView = _imageView;
        value = _value;
    }
}
