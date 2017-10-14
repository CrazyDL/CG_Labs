package com.crazydl.cg_lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;

public class OrthogrProj extends View{
    Paint pFigure;
    Path path;

    OctagonalPrism octPrism;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float viewWidth, viewHeight;

    public OrthogrProj(Context context) {
        super(context);
        init();
    }

    public OrthogrProj(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        octPrism = new OctagonalPrism();

        pFigure = new Paint();
        path = new Path();

        pFigure.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        pFigure.setStrokeWidth(4);
        pFigure.setStyle(Paint.Style.STROKE);
        pFigure.setStrokeJoin(Paint.Join.ROUND);
        pFigure.setStrokeCap(Paint.Cap.ROUND);
        pFigure.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorCanvasBackground));
        //canvas.save();
        canvas.translate(viewWidth / 2, viewHeight / 2);
        for (int i = 0; i < OctagonalPrism.EDGES; i++){
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
        }
        //canvas.restore();
        invalidate();
    }
}
