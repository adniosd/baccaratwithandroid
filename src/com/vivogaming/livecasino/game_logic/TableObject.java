package com.vivogaming.livecasino.game_logic;

import java.io.Serializable;

public final class TableObject implements Serializable {
    public String tableId, limitName, limitId, limitMin, limitMax, dealerName, tableStatus, resultHistory, dealerImage;

    private TableObject(final String _tableId, final String _limitName, final String _limitId, final String _limitMin,
                       final String _limitMax, final String _dealerName, final String _tableStatus,
                       final String _resultHistory, final String _dealerImage) {
        tableId = _tableId;
        limitName = _limitName;
        limitId = _limitId;
        limitMin = _limitMin;
        limitMax = _limitMax;
        dealerName = _dealerName;
        tableStatus = _tableStatus;
        resultHistory = _resultHistory;
        dealerImage = _dealerImage;
    }

    /**
     * factory method
     * @param _tableId
     * @param _limitName
     * @param _limitId
     * @param _limitMin
     * @param _limitMax
     * @param _dealerName
     * @param _tableStatus
     * @param _resultHistory
     * @param _dealerImage
     * @return
     */
    public static final TableObject create(final String _tableId, final String _limitName, final String _limitId, final String _limitMin,
                                    final String _limitMax, final String _dealerName, final String _tableStatus,
                                    final String _resultHistory, final String _dealerImage) {
        return new TableObject(_tableId, _limitName, _limitId, _limitMin, _limitMax, _dealerName,
                _tableStatus, _resultHistory, _dealerImage);
    }
}