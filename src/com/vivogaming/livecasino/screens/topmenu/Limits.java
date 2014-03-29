package com.vivogaming.livecasino.screens.topmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import com.vivogaming.livecasino.R;

public final class Limits extends Dialog implements View.OnClickListener {
    private TableLayout tableLimits_SML;
    private TextView playerMin,
                     playerMax,
                     dplayerMin,
                     dplayerMax,
                     tieMin,
                     tieMax,
                     bankerMin,
                     bankerMax,
                     dbankerMin,
                     dbankerMax;

    public Limits(final Context context) {
        super(context, R.style.MenuContentThemes);
    }

    @Override
    public final void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu_limits);
        final double width = getContext().getResources().getDimension(R.dimen.menu_content_width);
        final double height = getContext().getResources().getDisplayMetrics().heightPixels * 0.9;
        getWindow().setLayout((int) Math.round(width), (int) Math.round(height));

        tableLimits_SML     = (TableLayout) findViewById(R.id.tableLimits_SML);
        playerMin           = (TextView) findViewById(R.id.tvPlayerMin_SML);
        playerMax           = (TextView) findViewById(R.id.tvPlayerMax_SML);
        dplayerMin          = (TextView) findViewById(R.id.tvDPlayerMin_SML);
        dplayerMax          = (TextView) findViewById(R.id.tvDPlayerMax_SML);
        tieMin              = (TextView) findViewById(R.id.tvTieMin_SML);
        tieMax              = (TextView) findViewById(R.id.tvTieMax_SML);
        bankerMin           = (TextView) findViewById(R.id.tvBankerMin_SML);
        bankerMax           = (TextView) findViewById(R.id.tvBankerMax_SML);
        dbankerMin          = (TextView) findViewById(R.id.tvDBankerMin_SML);
        dbankerMax          = (TextView) findViewById(R.id.tvDBankerMax_SML);

        findViewById(R.id.btnCloseForLimits_SML).setOnClickListener(this);
    }

    public final void onClick(final View _view) {
        dismiss();
    }

    public final void setMinMaxPlayer(final int[] _limits){
        playerMin.setText("" + _limits[0]);
        playerMax.setText("" + _limits[1]);
    }

    public final void setMinMaxBanker(final int[] _limits){
        bankerMin.setText("" + _limits[0]);
        bankerMax.setText("" + _limits[1]);
    }

    public final void setMinMaxTie(final int[] _limits){
        tieMin.setText("" + _limits[0]);
        tieMax.setText("" + _limits[1]);
    }

    public void setMinMaxDPlayer(final int[] _limits){
        dplayerMin.setText("" + _limits[0]);
        dplayerMax.setText("" + _limits[1]);
    }

    public final void setMinMaxDBanker(final int[] _limits){
        dbankerMin.setText("" + _limits[0]);
        dbankerMax.setText("" + _limits[1]);
    }





}
