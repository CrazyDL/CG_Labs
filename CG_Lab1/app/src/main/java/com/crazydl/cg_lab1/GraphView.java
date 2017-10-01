package com.crazydl.cg_lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Locale;

public class GraphView extends SurfaceView implements SurfaceHolder.Callback{
    Paint pAxis = new Paint();
    Paint pField = new Paint();
    Paint pGraph = new Paint();
    Paint pText = new Paint();
    Path pathGraph = new Path();

    private DrawThread drawThread;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float fieldSize, textSize = 14, cellSize,
                         scaleFactor, optimalScale,
                         viewWidth, viewHeight,
                         offsetX = 0, offsetY = 0,
                         a = 1, k = 0.1f, B = 20 * (float)Math.PI;

    public GraphView(Context context) {
        super(context);
        init(context);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private static float function(float t){
        return a * (float)Math.exp(k * t);
    }

    private void init(Context context){
        pAxis.setColor(Color.BLACK);
        pAxis.setStrokeWidth(2);
        pAxis.setAntiAlias(true);

        pField.setColor(ContextCompat.getColor(getContext(), R.color.colorAxis));
        pField.setStrokeWidth(1);
        pField.setAntiAlias(true);

        pGraph.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        pGraph.setStrokeWidth(4);
        pGraph.setStyle(Paint.Style.STROKE);
        pGraph.setAntiAlias(true);

        pText.setColor(Color.BLACK);
        pText.setTextSize(textSize);
        pText.setAntiAlias(true);
        pText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        scaleFactor = 1f;
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        getHolder().addCallback(this);
    }

    public static void setConstants(float _a, float _k, float _B){
        a = _a;
        k = _k;
        B = _B * (float)Math.PI / 180;
        setConstants();
    }

    public static void setConstants(){
        float res = function(0);

        float maxX = res * (float)Math.cos(0),
              maxY = res * (float)Math.sin(0);

        float x, y;
        for (float t = 0; t <= B; t += Math.PI/180){
            res = function(t);
            x = Math.abs(res * (float)Math.cos(t));
            y = Math.abs(res * (float)Math.sin(t));
            if(x > maxX)
                maxX = x;
            if(y > maxY)
                maxY = y;
        }
        optimalScale = Math.min((viewWidth / 2) / maxX, (viewHeight / 2) / maxY);
        optimalScale -= optimalScale / 20;

        scaleFactor = 1f;
        offsetX = 0;
        offsetY = 0;

        int fieldCount = 20;
        fieldSize = Math.max(viewWidth / fieldCount, viewHeight / fieldCount);

        cellSize = fieldSize / optimalScale;
    }

    void drawGraph(Canvas canvas){
        float lclSF = scaleFactor,
              lclOffsetX = offsetX,
              lclOffsetY = offsetY;
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorCanvasBackground));

        float canvasWidth = canvas.getWidth();
        float canvasHeight = canvas.getHeight();

        float halfHeight = canvasHeight / 2;
        float halfWidth = canvasWidth / 2;

        float fieldOX = lclOffsetX % (fieldSize * lclSF);
        float fieldOY = lclOffsetY % (fieldSize * lclSF);

        pText.setTextSize(textSize);
        pText.setTextAlign(Paint.Align.CENTER);
        float value;
        if(lclOffsetX < 0)
            value = ((int)Math.floor((-lclOffsetX / lclSF)/ fieldSize) + 1) * cellSize;
        else
            value = ((int)Math.ceil((-lclOffsetX / lclSF)/ fieldSize) + 1) * cellSize;
        for (float x = halfWidth + fieldSize * lclSF; x + fieldOX <= canvasWidth; x += fieldSize * lclSF){
            canvas.drawLine(x + fieldOX, 0, x + fieldOX, viewHeight, pField);
            canvas.drawText(String.format(Locale.ENGLISH, "%.2f", value), x + fieldOX, halfHeight + lclOffsetY - 4, pText);
            value +=  cellSize;
        }
        if(lclOffsetX < 0)
            value =  (int)Math.floor((-lclOffsetX / lclSF)/ fieldSize) * cellSize;
        else
            value =  (int)Math.ceil((-lclOffsetX / lclSF)/ fieldSize) * cellSize;
        for (float x = halfWidth; x + fieldOX >= 0; x -= fieldSize * lclSF){
            canvas.drawLine(x + fieldOX, 0, x + fieldOX, viewHeight, pField);
            if(x + fieldOX != halfWidth + lclOffsetX)
                canvas.drawText(String.format(Locale.ENGLISH, "%.2f", value), x + fieldOX, halfHeight + lclOffsetY - 4, pText);
            value -=  cellSize;
        }

