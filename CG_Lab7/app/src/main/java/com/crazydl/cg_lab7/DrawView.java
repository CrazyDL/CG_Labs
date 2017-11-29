package com.crazydl.cg_lab7;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    private static final int POINTS_COUNT = 6;
    private static final int DEGREE = 3;
    private static final float POINT_RADIUS = 15;
    private boolean isMove = false;
    private int curPoint = -1;
    private float[][] points = {{50, 50}, {200, 300}, {400, 200}, {250, 200}, {500, 100}, {700, 500}};
    private float[] t = {0, 0, 0, 1, 2, 3, 4, 4, 4};
    Paint pPoint = new Paint();
    Paint pLine = new Paint();
    Path path = new Path();

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        pLine.setColor(getResources().getColor(R.color.colorPrimary));
        pLine.setStrokeWidth(4);
        pLine.setStyle(Paint.Style.STROKE);
        pLine.setStrokeJoin(Paint.Join.ROUND);
        pLine.setStrokeCap(Paint.Cap.ROUND);
        pLine.setAntiAlias(true);

        pPoint.setColor(getResources().getColor(R.color.colorAccent));
        pPoint.setStyle(Paint.Style.FILL);

    }

    private float[] bSpline(float x) {
        float[] result = {0, 0};
        for (int i = 0; i < POINTS_COUNT; i++){
            float resN = N(i, DEGREE, x);
            result[0] += points[i][0] * resN;
            result[1] += points[i][1] * resN;
        }
        return result;
    }

    private float div(float a, float b) {
        return b == 0 ? 0 : a / b;
    }

    private float N(int i, int k, float x) {
        if (k == 1){
            return (t[i] <= x && x < t[i + 1]) ? 1 : 0;
        }

        float a = div(x - t[i], t[i + k - 1] - t[i]) *  N(i, k - 1, x);
        float b = div(t[i + k] - x, t[i + k] - t[i + 1]) * N(i + 1, k - 1, x);
        return a + b;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        float[] point;
        path.reset();
        path.moveTo(points[0][0], points[0][1]);
        for (float t = 0; t <= 4; t += 0.01){
            point = bSpline(t);
            path.lineTo(point[0], point[1]);
        }
        canvas.drawPath(path, pLine);
        for (float[] pnt : points) {
            canvas.drawCircle(pnt[0], pnt[1], POINT_RADIUS, pPoint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currX = event.getX();
        float currY = event.getY();
        if(isMove){
            if(curPoint != -1 && currX < getWidth() && currX > 0 && currY < getHeight() && currY > 0){
                points[curPoint][0] = currX;
                points[curPoint][1] = currY;
            }
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                for(int i = 0; i < POINTS_COUNT; i++){
                    if(currX > points[i][0] - POINT_RADIUS*2 &&
                            currX < points[i][0] + POINT_RADIUS*2 &&
                            currY > points[i][1] - POINT_RADIUS*2 &&
                            currY < points[i][1] + POINT_RADIUS*2){
                        isMove = true;
                        curPoint = i;
                        break;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                isMove = false;
                curPoint = -1;
                break;
        }
        invalidate();
        return true;
    }
}
