package com.vivogaming.livecasino.screens.game.history;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import static com.vivogaming.livecasino.global.Constants.HORIZONTAL_COUNT_TO_CELL;
import static com.vivogaming.livecasino.global.Constants.VERTICAL_COUNT_TO_CELL;
import static com.vivogaming.livecasino.screens.game.history.DrawSmallAndBigCircles.buildList;
import static com.vivogaming.livecasino.screens.game.history.InitPaint.customInitFillColor;
import static com.vivogaming.livecasino.screens.game.history.InitPaint.customInitPaint;


/**
 * Custom XML View, that paint grid for table score with score elements
 * Created by Sofia on 10/2/13.
 */
public final class CustomViewHistory extends View {

    private static List<HashMap<String, String>> listHashMapsResult;
    private float cell;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint paint, fillColorAndText;
    private Resources resources;
    private static String resultHistory = "";


    public CustomViewHistory(Context _context,
                             AttributeSet _attributeSet) {
        super(_context, _attributeSet);
        resources = getResources();
        fillColorAndText = customInitFillColor();
        paint = customInitPaint();
    }

    public static final void setHistory(final String _history) {
        resultHistory = _history;    }

    /**
     * Draw all elements on canvas
     * Measure height of CustomViewScore
     *
     * @param _canvas
     */
    @Override
    protected void onDraw(Canvas _canvas) {
        invalidate();
        int width = getWidth();

        cell = width / HORIZONTAL_COUNT_TO_CELL;
        float tempSizeHigh = cell * VERTICAL_COUNT_TO_CELL;

        int height = (int) tempSizeHigh;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        DrawSmallAndBigCircles drawSmallAndBigCirclesHistory = new DrawSmallAndBigCircles(resources, paint, fillColorAndText, width, height, cell, mCanvas, HORIZONTAL_COUNT_TO_CELL, VERTICAL_COUNT_TO_CELL);
        if(resultHistory==null){
            return;
        }else{
            listHashMapsResult = buildList(resultHistory);
        }
        if (width != 0 && height != 0)
            drawSmallAndBigCirclesHistory.drawCustomGrid(listHashMapsResult);

        if (mBitmap != null) {
            _canvas.drawBitmap(mBitmap, 0, 0, fillColorAndText);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int kidsViewHeight = ((parentViewWidth) / HORIZONTAL_COUNT_TO_CELL) * VERTICAL_COUNT_TO_CELL;

        // ... take into account the parent's size as needed ...
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(parentViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(kidsViewHeight, MeasureSpec.EXACTLY));
    }
}
