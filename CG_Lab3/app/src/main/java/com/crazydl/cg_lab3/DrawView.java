package com.crazydl.cg_lab3;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class DrawView extends View {
    public static int vertAppr = 20;
    public static int horAppr = 20;
    public static float aLen = 180;
    public static float bLen = 160;
    public static final float RADIUS = 200;
    public static final int COLOR_R = 0;
    public static final int COLOR_G = 200;
    public static final int COLOR_B = 255;

    private static float offsetX = 0;
    private static float offsetY = 0;
    private static float scale = 1f;

    Paint pFigureContour = new Paint();
    Paint pFigureFill = new Paint();
    Path path = new Path();
    Path path2 = new Path();

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

        pFigureContour.setColor(Color.BLACK);
        pFigureContour.setStrokeWidth(1);
        pFigureContour.setStyle(Paint.Style.STROKE);
        pFigureContour.setStrokeJoin(Paint.Join.ROUND);
        pFigureContour.setStrokeCap(Paint.Cap.ROUND);
        pFigureContour.setAntiAlias(true);

        pFigureFill.setStyle(Paint.Style.FILL);

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
        canvas.drawColor(Color.WHITE);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        Vertex a, b, c, d;
        float trgHeight = (aLen + bLen) / vertAppr;
        float nextRadius = (float) Math.sqrt(RADIUS * RADIUS - aLen * aLen);
        path2.reset();
        for (int i = 0; i < vertAppr; i++) {
            float curRadius = nextRadius;
            nextRadius = (float) Math.sqrt(RADIUS * RADIUS - Math.pow((-aLen + trgHeight * (i + 1)), 2));
            for (int j = 0; j < horAppr; j++) {
                float curAlpha = (2 * (float) Math.PI * j) / horAppr;
                float nextAlpha = (2 * (float) Math.PI * (j + 1)) / horAppr;

                a = trans.transform(
                        curRadius * (float) Math.sin(nextAlpha),
                        -aLen + trgHeight * i,
                        curRadius * (float) Math.cos(nextAlpha));
                b = trans.transform(
                        nextRadius * (float) Math.sin(curAlpha),
                        -aLen + trgHeight * (i + 1),
                        nextRadius * (float) Math.cos(curAlpha));
                c = trans.transform(
                        curRadius * (float) Math.sin(curAlpha),
                        -aLen + trgHeight * i,
                        curRadius * (float) Math.cos(curAlpha));
                d = trans.transform(
                        nextRadius * (float) Math.sin(nextAlpha),
                        -aLen + trgHeight * (i + 1),
                        nextRadius * (float) Math.cos(nextAlpha));

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
                    path.reset();
                    path.moveTo(a.getX(), a.getY());
                    path.lineTo(b.getX(), b.getY());
                    path.lineTo(c.getX(), c.getY());
                    path.lineTo(a.getX(), a.getY());
                    path2.addPath(path);
                    float norm = (float) Math.sqrt(n1.getX() * n1.getX() + n1.getY() * n1.getY() + n1.getZ() * n1.getZ());
                    pFigureFill.setARGB(255, (int) (COLOR_R * (n1.getZ() / norm)),
                            (int) (COLOR_G * (n1.getZ() / norm)),
                            (int) (COLOR_B * (n1.getZ() / norm)));
                    canvas.drawPath(path, pFigureFill);
                }

                if (n2.getZ() > 0) {
                    path.reset();
                    path.moveTo(b.getX(), b.getY());
                    path.lineTo(a.getX(), a.getY());
                    path.lineTo(d.getX(), d.getY());
                    path.lineTo(b.getX(), b.getY());
                    path2.addPath(path);
                    float norm = (float) Math.sqrt(n2.getX() * n2.getX() + n2.getY() * n2.getY() + n2.getZ() * n2.getZ());
                    pFigureFill.setARGB(255, (int) (COLOR_R * (n2.getZ() / norm)),
                            (int) (COLOR_G * (n2.getZ() / norm)),
                            (int) (COLOR_B * (n2.getZ() / norm)));
                    canvas.drawPath(path, pFigureFill);
                }
            }
        }
        for (int k = 0; k < 2; k++) {
            for (int j = 0; j < horAppr; j++) {
                float curRadius = (float) Math.sqrt(RADIUS * RADIUS - (k == 0 ? aLen * aLen : bLen * bLen));
                float curAlpha = (2 * (float) Math.PI * j) / horAppr;
                float nextAlpha = (2 * (float) Math.PI * (j + 1)) / horAppr;

                a = trans.transform(
                        curRadius * (float) Math.sin(nextAlpha),
                        (k == 0 ? -aLen : bLen),
                        curRadius * (float) Math.cos(nextAlpha));
                b = trans.transform(
                        0,
                        (k == 0 ? -aLen : bLen),
                        0);
                c = trans.transform(
                        curRadius * (float) Math.sin(curAlpha),
                        (k == 0 ? -aLen : bLen),
                        curRadius * (float) Math.cos(curAlpha));

                Vertex v1 = new Vertex(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
                Vertex v2 = new Vertex(c.getX() - a.getX(), c.getY() - a.getY(), c.getZ() - a.getZ());

                Vertex n;
                if (k == 0) {
                    n = new Vertex(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                            v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                            v1.getX() * v2.getY() - v1.getY() * v2.getX());
                } else {
                    n = new Vertex(v1.getZ() * v2.getY() - v1.getY() * v2.getZ(),
                            v1.getX() * v2.getZ() - v1.getZ() * v2.getX(),
                            v1.getY() * v2.getX() - v1.getX() * v2.getY());
                }

                if (n.getZ() > 0) {
                    path.reset();
                    path.moveTo(a.getX(), a.getY());
                    path.lineTo(b.getX(), b.getY());
                    path.lineTo(c.getX(), c.getY());
                    path.lineTo(a.getX(), a.getY());
                    path2.addPath(path);
                    float norm = (float) Math.sqrt(n.getX() * n.getX() + n.getY() * n.getY() + n.getZ() * n.getZ());
                    pFigureFill.setARGB(255, (int) (COLOR_R * (n.getZ() / norm)),
                            (int) (COLOR_G * (n.getZ() / norm)),
                            (int) (COLOR_B * (n.getZ() / norm)));
                    canvas.drawPath(path, pFigureFill);
                }
            }
        }
        canvas.drawPath(path2, pFigureContour);
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
