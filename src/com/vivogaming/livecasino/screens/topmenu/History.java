package com.vivogaming.livecasino.screens.topmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.game.history.CustomViewHistory;

import static com.vivogaming.livecasino.screens.game.ViewWorker.drawGamePlayerHistory;


/**
 * Created with IntelliJ IDEA.
 * User: Assasin
 * Date: 10.10.13
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class History extends Dialog implements View.OnClickListener {
    private static CustomViewHistory customViewHistory;
    String resultGamePlayerHistory="";

    public History(final Context context, final String _resultGamePlayerHistory)
    {
        // Set your theme here
        super(context, R.style.MenuContentThemes);
        resultGamePlayerHistory = _resultGamePlayerHistory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.dialog_menu_history);
        final double width = getContext().getResources().getDimension(R.dimen.menu_content_width);
        final double height = getContext().getResources().getDisplayMetrics().heightPixels * 0.9;
        getWindow().setLayout((int) Math.round(width), (int) Math.round(height));
        customViewHistory = (CustomViewHistory)findViewById(R.id.cvhGameHistory);

        drawGamePlayerHistory(customViewHistory,resultGamePlayerHistory);
        findViewById(R.id.btnCancelForHistory_SMH).setOnClickListener(this);
    }


    public void onClick(View v) {
        dismiss();
    }

}
