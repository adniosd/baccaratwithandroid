package com.vivogaming.livecasino.global;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public abstract class BitmapWorker {

    /**
     * convert bitmap to base64 string
     * @param _bitmap
     * @return
     */
    public static final String bitmapToBase64(final Bitmap _bitmap) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    /**
     * convert base64 string to bitmap
     * @param _bitmapInBase64
     * @return
     */
    public static final Bitmap base64ToBitmap(final String _bitmapInBase64) {
        final byte[] decodedByte = Base64.decode(_bitmapInBase64, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
