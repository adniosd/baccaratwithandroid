package com.vivogaming.livecasino.web;

import android.util.Log;
import com.vivogaming.livecasino.game_logic.TableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.web.ApiWorker.requestDealerImage;

public abstract class Parser {

    /**
     * divide login response to key-value pairs and put them to hashmap
     * data format "key=value" (usually)
     * @param _responseStr
     * @return
     */
    public static final HashMap<String, String> parseLogin(String _responseStr) {
        final HashMap<String, String> loginDataMap = new HashMap<String, String>();

        if (_responseStr.isEmpty()) return loginDataMap;

        //delete "{" and "}" symbols
        _responseStr = _responseStr.substring(1, _responseStr.length() - 2);

        //split by ","
        final String[] pairs = _responseStr.split(",");

        /*  for each token
            split by "="
            put in map key and value
         */
        for (final String pair : pairs) {
            final String[] field = pair.split("=");        //get key and value

            String key = "";
            String value = "";
            if (field.length == 2) {
                key = field[0];
                value = field[1];

            } else if (field.length == 1) {
                key = field[0];
                value = "";
            }

            loginDataMap.put(key, value);
        }

        return loginDataMap;
    }

    public static final ArrayList<TableObject> parseGetActiveTables(final String _responseStr) throws IOException {
        final ArrayList<TableObject> tableList = new ArrayList<TableObject>();
        if (_responseStr.isEmpty()) return tableList;

        //split all table's strings with data
        final String[] tableStrings = _responseStr.split("\r");

        //parse each string and add object with obtained values
        int newLineIndex;
        for (int i = 0; i < tableStrings.length; i++) {
            //delete [NEW_LINE] ending
            if ((newLineIndex = tableStrings[i].indexOf(NEW_LINE)) != -1)
                tableStrings[i] = tableStrings[i].substring(0, newLineIndex);

            /* split by ",ResaultHistory="
                [0] = main data
                [1] = result history string */
            final String[] twoParts = tableStrings[i].split("," + RESULT_HISTORY + "=");

            //split main data string by ","
            final String[] pairsMainData = twoParts[0].split(",");

            /* for each token
                split by "="
                put in map key and value */
            final HashMap<String, String> dataMap = new HashMap<String, String>();
            for (final String pair : pairsMainData) {
                final String[] field = pair.split("=");        //get key and value

                final String key = field[0];
                final String value = field[1];

                dataMap.put(key, value);
            }

            //add to object
            final String tableId        = dataMap.get(TABLE_ID);
            final String limitName      = dataMap.get(LIMIT_NAME);
            final String limitId        = dataMap.get(LIMIT_ID);
            final String limitMin       = dataMap.get(LIMIT_MIN);
            final String limitMax       = dataMap.get(LIMIT_MAX);
            final String dealerName     = dataMap.get(DEALER_NAME);
            final String tableStatus    = dataMap.get(TABLE_STATUS);
            final String resultHistory  = twoParts[1];

            if (tableStatus.equals(OPEN)) {
                //sometimes there is no dealer images on server
                String bitmapStr = "";
                try {
                    bitmapStr = requestDealerImage(dealerName);
                } catch (final Exception _e) {
                    _e.printStackTrace();
//                    printErrorLogToFile(_e, "Request Dealer Image");
                } finally {
                    tableList.add(TableObject.create(tableId, limitName, limitId, limitMin, limitMax,
                            dealerName, tableStatus, resultHistory, bitmapStr));
                }
            }
        }

        return tableList;
    }

