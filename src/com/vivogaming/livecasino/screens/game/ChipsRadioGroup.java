package com.vivogaming.livecasino.screens.game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public final class ChipsRadioGroup extends TableLayout implements OnClickListener {

    private RadioButton activeRadioButton;

    public ChipsRadioGroup(final Context _context) {
        super(_context);
    }

    public ChipsRadioGroup(final Context _context, final AttributeSet _attrs) {
        super(_context, _attrs);
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public final void addView(final View _child, final int _index, final android.view.ViewGroup.LayoutParams _params) {
        super.addView(_child, _index, _params);
        setChildrenOnClickListener((TableRow) _child);
    }


    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public final void addView(final View _child, final android.view.ViewGroup.LayoutParams _params) {
        super.addView(_child, _params);
        setChildrenOnClickListener((TableRow) _child);
    }

    private final void setChildrenOnClickListener(final TableRow _tableRow) {
        final int childCount = _tableRow.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = _tableRow.getChildAt(i);
            if (view instanceof RadioButton) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * removes checked state of previous button
     * checks button
     * save checked button
     * @param _view
     */
    @Override
    public final void onClick(final View _view) {
        final RadioButton radioButton = (RadioButton) _view;
        if (activeRadioButton != null) {
            activeRadioButton.setChecked(false);
        }

        radioButton.setChecked(true);
        activeRadioButton = radioButton;
    }

    public final int getCheckedRadioButtonId() {
        if (activeRadioButton != null) {
            return activeRadioButton.getId();
        }

        return -1;
    }

    /**
     * set unchecked radio button and null
     */
    public final void removeCheck() {
        if (activeRadioButton != null) {
            activeRadioButton.setChecked(false);
            activeRadioButton = null;
        }
    }
}