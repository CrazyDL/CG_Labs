package com.crazydl.cg_cp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class DrawView extends View {
    private int POINT_COUNT = 4;
    private int POINT_RADIUS = 20;
    private float Z = 0;
    private int constA = 50;
    private float[][] points = {{-350, 0, Z}, {-200, -150, Z}, {-50, 100, Z}, {300, 0, Z}};
    private float offsetX = 0;
    private float offsetY = 0;
    private float scale = 1f;
    public static float step = 0.02f;
    private boolean isMove = false;
    private int curPoint = -1;
    private float halfWidth, halfHeight;
    MyMatrix3 trans;
    MyMatrix3 reversTrans;


    Paint pFigureContour = new Paint();
    Paint pPoint = new Paint();
    Path path = new Path();

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        pFigureContour.setColor(getResources().getColor(R.color.colorAccent));
        pFigureContour.setAlpha(150);
        pFigureContour.setStrokeWidth(1);
        pFigureContour.setStyle(Paint.Style.STROKE);
        pFigureContour.setStrokeJoin(Paint.Join.ROUND);
        pFigureContour.setStrokeCap(Paint.Cap.ROUND);
        pFigureContour.setAntiAlias(true);

        pPoint.setStyle(Paint.Style.FILL);
        pPoint.setAntiAlias(true);
        pPoint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        initTransformMatrix();
    }

    private void initTransformMatrix() {
        MyMatrix3 scaleMatrix = new MyMatrix3(new float[]{
                scale, 0, 0,
                0, scale, 0,
                0, 0, scale});

        MyMatrix3 horizontalRotateMatrix = new MyMatrix3(new float[]{
                (float) Math.cos(offsetX), 0, (float) Math.sin(offsetX),
                0, 1, 0,
                (float) -Math.sin(offsetX), 0, (float) Math.cos(offsetX)});

        MyMatrix3 verticalRotateMatrix = new MyMatrix3(new float[]{
                1, 0, 0,
                0, (float) Math.cos(offsetY), (float) Math.sin(offsetY),
                0, (float) -Math.sin(offsetY), (float) Math.cos(offsetY)});

        MyMatrix3 revScaleMatrix = new MyMatrix3(new float[]{
                1/scale, 0, 0,
                0, 1/scale, 0,
                0, 0, 1/scale,});

        MyMatrix3 revHorizontalRotateMatrix = new MyMatrix3(new float[] {
                (float)Math.cos(offsetX), 0, -(float)Math.sin(offsetX),
                0, 1, 0,
                (float)Math.sin(offsetX), 0, (float)Math.cos(offsetX),});

        MyMatrix3 revVerticalRotateMatrix = new MyMatrix3(new float[] {
                1, 0, 0,
                0, (float)Math.cos(offsetY), -(float)Math.sin(offsetY),
                0, (float)Math.sin(offsetY), (float)Math.cos(offsetY),});


        trans = horizontalRotateMatrix.multiply(verticalRotateMatrix.multiply(scaleMatrix));
        reversTrans = (revScaleMatrix.multiply(revVerticalRotateMatrix)).multiply(revHorizontalRotateMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        halfWidth = canvas.getWidth() / 2;
        halfHeight = canvas.getHeight() / 2;
        canvas.translate(halfWidth, halfHeight);

        Vertex point;
        path.reset();
        for (float t = step; t <= 1f; t += step) {
            float x = (1-t)*(1-t)*(1-t)*points[0][0] + 3*t*(1-t)*(1-t)*points[1][0] + 3*t*t*(1-t)*points[2][0] + t*t*t*points[3][0];
            float y = (1-t)*(1-t)*(1-t)*points[0][1] + 3*t*(1-t)*(1-t)*points[1][1] + 3*t*t*(1-t)*points[2][1] + t*t*t*points[3][1];
            point = trans.transform(x, y + constA, 0);
            path.moveTo(point.getX(), point.getY());
            for (float i = 0; i < Math.PI * 2; i += Math.PI / 30) {
                float y1 = 2 * constA * (float)Math.cos(i) - constA * (float)Math.cos(2*i);
                float z1 = 2 * constA * (float)Math.sin(i) - constA * (float)Math.sin(2*i);
                point = trans.transform(x, y + y1, z1);
                path.lineTo(point.getX(), point.getY());
            }
        }

        canvas.drawPath(path, pFigureContour);
        path.reset();

        for (int i = 0; i < POINT_COUNT; i++) {
            point = trans.transform(points[i][0], points[i][1], points[i][2]);
            canvas.drawCircle(point.getX(), point.getY(), POINT_RADIUS, pPoint);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            for(int i = 0; i < POINT_COUNT; i++){
                Vertex point = trans.transform(points[i][0], points[i][1], points[i][2]);
                if(x > point.getX() + halfWidth - POINT_RADIUS &&
                        x < point.getX() + halfWidth + POINT_RADIUS &&
                        y > point.getY() + halfHeight - POINT_RADIUS &&
                        y < point.getY() + halfHeight + POINT_RADIUS){
                    curPoint = i;
                    isMove = true;
                    break;
                }
                else
                    isMove = false;
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP && isMove){
            isMove = false;
        }
        if(isMove && curPoint != -1){
            Vertex point = reversTrans.transform(x - halfWidth, y - halfHeight, points[curPoint][2]);
            points[curPoint][0] = point.getX();
            points[curPoint][1] = point.getY();
        }
        else {
            gestureDetector.onTouchEvent(event);
            scaleGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newScale = scale * scaleGestureDetector.getScaleFactor();
            if (newScale > 0.4 && newScale < 2) {
                scale = newScale;
                initTransformMatrix();
            }
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            offsetX += distanceX / 200;
            offsetY += distanceY / 200;
            initTransformMatrix();
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            scale = 1f;
            offsetX = 0;
            offsetY = 0;
            initTransformMatrix();
            return true;
        }
    }
}
