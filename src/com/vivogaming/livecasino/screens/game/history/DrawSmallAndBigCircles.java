package com.vivogaming.livecasino.screens.game.history;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.vivogaming.livecasino.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.vivogaming.livecasino.global.Constants.*;

/**
 * Created by Sofia on 10/23/13.
 */
public final class DrawSmallAndBigCircles {

    private Resources resources;
    private Paint paint;
    private Paint fillColorAndText;
    private Canvas mCanvas;
    private float radiusCircle;
    private float cell;
    private float canvasSizeWidth;
    private float canvasSizeHigh;
    private static int horizontal_cell, vertical_cell;


    public DrawSmallAndBigCircles(final Resources _resources, Paint _paint, Paint _fillColorAndText, float _canvasSizeWidth,
                                  float _canvasSizeHigh, float _cell, Canvas _mCanvas, int _horizontal_cell, int _vertical_cell){
        resources = _resources;
        paint = _paint;
        fillColorAndText = _fillColorAndText;
        canvasSizeHigh = _canvasSizeHigh;
        canvasSizeWidth = _canvasSizeWidth;
        cell = _cell;
        mCanvas = _mCanvas;
        horizontal_cell = _horizontal_cell;
        vertical_cell = _vertical_cell;

        radiusCircle = (cell / 2) * PERCENT;
    }

    public final void drawBigCircles(int _cellX,
                                     int _cellY,
                                     int _colorOne,
                                     String _text,
                                     int _colorTwo,
                                     boolean _miniCircle,
                                     int _gravityC
                                    ) {

        //draw circle with such coordinate
        float tempX = (canvasSizeWidth / horizontal_cell) / 2;
        float tempY = (canvasSizeHigh / vertical_cell) / 2;
        float tempX1 = (float) _cellX * canvasSizeWidth / horizontal_cell;
        float tempY1 = (float) _cellY * canvasSizeHigh / vertical_cell;
        float x0 = (tempX1 + tempX);
        float y0 = (tempY1 + tempY);

        paint.setColor(_colorOne);
        mCanvas.drawCircle(x0, y0, radiusCircle, paint);
        fillColorAndText.setColor(_colorTwo);
        mCanvas.drawCircle(x0, y0, radiusCircle - 1, fillColorAndText);

        // draw text with such coordinate
        float x1 = (((float) _cellX * canvasSizeWidth / horizontal_cell) + tempX);
        float y1 = (((float) _cellY * canvasSizeHigh / vertical_cell) + tempY);
        float x2 = x1 - (radiusCircle / 2);
        float y2 = y1 + (radiusCircle / 2);

        fillColorAndText.setColor(resources.getColor(R.color.black));
        int textSize = (int) (radiusCircle * TEXT_SIZE_IN_CIRCLE) + 2;
        fillColorAndText.setTextSize(textSize);
        mCanvas.drawText(_text, x2, y2, fillColorAndText);

        //draw miniCircle with such coordinate and gravity
        if (_miniCircle) {
            drawSmallCircle(tempX1, tempY1, cell, radiusCircle, _gravityC);
        }
    }

    /**
     * Draw miniCircle of the top or of the bottom of circle when it necessarily
     * @param _X0          the highest point of Cell for X-axis
     * @param _Y0          the highest point of Cell for Y-axis
     * @param _squareWidth width of Cell
     * @param _radius      of circle
     * @param _gravityC    0 if no miniCircle , 1 - on top, 2 - on bottom
     *                     cellMiniCoordinates measure where miniCircle would draw
     */
    public final void drawSmallCircle(float _X0,
                                      float _Y0,
                                      float _squareWidth,
                                      float _radius,
                                      int _gravityC) {
        float cellMiniCoordinates = (float) (((Math.sqrt(2) * _squareWidth - 2 * _radius) / 2) / Math.sqrt(2));
        switch (_gravityC) {
            case 0:
                break;
            case 1:
                fillColorAndText.setColor(resources.getColor(R.color.red));
                mCanvas.drawCircle(_X0 + cellMiniCoordinates, _Y0 + cellMiniCoordinates, (int) (radiusCircle * COEFF_RADIUS) - 1, fillColorAndText);
                paint.setColor(resources.getColor(R.color.black));
                mCanvas.drawCircle(_X0 + cellMiniCoordinates, _Y0 + cellMiniCoordinates, (int) (radiusCircle * COEFF_RADIUS), paint);
                break;
            case 2:
                fillColorAndText.setColor(resources.getColor(R.color.dark_blue));
                mCanvas.drawCircle(_X0 + _squareWidth - cellMiniCoordinates, _Y0 + _squareWidth - cellMiniCoordinates, (int) (radiusCircle * COEFF_RADIUS) - 1, fillColorAndText);
                paint.setColor(resources.getColor(R.color.black));
                mCanvas.drawCircle(_X0 + _squareWidth - cellMiniCoordinates, _Y0 + _squareWidth - cellMiniCoordinates, (int) (radiusCircle * COEFF_RADIUS), paint);
                break;
        }
    }

