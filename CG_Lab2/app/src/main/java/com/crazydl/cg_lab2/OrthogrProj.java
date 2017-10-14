package com.crazydl.cg_lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.renderscript.Matrix3f;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class OrthogrProj extends View{
    Paint pFigure;
    Path path;
    OctagonalPrism octPrism;
    Matrix3f matrix3f;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float offsetX = 0;
    private static float offsetY = 0;
    private static float scale = 1f;

    public OrthogrProj(Context context) {
        super(context);
        init(context);
    }

    public OrthogrProj(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        octPrism = new OctagonalPrism();

        pFigure = new Paint();
        path = new Path();

        pFigure.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        pFigure.setStrokeWidth(4);
        pFigure.setStyle(Paint.Style.STROKE);
        pFigure.setStrokeJoin(Paint.Join.ROUND);
        pFigure.setStrokeCap(Paint.Cap.ROUND);
        pFigure.setAntiAlias(true);

        matrix3f = new Matrix3f();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float viewWidth = canvas.getWidth();
        float viewHeight = canvas.getHeight();
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorCanvasBackground));

        canvas.translate(viewWidth / 2, viewHeight / 2);
        matrix3f.scale(scale, scale, scale);

        path.reset();
        for (int i = 0; i < OctagonalPrism.EDGES - 1; i++){
            path.moveTo(octPrism.vrts[0][i].getX(), octPrism.vrts[0][i].getY());
            path.lineTo(octPrism.vrts[0][i + 1].getX(), octPrism.vrts[0][i + 1].getY());
            path.lineTo(octPrism.vrts[1][i + 1].getX(), octPrism.vrts[1][i + 1].getY());
            path.lineTo(octPrism.vrts[1][i].getX(), octPrism.vrts[1][i].getY());
            path.lineTo(octPrism.vrts[0][i].getX(), octPrism.vrts[0][i].getY());
        }
        path.moveTo(octPrism.vrts[0][OctagonalPrism.EDGES - 1].getX(), octPrism.vrts[0][OctagonalPrism.EDGES - 1].getY());
        path.lineTo(octPrism.vrts[0][0].getX(), octPrism.vrts[0][0].getY());
        path.moveTo(octPrism.vrts[1][OctagonalPrism.EDGES - 1].getX(), octPrism.vrts[1][OctagonalPrism.EDGES - 1].getY());
        path.lineTo(octPrism.vrts[1][0].getX(), octPrism.vrts[1][0].getY());

        canvas.drawPath(path, pFigure);


        /*for (int i = 0; i < OctagonalPrism.EDGES; i++){
            canvas.drawLine(octPrism.vrts[0][i].getX(), octPrism.vrts[0][i].getY(),
                    octPrism.vrts[1][i].getX(), octPrism.vrts[1][i].getY(), pFigure);
        }
        for (int k = 0; k < 2; k++){
            path.reset();
            path.moveTo(octPrism.vrts[k][0].getX(), octPrism.vrts[k][0].getY());
            for (int i = 1; i < OctagonalPrism.EDGES; i++){
                path.lineTo(octPrism.vrts[k][i].getX(), octPrism.vrts[k][i].getY());
            }
            path.lineTo(octPrism.vrts[k][0].getX(), octPrism.vrts[k][0].getY());
            canvas.drawPath(path, pFigure);
        }*/

        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newScale = scale * scaleGestureDetector.getScaleFactor();
            if(newScale > 0.4 && newScale < 2)
                scale = newScale;
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
            scale = 1f;
            offsetX = 0;
            offsetY = 0;
            return true;
        }
    }
}
