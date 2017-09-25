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
    private float mScaleFactor;

    private static float canvasWidth, canvasHeight, viewWidth, viewHeight;
    private static float a, k, B;

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
        float r = a * (float)Math.exp(k * t);
        return r;
    }

    private void init(Context context){
        pAxis.setColor(ContextCompat.getColor(context, R.color.colorAxis));
        pAxis.setStrokeWidth(2);
        pAxis.setAntiAlias(true);

        pGraph.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        pGraph.setStrokeWidth(4);
        pGraph.setStyle(Paint.Style.STROKE);
        pGraph.setAntiAlias(true);

        viewWidth = context.getResources().getDisplayMetrics().widthPixels;
        viewHeight = context.getResources().getDisplayMetrics().heightPixels;
        mScaleFactor = 1f;
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
        setConstants(1, 0.1f, 1800);
    }

    public static void setConstants(float _a, float _k, float _B){
        a = _a;
        k = _k;
        B = _B;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        float halfHeight = canvasHeight / 2;
        float halfWidth = canvasWidth / 2;

        canvas.drawLine(0, halfHeight, canvasWidth, halfHeight, pAxis);
        canvas.drawLine(halfWidth, 0, halfWidth, canvasHeight, pAxis);

        canvas.drawLine(halfWidth - canvasWidth / 150, canvasHeight / 50, halfWidth, 0, pAxis);
        canvas.drawLine(halfWidth + canvasWidth / 150, canvasHeight / 50, halfWidth, 0, pAxis);

        canvas.drawLine(canvasWidth - canvasWidth / 50, halfHeight - canvasWidth / 150, canvasWidth, halfHeight, pAxis);
        canvas.drawLine(canvasWidth - canvasWidth / 50, halfHeight + canvasWidth / 150, canvasWidth, halfHeight, pAxis);

        float radB = B * (float)Math.PI / 180;
        float res = function(0);

        float maxX = res * (float)Math.cos(0),
              maxY = res * (float)Math.sin(0);

        float x, y;
        for (float t = 0; t <= radB; t += Math.PI/180){
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

        float scale = Math.min(halfWidth / maxX, halfHeight / maxY);
        scale -= scale / 20;

        res = function(0);
        pathGraph.reset();
        pathGraph.moveTo(halfWidth + res * (float)Math.cos(0) * scale, halfHeight - res * (float)Math.sin(0) * scale);
        for (float t = 0; t <= radB; t += Math.PI/180){
            res = function(t);
            pathGraph.lineTo(halfWidth + res * (float)Math.cos(t)* scale, halfHeight - res * (float)Math.sin(t) * scale );
        }
        canvas.drawPath(pathGraph, pGraph);

        canvas.restore();
    }


    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();

            if(mScaleFactor * scaleFactor > 1 && mScaleFactor * scaleFactor < 4){
                mScaleFactor *= scaleGestureDetector.getScaleFactor();
                canvasWidth *= mScaleFactor;
                canvasHeight *=  mScaleFactor;

                float scrollX= (getScrollX() + focusX) * scaleFactor - focusX;
                scrollX = Math.min(Math.max(scrollX, 0), canvasWidth - viewWidth);
                float scrollY= (getScrollY() + focusY) * scaleFactor-focusY;
                scrollY = Math.min(Math.max(scrollY, 0), canvasHeight - viewHeight);
                scrollTo((int)scrollX, (int)scrollY);
            }

            invalidate();
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
