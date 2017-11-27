package com.crazydl.cg_lab6;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */

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

class MyGLRenderer implements GLSurfaceView.Renderer {
    static int vertAppr = 20;
    static int horAppr = 20;
    static float aLen = 0.38f;
    static float bLen = 0.36f;
    static float offsetX = 10;
    static float offsetY = 10;
    static float scale = 1.5f;
    static float lightRad = 0.1f;
    static float lightX = 0.6f;
    static float lightY = 0.6f;
    static float lightZ = 2f;
    static int width;
    static int height;
    private static final float RADIUS = 0.4f;
    private float[] mVMatrix = new float[16];
    private float[] mPMatrix = new float[16];
    static float[] mMVPMatrix = new float[16];

    private static MyMatrix3 trans;

    private DrawHelper mDrawHelper;

    MyGLRenderer() {
        super();
    }

    static void initTransformMatrix() {
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
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(1f, 1f, 1f, 1f);
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, 5f, 0f, 0f, -5f, 0f, 1.0f, 0.0f);
        int mProgram = GLES20.glCreateProgram();
        mDrawHelper = new DrawHelper(mProgram);
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
    private float[] normalCoords;
    private int ind;

    private void putCoord(Vertex v, Vertex n) {
        trianglesCoords[ind * 3] = v.getX();
        trianglesCoords[ind * 3 + 1] = v.getY();
        trianglesCoords[ind * 3 + 2] = v.getZ();
        normalCoords[ind * 3] = n.getX();
        normalCoords[ind * 3 + 1] = n.getY();
        normalCoords[ind * 3 + 2] = n.getZ();
        ind++;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        MyMatrix3 svTrans = trans;
        int svVertAppr = vertAppr;
        int svHorAppr = horAppr;
        float svALen = aLen;
        float svBLen = bLen;
        float svLightX = lightX;
        float svLightY = lightY;
        Matrix.multiplyMM(mMVPMatrix, 0, mPMatrix, 0, mVMatrix, 0);
        GLES20.glUniformMatrix4fv(mDrawHelper.getMuMVPMatrixHandle(), 1, false, mMVPMatrix, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_LIGHTING);
        ind = 0;
        trianglesCoords = new float[(svVertAppr + 1) * svHorAppr * 18 + 360 * 9];
        normalCoords = new float[(svVertAppr + 1) * svHorAppr * 18 + 360 * 9];
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

                Vertex v1 = new Vertex(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
                Vertex v2 = new Vertex(b.getX() - c.getX(), b.getY() - c.getY(), b.getZ() - c.getZ());
                Vertex v3 = new Vertex(b.getX() - d.getX(), b.getY() - d.getY(), b.getZ() - d.getZ());

                Vertex n1 = new Vertex(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                        v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                        v1.getX() * v2.getY() - v1.getY() * v2.getX());

                Vertex n2 = new Vertex(v2.getY() * v3.getZ() - v2.getZ() * v3.getY(),
                        v2.getZ() * v3.getX() - v2.getX() * v3.getZ(),
                        v2.getX() * v3.getY() - v2.getY() * v3.getX());

                putCoord(c, n1);
                putCoord(b, n1);
                putCoord(a, n1);
                putCoord(d, n2);
                putCoord(b, n2);
                putCoord(a, n2);
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
                putCoord(a, n);
                putCoord(b, n);
                putCoord(c, n);
            }
        }
        float oneGrad = (float)(Math.PI / 180f);
        for (int i = 0; i < 360; i++) {
            a = new Vertex(lightRad * (float) Math.cos(i*oneGrad) + svLightX,
                    lightRad * (float) Math.sin(i*oneGrad) + svLightY, lightZ);
            b = new Vertex(lightRad * (float) Math.cos(((i + 1) % 360) *oneGrad) + svLightX,
                    lightRad * (float) Math.sin(((i + 1) % 360) *oneGrad) + svLightY, lightZ);
            c = new Vertex(svLightX, svLightY, lightZ);
            putCoord(a, new Vertex(0, 0, 0));
            putCoord(b, new Vertex(0, 0, 0));
            putCoord(c, new Vertex(0, 0, 0));
        }
        mDrawHelper.draw(trianglesCoords, normalCoords);
    }
}
