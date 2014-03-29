package com.vivogaming.livecasino.global;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * User: Vasja
 * Date: 05.11.13
 * Time: 11:58
 */
public abstract class Encoder {
    /**
     * encode user's password as base64 string
     * @param _pass     user's pass in string
     * @return          user's pass encoded with base64
     */
    public static final String stringToBase64(final String _pass) {
        String result = "";
        try {
            result = new String(Base64.encode(_pass.getBytes(), Base64.NO_WRAP), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final String base64ToString(final String _encodedPass) {
        final String result = new String(Base64.decode(_encodedPass, Base64.NO_WRAP));
        return result;
    }
}
