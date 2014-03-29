package com.vivogaming.livecasino.game_logic;

import com.vivogaming.livecasino.R;

public abstract class ChipData {

    /**
     * return appropriate image by selected chip
     * @param _chipResId
     * @return
     */
    public static final int getImageIdByChipValue(final int _chipResId) {
        switch (_chipResId) {
            case 1:
                return R.drawable.chip_1;

            case 5:
                return R.drawable.chip_5;

            case 10:
                return R.drawable.chip_10;

            case 25:
                return R.drawable.chip_25;

            case 100:
                return R.drawable.chip_100;

            case 500:
                return R.drawable.chip_500;

            case 1000:
                return R.drawable.chip_1000;

            default:
            	
            	/**
            	 * developer Sam
            	 * 2014 年 3月29日
            	 * if the chips that client places in a tray more than 6 chips
            	 * need to replace with a "magic chip"
            	 */
            	return R.drawable.chip_magic;
        }
    }

    /**
     * returns chip value by chip view resource id
     * @param _checkedChipId
     * @return
     */
    public static final int getChipValueByResId(final int _checkedChipId) {
        switch (_checkedChipId) {
            case R.id.rbChip1_SG:
                return 1;

            case R.id.rbChip5_SG:
                return 5;

            case R.id.rbChip10_SG:
                return 10;

            case R.id.rbChip25_SG:
                return 25;

            case R.id.rbChip100_SG:
                return 100;

            case R.id.rbChip500_SG:
                return 500;

            case R.id.rbChip1000_SG:
                return 1000;

            default:
                return 0;
        }
    }

    /**
     * returns tip chip value by chip view resource id
     * @param _tipChipId
     * @return
     */
    public static final int getTipChipValueByResId(final int _tipChipId) {
        switch (_tipChipId) {
            case R.id.btnTipChip1_SG:
                return 1;

            case R.id.btnTipChip5_SG:
                return 5;

            case R.id.btnTipChip10_SG:
                return 10;

            case R.id.btnTipChip25_SG:
                return 25;

            default:
                return 0;
        }
    }

    /**
     * returns chip image view id by chip value
     * @param _chipValue
     * @return
     */
    public static final int getChipViewResIdByValue(final int _chipValue) {
        switch (_chipValue) {
            case 1:
                return R.id.rbChip1_SG;

            case 5:
                return R.id.rbChip5_SG;

            case 10:
                return R.id.rbChip10_SG;

            case 25:
                return R.id.rbChip25_SG;

            case 100:
                return R.id.rbChip100_SG;

            case 500:
                return R.id.rbChip500_SG;

            case 1000:
                return R.id.rbChip1000_SG;

            default:
                return 0;
        }
    }
}