    /**
     * Compare list of game with score and wins
     * @param _resultHistory takes the value from parser
     * @return list of HashMaps with score and value: P,B, DP, DB, T
     */
    public static final List<HashMap<String, String>> listOfGame(String _resultHistory) {
        List<HashMap<String, String>> listHashMapsResult = new ArrayList<HashMap<String, String>>();
        if(_resultHistory!=null){
            String[] resultHistoryArray = _resultHistory.split(",");
            HashMap<String, String> item;
            for (int i = 0; i < resultHistoryArray.length; i++) {
                String[] resultHistoryArrayWithoutSplit = resultHistoryArray[i].split(";");

                for (int j = 0; j < resultHistoryArrayWithoutSplit.length / 2; j++) {
                    item = new HashMap<String, String>();
                    item.put(resultHistoryArrayWithoutSplit[2 * j], resultHistoryArrayWithoutSplit[2 * j + 1]);
                    listHashMapsResult.add(item);
                }
            }
            return listHashMapsResult;
        }
        return listHashMapsResult;
    }

    /**
     * Check size of list compare with size of grid, and put to grid the lasts elements
     * @param _listHashMapsResult all elements
     * @return list of lasts elements
     */
    private static final  List<HashMap<String, String>> resizeList(List<HashMap<String, String>> _listHashMapsResult){
        int limitLength = _listHashMapsResult.size() - horizontal_cell * vertical_cell;
        if(limitLength > 0) {
            for(int i = limitLength-1; i >= 0; i--) {
           _listHashMapsResult.remove(_listHashMapsResult.get(i));
        }
        return _listHashMapsResult;
        }
        else return _listHashMapsResult;
    }

    public static final List<HashMap<String, String>> buildList(String _resultHistory) {
        return resizeList(listOfGame(_resultHistory));
    }

    /**
     * Draw search circle
     *
     * @param _listHashMapsResult took from listOfGame;
     */
    public final void findCircle(List<HashMap<String, String>> _listHashMapsResult) {
        int f = 0;
        for (int a = 0; a < horizontal_cell; a++) {
            for (int b = 0; b < vertical_cell; b++) {
                for (int i = f; i < _listHashMapsResult.size(); i++) {
                    if (_listHashMapsResult.get(i).containsKey("P")) {
                        drawBigCircles(a, b, resources.getColor(R.color.dark_blue), _listHashMapsResult.get(i).get("P"), resources.getColor(R.color.light_blue), false, 0);
                        i = _listHashMapsResult.size();
                    } else if (_listHashMapsResult.get(i).containsKey("B")) {
                        drawBigCircles(a, b, resources.getColor(R.color.red), _listHashMapsResult.get(i).get("B"), resources.getColor(R.color.light_red), false, 0);
                        i = _listHashMapsResult.size();

                    } else if (_listHashMapsResult.get(i).containsKey("T")) {
                        drawBigCircles(a, b, resources.getColor(R.color.dark_green_CS), _listHashMapsResult.get(i).get("T"), resources.getColor(R.color.light_green_CS), false, 0);
                        i = _listHashMapsResult.size();

                    } else if (_listHashMapsResult.get(i).containsKey("DP")) {
                        drawBigCircles(a, b, resources.getColor(R.color.dark_blue), _listHashMapsResult.get(i).get("DP"), resources.getColor(R.color.light_blue), true, 1);
                        i = _listHashMapsResult.size();

                    } else if (_listHashMapsResult.get(i).containsKey("DB")) {
                        drawBigCircles(a, b, resources.getColor(R.color.red), _listHashMapsResult.get(i).get("DB"), resources.getColor(R.color.light_red), true, 2);
                        i = _listHashMapsResult.size();
                    }
                    f++;
                }
            }
        }
    }

    /**
     * Draw grid for score table
     */
    public final void drawCustomGrid(List<HashMap<String, String>> _listHashMapsResult) {
        paint.setColor(resources.getColor(R.color.gray_light));
        for (int x = 0; x < horizontal_cell + 1; x++)
            mCanvas.drawLine((float) x * canvasSizeWidth / horizontal_cell, 0, (float) x * canvasSizeWidth / horizontal_cell, canvasSizeHigh, paint);
        for (int y = 0; y < vertical_cell + 1; y++)
            mCanvas.drawLine(0, (float) y * canvasSizeHigh / vertical_cell, canvasSizeWidth, (float) y * canvasSizeHigh / vertical_cell, paint);
        findCircle(_listHashMapsResult);
    }
}
