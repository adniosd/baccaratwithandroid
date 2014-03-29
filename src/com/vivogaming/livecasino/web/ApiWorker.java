package com.vivogaming.livecasino.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.vivogaming.livecasino.game_logic.TableObject;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.vivogaming.livecasino.global.BitmapWorker.bitmapToBase64;
import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.global.ErrorHandler.createErrorMapWithStatusCode;
import static com.vivogaming.livecasino.web.Parser.*;
import static com.vivogaming.livecasino.web.UriBuilder.buildURIfromString;

public abstract class ApiWorker {
    /**
     * create http client with timeouts
     * @return
     */
    private static final DefaultHttpClient prepareHttpClient() {
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);           //30 sec timeout
        HttpConnectionParams.setSoTimeout(httpParams, 15000);
        return new DefaultHttpClient(httpParams);
    }

    /**
     * get String from http server response
     * @param _response
     * @return
     * @throws IOException
     */
    private static final String getStringFromResponse(final HttpResponse _response) throws IOException {
//        final BufferedReader reader = new BufferedReader(new InputStreamReader(_response.getEntity().getContent(), "UTF-8"));
//        final StringBuilder stringBuilder = new StringBuilder();
//
//        String line;
//        while ((line = reader.readLine()) != null) {
//            stringBuilder.append((line + "\n"));
//        }
//
//        return stringBuilder.toString();

        final String result = EntityUtils.toString(_response.getEntity());
        return result;
    }

    /**
     * login request
     * can return two objects:
     * if ok - HashMap<String, String> - map with parsed login data
     * otherwise - HashMap<String, Object> - error map
     * @param _login
     * @param _pass
     * @param _operatorKey
     * @param _ipStr
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static final HashMap<String, Object> apiLogin(final String _login, final String _pass, final String _operatorKey,
                                      final String _ipStr) throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";
        final String url = API_LOGIN +
                LOGIN_NAME      + "=" + _login          + "&" +
                PLAYER_PASSWORD + "=" + _pass           + "&" +
                OPERATOR_ID     + "=" + _operatorKey    + "&" +
                PLAYER_IP       + "=" + _ipStr;
        final URI uri = buildURIfromString(url);

        final HttpClient client = prepareHttpClient();
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
            Log.i("result", "apiLogin : " + responseStr);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        if (!responseStr.isEmpty() && !responseStr.contains(ERROR)) {
            final HashMap<String, String> loginMap = parseLogin(responseStr);
            resultMap.put(API_LOGIN, loginMap);
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }

    //don't work
    public static final HashMap<String, Object> apiRegister(final String _userName, final String _pass, final String _operatorKey,
                                                         final String _firstName, final String _lastName, final String _currency, final String _email) throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";

        final String url = API_REGISTER +
                OPERATOR_KEY      + "=" +   _operatorKey      + "&" +
                USER_NAME         + "=" +   _userName         + "&" +
                USER_PASSWORD     + "=" +   _pass             + "&" +
                FIRST_NAME        + "=" +   _firstName        + "&" +
                LAST_NAME         + "=" +   _lastName         + "&" +
                CURRENCY          + "=" +   _currency         + "&" +
                EMAIL             + "=" +   _email;
        final URI uri = buildURIfromString(url);

        final DefaultHttpClient client = prepareHttpClient();

        final CredentialsProvider credsProvider = client.getCredentialsProvider();
        credsProvider.setCredentials(new AuthScope("207.232.7.175", 8080), new UsernamePasswordCredentials("dima1", "jojo1974"));
        client.setCredentialsProvider(credsProvider);
        final HttpHost httpproxy = new HttpHost("207.232.7.175", 8080);
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpproxy);

        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        if (!responseStr.isEmpty() && !responseStr.contains(ERROR)) {
            final HashMap<String, String> registerMap = parseRegister(responseStr);
            if(registerMap.containsKey(FAIL)) {
                resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, registerMap.get(FAIL)));
                return resultMap;
            } else {
                resultMap.put(API_REGISTER, registerMap);
            }
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }

    /**
     * request for active tables data
     * can return two objects:
     * if ok - ArrayList<TableObject> - list with parsed table data
     * otherwise - HashMap<String, Object> - error map
     * @param _operatorId
     * @param _playerCurrency
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static final HashMap<String, Object> apiGetActiveTables(final String _operatorId, final String _playerCurrency)
            throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";
        final String url = API_GET_ACTIVE_TABLES +
                GAME_NAME       + "=" + BACCARAT        + "&" +
                OPERATOR_ID     + "=" + _operatorId     + "&" +
                PLAYER_CURRENCY + "=" + _playerCurrency;
        final URI uri = buildURIfromString(url);

        final HttpClient client = prepareHttpClient();
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
            Log.i("result", "apiGetActiveTables : " + responseStr);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        if (!responseStr.isEmpty() && !responseStr.contains(ERROR)) {
            final ArrayList<TableObject> tableList = parseGetActiveTables(responseStr);
            resultMap.put(API_GET_ACTIVE_TABLES, tableList);
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }

    /**
     * send request for dealer image
     * @param _dealerName name from response
     * @return bitmap in base64 string
     */
    public static final String requestDealerImage(final String _dealerName) throws IOException {
        final URL imageUrl = new URL("http://1vivo.com/flash/images/dealers/" + _dealerName.replaceAll(" ", "%20") + ".jpeg");
        final Bitmap dealerImage = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        return bitmapToBase64(dealerImage);
    }

    /**
     * init request
     * can return two objects:
     * if ok - ArrayList<TableObject> - list with parsed table data
     * otherwise - HashMap<String, Object> - error map
     * @param _tableId
     * @param _playerToken
     * @param _operatorId
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static final HashMap<String, Object> apiInit(final String _tableId, final String _playerToken,
                                                        final String _operatorId) throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";
        final String url = API_INIT +
                TABLE_ID        + "=" + _tableId        + "&" +
                PLAYER_TOKEN    + "=" + _playerToken    + "&" +
                OPERATOR_ID     + "=" + _operatorId;
        final URI uri = buildURIfromString(url);

        final HttpClient client = prepareHttpClient();
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        if (!responseStr.isEmpty() && !responseStr.contains(ERROR)) {
            final HashMap<String, String> initMap = parseInit(responseStr);
            resultMap.put(API_INIT, initMap);
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }

    public static final HashMap<String, Object> apiGetStatus(final String _tableId, final String _playerToken,
            final String _operatorId, final String _initId, final String _tableTrnId) throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";
        final String url = API_GET_STATUS +
                TABLE_ID        + "=" + _tableId        + "&" +
                PLAYER_TOKEN    + "=" + _playerToken    + "&" +
                OPERATOR_ID     + "=" + _operatorId     + "&" +
                INIT_ID         + "=" + _initId         +
                    (_tableTrnId == null || _tableTrnId.isEmpty() ? "" :
                    "&" + TABLE_TRN_ID + "=" + _tableTrnId);
        final URI uri = buildURIfromString(url);

        final HttpClient client = prepareHttpClient();
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        if (!responseStr.isEmpty() && !responseStr.contains(ERROR)) {
            final HashMap<String, String> statusMap = parseGetStatus(responseStr);
            resultMap.put(API_GET_STATUS, statusMap);
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }

    public static final HashMap<String, Object> apiNewBets(final String _tableId, final String _playerToken,
            final String _operatorId, final String _initId, final String _newBets, final String _guid,
            final String _hash) throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";
        final String url = API_NEW_BETS +
                TABLE_ID        + "=" + _tableId        + "&" +
                PLAYER_TOKEN    + "=" + _playerToken    + "&" +
                OPERATOR_ID     + "=" + _operatorId     + "&" +
                INIT_ID         + "=" + _initId         + "&" +
                NEW_BETS        + "=" + _newBets        + "&" +
                GUID            + "=" + _guid           + "&" +
                HASH            + "=" + _hash;
        final URI uri = buildURIfromString(url);

        final HttpClient client = prepareHttpClient();
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        //no need to check for "error" here, because it's not critical, game must continue working
        if (!responseStr.isEmpty()) { //&& !responseStr.contains(ERROR)
            final HashMap<String, String> statusMap = parseNewBets(responseStr);
            resultMap.put(API_NEW_BETS, statusMap);
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }

    public static final HashMap<String, Object> apiTips(final String _playerToken, final String _operatorId,
            final String _tableId, final String _initId, final String _cmd, final String _amount) throws IOException, URISyntaxException {
        final HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String responseStr = "";
        final String url = API_TIPS +
                PLAYER_TOKEN    + "=" + _playerToken    + "&" +
                OPERATOR_ID     + "=" + _operatorId     + "&" +
                TABLE_ID        + "=" + _tableId        + "&" +
                INIT_ID         + "=" + _initId         + "&" +
                CMD             + "=" + _cmd            + "&" +
                AMOUNT          + "=" + _amount;
        final URI uri = buildURIfromString(url);

        final HttpClient client = prepareHttpClient();
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = client.execute(get);

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            responseStr = getStringFromResponse(response);
        } else {
            resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
            return resultMap;
        }

        //no need to check for "error" here, because it's not critical, game must continue working
        if (!responseStr.isEmpty()) { //&& !responseStr.contains(ERROR)
            final HashMap<String, String> statusMap = parseTip(responseStr);
            resultMap.put(API_TIPS, statusMap);
            return resultMap;
        }

        resultMap.put(ERROR, createErrorMapWithStatusCode(statusCode, responseStr));
        return resultMap;
    }
}