package com.vivogaming.livecasino.screens.game;

import static com.vivogaming.livecasino.global.Variables.getCurrency;

/**
 * User: Vasja
 * Date: 08.11.13
 * Time: 15:47
 */
public abstract class NotificationDataWorker {

    public static final String getCurrencySymbolByName(String _currencyName) {
        if (_currencyName == null) return "";

        if (_currencyName.equalsIgnoreCase("usd")) {
            _currencyName = "\u0024"; //dollar
        } else if (_currencyName.equalsIgnoreCase("eur")) {
            _currencyName = "\u20A0"; //euro
        }
        return _currencyName;
    }

    public static final String getWinnerString(final String _resultSum, final String _amount) {
        String winner = "";

        if (_amount.equals("0")) { //you lose
            if (_resultSum.equals("1")) {
                winner = "Winner: Player";
            } else if (_resultSum.equals("2")) {
                winner = "Winner: Banker";
            } else if (_resultSum.equals("3")) {
                winner = "Winner: Tie";
            }
        } else {    //you won
            final String currencySymbol = getCurrencySymbolByName(getCurrency());
            winner = currencySymbol + _amount;
        }

        return winner;
    }
}
