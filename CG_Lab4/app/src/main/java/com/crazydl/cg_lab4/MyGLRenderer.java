package com.crazydl.cg_lab4;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    public static int vertAppr = 20;
    public static int horAppr = 20;
    public static float aLen = 0.38f;
    public static float bLen = 0.36f;
    public static final float RADIUS = 0.4f;
    public static final int COLOR_R = 0;
    public static final int COLOR_G = 200;
    public static final int COLOR_B = 255;
    public static int width;
    public static int height;
    public static float offsetX = 10;
    public static float offsetY = 10;
    public static float scale = 1f;
    private float[] mVMatrix = new float[16];
    private float[] mPMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    static MyMatrix3 trans;

    private DrawHelper mDrawHelper;

    MyGLRenderer(Context context) {
        super();
    }

    static public void initTransformMatrix() {
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
/*

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



*/

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(1f, 1f, 1f, 1f);
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        mDrawHelper = new DrawHelper();
        initTransformMatrix();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;

        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mPMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    private float[] trianglesCoords;
    private int ind;

    private void putCoord(Vertex v) {
        trianglesCoords[ind * 3] = v.getX();
        trianglesCoords[ind * 3 + 1] = v.getY();
        trianglesCoords[ind * 3 + 2] = v.getZ();
        ind++;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        MyMatrix3 svTrans = trans;
        int svVertAppr = vertAppr;
        int svHorAppr = horAppr;
        float svALen = aLen;
        float svBLen = bLen;
        Matrix.multiplyMM(mMVPMatrix, 0, mPMatrix, 0, mVMatrix, 0);
        GLES20.glUniformMatrix4fv(mDrawHelper.getMuMVPMatrixHandle(), 1, false, mMVPMatrix, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        ind = 0;
        trianglesCoords = new float[(svVertAppr + 1 ) * svHorAppr * 18];
        Vertex a, b, c, d;
        float trgHeight = (svALen + svBLen) / svVertAppr;
        float nextRadius = (float) Math.sqrt(RADIUS * RADIUS - svALen * svALen);
        for (int i = 0; i < svVertAppr; i++) {
            float curRadius = nextRadius;
            nextRadius = (float) Math.sqrt(RADIUS * RADIUS - Math.pow((-svALen + trgHeight * (i + 1)), 2));
            for (int j = 0; j < svHorAppr; j++) {
                float curAlpha = (2 * (float) Math.PI * j) / svHorAppr;
                float nextAlpha = (2 * (float) Math.PI * (j + 1)) / svHorAppr;

                a = svTrans.transform(
                        curRadius * (float) Math.sin(nextAlpha),
                        -svALen + trgHeight * i,
                        curRadius * (float) Math.cos(nextAlpha));
                b = svTrans.transform(
                        nextRadius * (float) Math.sin(curAlpha),
                        -svALen + trgHeight * (i + 1),
                        nextRadius * (float) Math.cos(curAlpha));
                c = svTrans.transform(
                        curRadius * (float) Math.sin(curAlpha),
                        -svALen + trgHeight * i,
                        curRadius * (float) Math.cos(curAlpha));
                d = svTrans.transform(
                        nextRadius * (float) Math.sin(nextAlpha),
                        -svALen + trgHeight * (i + 1),
                        nextRadius * (float) Math.cos(nextAlpha));
                putCoord(c);
                putCoord(b);
                putCoord(a);
                putCoord(d);
                putCoord(b);
                putCoord(a);
            }
        }

        for (int k = 0; k < 2; k++) {
            for (int j = 0; j < svHorAppr; j++) {
                float curRadius = (float) Math.sqrt(RADIUS * RADIUS - (k == 0 ? svALen * svALen : svBLen * svBLen));
                float curAlpha = (2 * (float) Math.PI * j) / svHorAppr;
                float nextAlpha = (2 * (float) Math.PI * (j + 1)) / svHorAppr;

                a = svTrans.transform(
                        curRadius * (float) Math.sin(nextAlpha),
                        (k == 0 ? -svALen : svBLen),
                        curRadius * (float) Math.cos(nextAlpha));
                b = svTrans.transform(
                        0,
                        (k == 0 ? -svALen : svBLen),
                        0);
                c = svTrans.transform(
                        curRadius * (float) Math.sin(curAlpha),
                        (k == 0 ? -svALen : svBLen),
                        curRadius * (float) Math.cos(curAlpha));
                putCoord(a);
                putCoord(c);
                putCoord(b);
            }
        }
        mDrawHelper.draw(gl, trianglesCoords);
    }
}