    public static final HashMap<String, String> parseInit(String _responseStr) {
        final HashMap<String, String> resultMap = new HashMap<String, String>();

        //delete all "}"
        //due to empty block "{}" need replace "{{" to "{"
        //than replace first "{" to ""
        _responseStr = _responseStr.replaceAll("\\}", "");
        _responseStr = _responseStr.replaceAll("\\{\\{", "{");
        _responseStr = _responseStr.replaceFirst("\\{", "");

        final String[] blocks = _responseStr.split("\\{");

        /*
            parse zero block
            table status and window id
         */
        final String[] zeroBlock = blocks[0].split(",");

        //check is table open
        //if not - return error map
        final String tableStatus = zeroBlock[0];
        if (!tableStatus.equals(OPEN)) {
            resultMap.put(ERROR, "The table is closed");
            return resultMap;
        }

        //save window id
        //[0] WINDOW_ID
        //[1] value
        final String[] windowIdPair = zeroBlock[2].split(":");
        resultMap.put(windowIdPair[0], windowIdPair[1]);

        /*
            parse first block
            game limits
         */
        final String[] firstBlock = blocks[1].split(",");

        final String[] boxPlayer = firstBlock[0].split("\\\\");
        final String[] boxBanker = firstBlock[1].split("\\\\");
        final String[] boxTie = firstBlock[2].split("\\\\");
        final String[] tableLimits = firstBlock[3].split("\\\\");
        final String[] boxPlayerPair = firstBlock[4].split("\\\\");
        final String[] boxBankerPair = firstBlock[5].split("\\\\");

        resultMap.put(PLAYER_BOX_NUM, boxPlayer[1]);
        resultMap.put(BANKER_BOX_NUM, boxBanker[1]);
        resultMap.put(TIE_BOX_NUM, boxTie[1]);
        resultMap.put(PLAYER_PAIR_BOX_NUM, boxPlayerPair[1]);
        resultMap.put(BANKER_PAIR_BOX_NUM, boxBankerPair[1]);
        resultMap.put(TABLE_LIMITS, tableLimits[1]);

        //[0] *_BOX_NUM
        //[1] value

        /*
            parse second block
            Chip Values
            not used
         */
        final String[] secondBlock = blocks[2].split(",");

        /*
            parse third block
            live video url
         */
        final String liveVideoUrl = blocks[3];
        resultMap.put(LIVE_VIDEO_URL, liveVideoUrl);

        /*
            parse fourth block
            player currency symbol
         */
        final String fourthBlock = blocks[4];
        final String playerCurrency = fourthBlock;

        /*
            parse fifth block
            Informative Limit Range
         */
        final String fifthBlock = blocks[5];
        //not used. table limits is in the 4 place of limits list
        final String[] tableLimitsPair = fifthBlock.split("\\\\");

        /*
            parse sixth block
            Chat Block
         */
        final String[] sixthBlock = blocks[6].split(",");
        final String chatUrl = sixthBlock[0];

        /*
            parse seventh block
            Balance Engine
         */
        final String seventhBlock = blocks[7];
        final String balanceEngineUrl = seventhBlock;

        /*
            parse eighthBlock
            Flags
         */
        final String[] eighthBlock = blocks[8].split(",");
        final String buyChipsOn = eighthBlock[1];
        if (buyChipsOn.equals(BUY_CHIPS_ON)) {
            final String buyChipUrl = eighthBlock[2];
            resultMap.put(BUY_CHIPS_ON, buyChipUrl);
        }

        return resultMap;
    }

    public static final HashMap<String, String> parseGetStatus(String _responseStr) {
        final HashMap<String, String> resultMap = new HashMap<String, String>();

        //delete all "}"
        //due to empty block "{}" need replace "{{" to "{"
        //due to second empty block "{}" need replace "{{" to "{"
        //than replace first "{" to ""
        _responseStr = _responseStr.replaceAll("\\}", "");
        _responseStr = _responseStr.replaceAll("\\{\\{", "{");
        _responseStr = _responseStr.replaceAll("\\{\\{", "{");
        _responseStr = _responseStr.replaceFirst("\\{", "");

        final String[] blocks = _responseStr.split("\\{");

        /*
            parse zero block
            GameStat, TableTrnID, History etc.
         */
        final String[] zeroBlock = blocks[0].split("&");
        final String[] gameStat         = zeroBlock[0].split("=");
        final String[] tableTrnId       = zeroBlock[1].split("=");
        final String[] resultSum        = zeroBlock[2].split("=");
        final String[] resultHistory    = zeroBlock[3].split("=");
        final String[] lastResultCard   = zeroBlock[4].split("=");
        final String[] dealerName       = zeroBlock[5].split("=");
        final String[] tableStatus      = zeroBlock[6].split("=");
        final String[] requestToClose   = zeroBlock[7].split("=");
        final String[] timeToBet        = zeroBlock[8].split("=");
        final String[] nextCall         = zeroBlock[9].split("=");
        final String[] playerBalance    = zeroBlock[10].split("=");

        resultMap.put(tableTrnId[0], tableTrnId[1]);
        resultMap.put(resultHistory[0], resultHistory[1]);
        resultMap.put(timeToBet[0], timeToBet[1]);
        resultMap.put(nextCall[0], nextCall[1]);
        resultMap.put(playerBalance[0], playerBalance[1]);
        resultMap.put(resultSum[0], resultSum[1]);

        /*
            parse first block
            total bets and wins
         */
        final String[] firstBlock = blocks[1].split(",");
        //[0] - key, [1] - value
        final String[] totalBets = firstBlock[0].split("=");
        final String[] totalWins = firstBlock[1].split("=");
        resultMap.put(totalBets[0], totalBets[1]);
        resultMap.put(totalWins[0], totalWins[1]);

        return resultMap;
    }

