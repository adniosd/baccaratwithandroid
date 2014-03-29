package com.vivogaming.livecasino.game_logic;

import java.util.ArrayList;

import static com.vivogaming.livecasino.global.Constants.USED_CHIP_VALUES;

public abstract class ChipStackWorker {
    /**
     * adds chip to list
     * list selected by box id
     */
    public static final void addChipToStack(ArrayList<ChipObject> _stack, final int _chipValue) {
        _stack.add(new ChipObject(null, _chipValue));
    }

    /**
     * regroup chips in list to use minimum chips
     * @param _listChips
     */
    public static final void groupChips(final ArrayList<ChipObject> _listChips) {
        int listSum = getStackChipsSum(_listChips);
        _listChips.clear();

        while (listSum != 0) {
            int remainder = 0;
            int chipPos = USED_CHIP_VALUES.length - 1;
            while ((remainder = listSum % USED_CHIP_VALUES[chipPos]) == listSum) {
                chipPos--;
            }
            final int usedChipValue = USED_CHIP_VALUES[chipPos];
            listSum -= usedChipValue;

            _listChips.add(new ChipObject(null, usedChipValue));
        }
    }

    /**
     * get sum of all chips in list
     * @param _listChips
     * @return
     */
    public static final int getStackChipsSum(final ArrayList<ChipObject> _listChips) {
        int sum = 0;
        for (int i = 0; i < _listChips.size(); i++)
            sum += _listChips.get(i).value;

        return sum;
    }
}
