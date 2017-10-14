package com.crazydl.cg_lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;

public class OrthogrProj extends View{
    Paint pFigure = new Paint();
    Paint pText = new Paint();

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float viewWidth, viewHeight;

    public OrthogrProj(Context context) {
        super(context);
    }

    public OrthogrProj(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorCanvasBackground));
        pText.setTextSize(64);
        pText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("TODO", viewWidth / 2, viewHeight / 2, pText);
        invalidate();
    }
}