    /**
     * parsing error string
     * 101 - login, 2 strings
     * 103 - getStatus, 3 strings
     * @param _errorDescription
     * @return if status code != 200 return map with key "error" and value "description"
     */
    public static final HashMap<String, String> parseNextCallError(final String _errorDescription) {
        Log.d(TAG_ERROR, "errorStr = " + _errorDescription);
        final HashMap<String, String> errorMap = new HashMap<String, String>();

        //error 103,description=no new status was reported,NextCall=4
        final String[] descriptionArr = _errorDescription.split(",");

        /*
            parse zero block
            error number
         */
//        final String[] errorNumberPair = descriptionArr[0].split(" ");
//        final String errorNumber = errorNumberPair[1];
//        errorMap.put(ERROR_NUM, errorNumber);

        final String[] descriptionPair = descriptionArr[1].split("=");
        errorMap.put(ERROR_DESCRIPTION, descriptionPair[1]);

        final String[] nextCallPair = descriptionArr[2].split("=");
        errorMap.put(NEXT_CALL, nextCallPair[1]);


        return errorMap;
    }

    /**
     * parsing new best api response
     * can contains success or fail
     * if fail then show dialog with description to user
     * @param _responseStr
     * @return
     */
    public static final HashMap<String, String> parseNewBets(final String _responseStr) {
        final HashMap<String, String> resultMap = new HashMap<String, String>();

        final String[] blocks = _responseStr.split(",");

        //success or fail
        final String betResponse = blocks[0];

        //"SUCCESS,newbalance=100.00"
        if (betResponse.equals(SUCCESS)) {
            final String[] newBalancePair = blocks[1].split("=");
            resultMap.put(newBalancePair[0], newBalancePair[1]);
            return resultMap;

        //"FAIL,error 108,description=No Proper Bets Reported"
        } else if (betResponse.equals(FAIL)) {
            final String[] errorPair = blocks[1].split(" ");
            final String[] descriptionPair = blocks[2].split("=");

            resultMap.put(FAIL, "");
//            resultMap.put(descriptionPair[0], descriptionPair[1]);
            resultMap.put(descriptionPair[0], _responseStr);
        }

        return resultMap;
    }

    public static final HashMap<String, String> parseTip(final String _responseStr) {
        final HashMap<String, String> resultMap = new HashMap<String, String>();
        final String[] blocks = _responseStr.split(",");

        //success or fail
        final String betResponse = blocks[0];

        //"SUCCESS,newbalance=432.2"
        if (betResponse.equals(SUCCESS)) {
            final String[] newBalancePair = blocks[1].split("=");
            resultMap.put(newBalancePair[0], newBalancePair[1]);
            return resultMap;

            //"FAIL,error 108,description=No Proper Tip Reported"
        } else if (betResponse.equals(FAIL)) {
            final String[] errorPair = blocks[1].split(" ");
            final String[] descriptionPair = blocks[2].split("=");

            resultMap.put(FAIL, "");
//            resultMap.put(descriptionPair[0], descriptionPair[1]);
            resultMap.put(descriptionPair[0], _responseStr);
        }

        return resultMap;
    }

    public static final HashMap<String, String> parseRegister(final String _responseStr) {
        final HashMap<String, String> resultMap = new HashMap<String, String>();

        String text = _responseStr;
        Pattern pattern = Pattern.compile("(?<=<STATUS>).*.(?=</STATUS>)");
        Matcher matcher = pattern.matcher(text);

        String result = null;
        while (matcher.find()) {
            result = matcher.group().toString();
        }
           if(result.equals("S")||result.equals(SUCCESS)){
               Pattern pattern1 = Pattern.compile("(?<=<TransactionID>).*.(?=</TransactionID>)");
               Matcher matcher1 = pattern1.matcher(text);
               String result1;
               while (matcher1.find()) {
                   result1 = matcher1.group().toString();
                   resultMap.put(SUCCESS, result1);
               }
           } else {
               Pattern pattern2 = Pattern.compile("(?<=<DESC>).*.(?=</DESC>)");
               Matcher matcher2 = pattern2.matcher(text);
               String result2;

               while (matcher2.find()) {
                   result2 = matcher2.group().toString();
                   resultMap.put(FAIL, result2);
               }
           }
        return resultMap;
        }
}