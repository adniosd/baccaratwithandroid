package com.vivogaming.livecasino.screens.game.history;

import android.graphics.Paint;

/**
 * Created by Sofia on 10/24/13.
 */
public abstract class InitPaint {

    public static final Paint customInitFillColor() {
        final Paint fillColorAndText = new Paint();
        fillColorAndText.setAntiAlias(true);
        fillColorAndText.setDither(true);
        fillColorAndText.setStrokeWidth(1);
        fillColorAndText.setTextSize(27);
        fillColorAndText.setColor(0xffffb0af);
        fillColorAndText.setStyle(Paint.Style.FILL_AND_STROKE);
        return fillColorAndText;
    }

    public static final Paint customInitPaint(){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xff8e8e8e);
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(10);
        return paint;
    }
}
