package com.vivogaming.livecasino.global;

import java.util.HashMap;

import static com.vivogaming.livecasino.global.Constants.ERROR_DESCRIPTION;
import static com.vivogaming.livecasino.global.Constants.STATUS_CODE;

/**
 * User: Vasja
 * Date: 04.11.13
 * Time: 9:29
 */
public abstract class ErrorHandler {

    /**
     * returns map with error description as string
     * @param _statusCode
     * @return
     */
    public static final HashMap<String, String> createErrorMapWithStatusCode(final int _statusCode, final String _description) {
        final HashMap<String, String> errorMap = new HashMap<String, String>();
        errorMap.put(STATUS_CODE, "" + _statusCode);
        errorMap.put(ERROR_DESCRIPTION, _description);
        return errorMap;
    }
}