        pText.setTextAlign(Paint.Align.LEFT);

        if(lclOffsetY < 0)
            value = (int)Math.ceil((lclOffsetY / lclSF)/ fieldSize) * cellSize;
        else
            value = (int)Math.floor((lclOffsetY / lclSF)/ fieldSize) * cellSize;

        for (float y = halfHeight; y + fieldOY <= viewHeight; y += fieldSize * lclSF){
            canvas.drawLine(0, y + fieldOY, viewWidth, y + fieldOY, pField);
            canvas.drawText(String.format(Locale.ENGLISH, "%.2f", value), halfWidth + lclOffsetX + 4, y + fieldOY, pText);
            value -=  cellSize;
        }
        if(lclOffsetY < 0)
            value = (int)Math.ceil((lclOffsetY / lclSF)/ fieldSize) * cellSize + cellSize;
        else
            value = (int)Math.floor((lclOffsetY / lclSF)/ fieldSize) * cellSize + cellSize;
        for (float y = halfHeight - fieldSize * lclSF; y + fieldOY >= 0; y -= fieldSize * lclSF){
            canvas.drawLine(0, y + fieldOY, viewWidth, y + fieldOY, pField);
            canvas.drawText(String.format(Locale.ENGLISH, "%.2f", value), halfWidth + lclOffsetX + 4, y + fieldOY, pText);
            value +=  cellSize;
        }
        pText.setTextSize(18);
        canvas.drawText(String.format(Locale.ENGLISH, "r = %.2f * e^(%.2f * fi)", a, k), 10, 20, pText);
        canvas.drawText(String.format(Locale.ENGLISH, "fi <= %.2f", B * 180 / Math.PI), 10, 40, pText);

        canvas.drawLine(0, halfHeight + lclOffsetY, canvasWidth, halfHeight + lclOffsetY, pAxis);
        canvas.drawLine(halfWidth + lclOffsetX, 0, halfWidth + lclOffsetX, canvasHeight, pAxis);

        float res = function(0);
        pathGraph.reset();
        pathGraph.moveTo(lclOffsetX + halfWidth + res * (float)Math.cos(0) * optimalScale * lclSF,
                lclOffsetY + halfHeight - res * (float)Math.sin(0) * optimalScale * lclSF);
        for (float t = 0; t <= B; t += Math.PI/180){
            res = function(t);
            pathGraph.lineTo(lclOffsetX + halfWidth + res * (float)Math.cos(t) * optimalScale * lclSF,
                    lclOffsetY + halfHeight - res * (float)Math.sin(t) * optimalScale * lclSF);
        }
        canvas.drawPath(pathGraph, pGraph);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        viewHeight = i2;
        viewWidth = i1;
        setConstants();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry){
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException ignored) {}
        }
    }

    private class DrawThread extends Thread{
        private SurfaceHolder surfaceHolder;
        private boolean running = false;

        DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running){
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if(canvas == null)
                        continue;
                    drawGraph(canvas);
                } finally {
                    if(canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }


    }
    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newScale = scaleFactor * scaleGestureDetector.getScaleFactor();
            if(newScale > 0.4 && newScale < 8){
                float focusX = scaleGestureDetector.getFocusX();
                float focusY = scaleGestureDetector.getFocusY();
                scaleFactor = newScale;
                if(newScale < 0.6)
                    textSize = 8;
                else if(newScale < 0.8)
                    textSize = 10;
                else if(newScale < 1)
                    textSize = 12;
                else if(newScale < 2)
                    textSize = 14;
                else if(newScale < 4)
                    textSize = 16;
                else
                    textSize = 18;
                /*float a = getScrollX();
                float b = getScrollY();
                offsetX = focusX;
                offsetY = focusY;
                /*offsetX += offsetX + (getScrollX() + focusX) * scaleFactor - focusX;
                offsetY += offsetY + (getScrollY() + focusY) * scaleFactor - focusY;*/



            }
            return true;
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            offsetX -= distanceX;
            offsetY -= distanceY;
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event){
            scaleFactor = 1f;
            offsetX = 0;
            offsetY = 0;
            return true;
        }
    }
}
