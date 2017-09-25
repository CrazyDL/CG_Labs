package com.crazydl.cg_lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class GraphView extends View {
    Paint pAxis = new Paint();
    Paint pGraph = new Paint();
    Path pathGraph = new Path();

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float scaleFactor, optimalScale;
    private static float viewWidth, viewHeight;
    private static float a = 1, k = 0.1f, B = 4 * (float)Math.PI;

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
        init(context);
    }

    private static float function(float t){
        return a * (float)Math.exp(k * t);
    }

    private void init(Context context){
        pAxis.setColor(ContextCompat.getColor(context, R.color.colorBlack));
        pAxis.setStrokeWidth(2);
        pAxis.setAntiAlias(true);

        pGraph.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        pGraph.setStrokeWidth(4);
        pGraph.setStyle(Paint.Style.STROKE);
        pGraph.setAntiAlias(true);

        viewWidth = context.getResources().getDisplayMetrics().widthPixels;
        viewHeight = context.getResources().getDisplayMetrics().heightPixels;
        scaleFactor = 1f;
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        setConstants();
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
        //Toast.makeText(getContext(), "maxX: " + Float.toString(maxX) +
        //                             "\nmaxY: " + Float.toString(maxY), Toast.LENGTH_SHORT).show();

        optimalScale = Math.min((viewWidth / 2) / maxX, (viewHeight / 2) / maxY);
        optimalScale -= optimalScale / 20;

        scaleFactor = 1f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorCanvasBackground));

        float canvasWidth = canvas.getWidth();
        float canvasHeight = canvas.getHeight();

        float halfHeight = canvasHeight / 2;
        float halfWidth = canvasWidth / 2;

        canvas.drawLine(0, halfHeight, canvasWidth, halfHeight, pAxis);
        canvas.drawLine(halfWidth, 0, halfWidth, canvasHeight, pAxis);

        /*canvas.drawLine(halfWidth - canvasWidth / 150, canvasHeight / 50, halfWidth, 0, pAxis);
        canvas.drawLine(halfWidth + canvasWidth / 150, canvasHeight / 50, halfWidth, 0, pAxis);

        canvas.drawLine(canvasWidth - canvasWidth / 50, halfHeight - canvasWidth / 150, canvasWidth, halfHeight, pAxis);
        canvas.drawLine(canvasWidth - canvasWidth / 50, halfHeight + canvasWidth / 150, canvasWidth, halfHeight, pAxis);
        */


        float res = function(0);
        pathGraph.reset();
        pathGraph.moveTo(halfWidth + res * (float)Math.cos(0) * optimalScale * scaleFactor,
                    halfHeight - res * (float)Math.sin(0) * optimalScale * scaleFactor);
        for (float t = 0; t <= B; t += Math.PI/180){
            res = function(t);
            pathGraph.lineTo(halfWidth + res * (float)Math.cos(t) * optimalScale * scaleFactor,
                    halfHeight - res * (float)Math.sin(t) * optimalScale * scaleFactor);
        }
        canvas.drawPath(pathGraph, pGraph);
        invalidate();
    }


    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newScale = scaleFactor * scaleGestureDetector.getScaleFactor();
            if(newScale > 0.4 && newScale <  4){
                GraphView.scaleFactor = newScale;
            }
            return true;
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /*if(getScrollX() + distanceX < canvasWidth - viewWidth  && getScrollX() + distanceX > 0){
                scrollBy((int)distanceX, 0);
            }

            if(getScrollY() + distanceY < canvasHeight - viewHeight  && getScrollY() + distanceY > 0){
                scrollBy(0, (int)distanceY);
            }*/
            return true;
        }
    }
}
