package com.vivogaming.livecasino.game_logic;

public final class PlacingObject {
    //boxResId used to choose needed stack
    //(0 - value) will added to this stack
    public int boxResId;
    //value being subtracting from stack
    public int chipValue;

    public PlacingObject(final int _boxResId, final int _chipValue) {
        boxResId = _boxResId;
        chipValue = _chipValue;
    }
}
