package com.vivogaming.livecasino.screens.choose_table;


import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.game_logic.TableObject;
import com.vivogaming.livecasino.screens.game.history.CustomViewScore;
import com.vivogaming.livecasino.web.Request;

import java.util.ArrayList;

import static com.vivogaming.livecasino.global.BitmapWorker.base64ToBitmap;
import static com.vivogaming.livecasino.global.Constants.API_INIT;
import static com.vivogaming.livecasino.global.Variables.*;
import static com.vivogaming.livecasino.web.RequestPool.asyncTask;

public final class TableLobbyAdapter extends PagerAdapter implements View.OnClickListener {
    private ArrayList<TableObject> pages;
    private Activity activity;

    public TableLobbyAdapter(final Activity _activity, final ArrayList<TableObject> _pages) {
        pages = _pages;
        activity = _activity;
    }

    @Override
    public Object instantiateItem(View collection, int position) {

        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View viewPage = inflater.inflate(R.layout.page_choose_table, null);

        final ImageView ivDealerImage     = (ImageView) viewPage.findViewById(R.id.ivDealerImage_PCT);
        final TextView tvDealerNamel      = (TextView) viewPage.findViewById(R.id.tvDealerNamel_PCT);
        final TextView tvGameName         = (TextView) viewPage.findViewById(R.id.tvGameName_PCT);
        final TextView tvLimitName        = (TextView) viewPage.findViewById(R.id.tvLimitName_PCT);
        final TextView tvLimits           = (TextView) viewPage.findViewById(R.id.tvLimits_PCT);
        final ImageButton btnPlay         = (ImageButton) viewPage.findViewById(R.id.btnPlay_PCT);
        final CustomViewScore cvsHistory_PCT = (CustomViewScore) viewPage.findViewById(R.id.cvsHistory_PCT);

        final TextView tvTableId_PCT      = (TextView) viewPage.findViewById(R.id.tvTableId_PCT);

       ((ViewPager) collection).addView(viewPage);

        ivDealerImage.setImageBitmap(base64ToBitmap(pages.get(position).dealerImage));
        tvDealerNamel.setText(pages.get(position).dealerName);
        tvGameName.setText(activity.getString(R.string.app_name));
        tvLimitName.setText(pages.get(position).limitName);
        tvLimits.setText(" " + pages.get(position).limitMin + "-" + pages.get(position).limitMax);
        cvsHistory_PCT.setHistory(pages.get(position).resultHistory);

        tvTableId_PCT.setText("id: " + pages.get(position).tableId);

        btnPlay.setTag(position);
        btnPlay.setOnClickListener(this);

        return viewPage;
    }

    @Override
    public final void onClick(final View _v) {
        final int position = (Integer) _v.getTag();

        final TableObject tableObject = pages.get(position);

        final String tableId       = tableObject.tableId;
        final String playerToken   = getPlayerToken();
        final String operatorId    = getOperatorId();

        setTableId(tableId);

        asyncTask = new Request(activity, API_INIT);
        asyncTask.execute(tableId, playerToken, operatorId);
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }
}