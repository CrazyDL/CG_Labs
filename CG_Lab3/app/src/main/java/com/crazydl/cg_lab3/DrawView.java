package com.crazydl.cg_lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class DrawView extends View {
    public static int vertAppr = 20;
    public static int horAppr = 20;
    public static float A_LEN = 200;
    public static float B_LEN = 200;
    public static float RADIUS = 200;

    private static float offsetX = 0;
    private static float offsetY = 0;
    private static float scale = 1f;

    Paint pFigure = new Paint();
    Path path = new Path();

    MyMatrix3 trans;

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

        pFigure.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        pFigure.setStrokeWidth(4);
        pFigure.setStyle(Paint.Style.STROKE);
        pFigure.setStrokeJoin(Paint.Join.ROUND);
        pFigure.setStrokeCap(Paint.Cap.ROUND);
        pFigure.setAntiAlias(true);
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

        trans = horizontalRotateMatrix.multiply(verticalRotateMatrix.multiply(scaleMatrix));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float viewWidth = canvas.getWidth();
        float viewHeight = canvas.getHeight();
        canvas.drawColor(Color.WHITE);
        canvas.translate(viewWidth / 2, viewHeight / 2);
        float trgHeight = (A_LEN + B_LEN) / vertAppr;
        float nextRadius = (float) Math.sqrt(RADIUS * RADIUS - A_LEN * A_LEN);
        for (int i = 0; i < vertAppr; i++) {
            float curRadius = nextRadius;
            nextRadius = (float) Math.sqrt(RADIUS * RADIUS - Math.pow((-A_LEN + trgHeight * (i + 1)), 2));
            for (int k = 0; k < 2; k++) {
                path.reset();
                for (int j = 0; j < horAppr; j++) {
                    float curAlpha = (2 * (float) Math.PI * j) / horAppr;
                    float nextAlpha = (2 * (float) Math.PI * (j + 1)) / horAppr;

                    Vertex a = trans.transform(new Vertex(
                            curRadius * (float) Math.sin(nextAlpha),
                            -A_LEN + trgHeight * i,
                            curRadius * (float) Math.cos(nextAlpha)));
                    Vertex b = trans.transform(new Vertex(
                            nextRadius * (float) Math.sin(curAlpha),
                            -A_LEN + trgHeight * (i + 1),
                            nextRadius * (float) Math.cos(curAlpha)));
                    Vertex c = trans.transform(new Vertex(
                            curRadius * (float) Math.sin(curAlpha),
                            -A_LEN + trgHeight * i,
                            curRadius * (float) Math.cos(curAlpha)));
                    Vertex d = trans.transform(new Vertex(
                            nextRadius * (float) Math.sin(nextAlpha),
                            -A_LEN + trgHeight * (i + 1),
                            nextRadius * (float) Math.cos(nextAlpha)));

                    Vertex v1 = new Vertex(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
                    Vertex v2 = new Vertex(b.getX() - c.getX(), b.getY() - c.getY(), b.getZ() - c.getZ());
                    Vertex v3 = new Vertex(b.getX() - d.getX(), b.getY() - d.getY(), b.getZ() - d.getZ());

                    Vertex n1 = new Vertex(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                            v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                            v1.getX() * v2.getY() - v1.getY() * v2.getX());

                    Vertex n2 = new Vertex(v2.getY() * v3.getZ() - v2.getZ() * v3.getY(),
                            v2.getZ() * v3.getX() - v2.getX() * v3.getZ(),
                            v2.getX() * v3.getY() - v2.getY() * v3.getX());
                    if (n1.getZ() > 0) {
                        path.moveTo(a.getX(), a.getY());
                        path.lineTo(b.getX(), b.getY());
                        path.lineTo(c.getX(), c.getY());
                        path.lineTo(a.getX(), a.getY());
                    }

                    if (n2.getZ() > 0) {
                        path.moveTo(b.getX(), b.getY());
                        path.lineTo(a.getX(), a.getY());
                        path.lineTo(d.getX(), d.getY());
                        path.lineTo(b.getX(), b.getY());
                    }
                }
                canvas.drawPath(path, pFigure);
            }
        }
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
