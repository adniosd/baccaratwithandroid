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

import static com.vivogaming.livecasino.global.Constants.*;
import static com.vivogaming.livecasino.screens.game.history.DrawSmallAndBigCircles.buildList;
import static com.vivogaming.livecasino.screens.game.history.InitPaint.customInitFillColor;
import static com.vivogaming.livecasino.screens.game.history.InitPaint.customInitPaint;



/**
 * Custom XML View, that paint grid for table score with score elements
 * Created by Sofia on 10/2/13.
 */
public final class CustomViewScore extends View {

    private static String resultHistory = "";
    private static List<HashMap<String, String>> listHashMapsResult;
    private float cell;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint paint, fillColorAndText;
    private Resources resources;

    public CustomViewScore(Context _context,
                           AttributeSet _attributeSet) {
        super(_context, _attributeSet);

        resources = getResources();
        fillColorAndText = customInitFillColor();
        paint = customInitPaint();
    }

    public static final void setHistory(final String _history) {
        resultHistory = _history;
    }

    /**
     * Draw all elements on canvas
     * Measure height of CustomViewScore
     *
     * @param _canvas
     */
    @Override
    protected void onDraw(Canvas _canvas) {
        int widthLayout = getWidth();
        int width = (int) (widthLayout * WIDTH_LINER_SCORE);
        int heightLayout = getHeight();
        cell = width / HORIZONTAL_COUNT_OF_CELL;
        float tempSizeHigh = cell * VERTICAL_COUNT_OF_CELL;
        int height = (int) tempSizeHigh;
        if(width==0 || height==0) {
            return;
        }else {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        biggerSizeResize(heightLayout, tempSizeHigh, width);
        smallerSizeResize(heightLayout, tempSizeHigh, width);
        DrawSmallAndBigCircles drawSmallAndBigCircles = new DrawSmallAndBigCircles(resources, paint, fillColorAndText, width, height, cell, mCanvas, HORIZONTAL_COUNT_OF_CELL, VERTICAL_COUNT_OF_CELL);
        if(resultHistory==null){
            return;
        }else{
            listHashMapsResult = buildList(resultHistory);
        }
        if (width != 0 && height != 0)
            drawSmallAndBigCircles.drawCustomGrid(listHashMapsResult);

        //tt coordinates of start draw of CustomViewScore on X
        //hh coordinates of start draw of CustomViewScore on Y
        if (mBitmap != null) {
            float tt = ((widthLayout - width) / 3);
            float hh = ((heightLayout - height) / 2);
            _canvas.drawBitmap(mBitmap, tt, hh, fillColorAndText);
        }
        }
    }
    /**
     * if CustomGrid bigger in size than layout
     * @param _heightLayout height of layout
     * @param _tempSizeHigh height of view
     * @param _width width of view
     */
    public final void biggerSizeResize(int _heightLayout, float _tempSizeHigh, int _width){
        while (_heightLayout < _tempSizeHigh) {
            HORIZONTAL_COUNT_OF_CELL = HORIZONTAL_COUNT_OF_CELL + 1;
            cell = _width / HORIZONTAL_COUNT_OF_CELL;
            if(VERTICAL_COUNT_OF_CELL > 5) {
                VERTICAL_COUNT_OF_CELL = (VERTICAL_COUNT_OF_CELL - 1);
                _tempSizeHigh = cell * VERTICAL_COUNT_OF_CELL;
                invalidate();
            }
            else
                _tempSizeHigh = cell * VERTICAL_COUNT_OF_CELL;
        }
    }

    /**
     * if CustomGrid smaller in size than layout
     * @param _heightLayout height of layout
     * @param _tempSizeHigh height of view
     * @param _width width of view
     */
    public final void smallerSizeResize(int _heightLayout, float _tempSizeHigh, int _width){
        while(_heightLayout - _tempSizeHigh >= 2.5 * cell){
            HORIZONTAL_COUNT_OF_CELL = HORIZONTAL_COUNT_OF_CELL - 1;
            cell = _width / HORIZONTAL_COUNT_OF_CELL;
            if(VERTICAL_COUNT_OF_CELL < 8){
                VERTICAL_COUNT_OF_CELL = VERTICAL_COUNT_OF_CELL + 1;
                _tempSizeHigh = cell * VERTICAL_COUNT_OF_CELL;
                invalidate();
            }
            else
                _tempSizeHigh = cell * VERTICAL_COUNT_OF_CELL;
        }
    }
}
