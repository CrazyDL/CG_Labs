package com.crazydl.cg_lab6;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

class MyGLSurfaceView extends GLSurfaceView {
    private boolean isMove = false;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    public MyGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyGLRenderer());

        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] invertedMatrix, normalizedInPoint, outPoint;
        invertedMatrix = new float[16];
        normalizedInPoint = new float[4];
        outPoint = new float[4];

        int oglTouchY = (int) (MyGLRenderer.height  - event.getY());
        normalizedInPoint[0] = (float) ((event.getX()) * 2.0f / MyGLRenderer.width - 1.0);
        normalizedInPoint[1] = (float) ((oglTouchY) * 2.0f / MyGLRenderer.height - 1.0);
        normalizedInPoint[2] = - 1.0f;
        normalizedInPoint[3] = 1.0f;

        Matrix.invertM(invertedMatrix, 0, MyGLRenderer.mMVPMatrix, 0);

        Matrix.multiplyMV(
                outPoint, 0,
                invertedMatrix, 0,
                normalizedInPoint, 0);

        float x = outPoint[0] / outPoint[3];
        float y = outPoint[1] / outPoint[3];
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(x >= MyGLRenderer.lightX - MyGLRenderer.lightRad &&
                    x <= MyGLRenderer.lightX + MyGLRenderer.lightRad &&
                    y >= MyGLRenderer.lightY - MyGLRenderer.lightRad &&
                    y <= MyGLRenderer.lightY + MyGLRenderer.lightRad
                    ){
                MyGLRenderer.lightX = x;
                MyGLRenderer.lightY = y;
                isMove = true;
            }
            else
                isMove = false;

        }
        else if(event.getAction() == MotionEvent.ACTION_UP && isMove){
            isMove = false;
        }
        if(isMove){
            MyGLRenderer.lightX = x;
            MyGLRenderer.lightY = y;
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
            float newScale = MyGLRenderer.scale * scaleGestureDetector.getScaleFactor();
            if (newScale > 0.4 && newScale < 2.5) {
                MyGLRenderer.scale = newScale;
                MyGLRenderer.initTransformMatrix();
            }
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /*float[] invertedMatrix, normalizedInPoint,  normalizedInPoint2, outPoint, outPoint2;
            invertedMatrix = new float[16];
            normalizedInPoint = new float[4];
            normalizedInPoint2 = new float[4];
            outPoint = new float[4];
            outPoint2 = new float[4];

            int oglTouchY = (int) (MyGLRenderer.height  - e1.getY());
            int oglTouchY2 = (int) (MyGLRenderer.height  - e2.getY());

            normalizedInPoint[0] =
                    (float) ((e1.getX()) * 2.0f / MyGLRenderer.width - 1.0);
            normalizedInPoint[1] =
                    (float) ((oglTouchY) * 2.0f / MyGLRenderer.height - 1.0);
            normalizedInPoint[2] = - 1.0f;
            normalizedInPoint[3] = 1.0f;

            normalizedInPoint2[0] =
                    (float) ((e2.getX()) * 2.0f / MyGLRenderer.width - 1.0);
            normalizedInPoint2[1] =
                    (float) ((oglTouchY2) * 2.0f / MyGLRenderer.height - 1.0);
            normalizedInPoint2[2] = - 1.0f;
            normalizedInPoint2[3] = 1.0f;

            Matrix.invertM(invertedMatrix, 0, MyGLRenderer.mMVPMatrix, 0);

            Matrix.multiplyMV(
                    outPoint, 0,
                    invertedMatrix, 0,
                    normalizedInPoint, 0);

            Matrix.multiplyMV(
                    outPoint2, 0,
                    invertedMatrix, 0,
                    normalizedInPoint2, 0);

            float x = outPoint[0] / outPoint[3];
            float y = outPoint[1] / outPoint[3];
            if(x >= MyGLRenderer.lightX - MyGLRenderer.lightRad &&
                    x <= MyGLRenderer.lightX + MyGLRenderer.lightRad &&
                    y >= MyGLRenderer.lightY - MyGLRenderer.lightRad &&
                    y <= MyGLRenderer.lightY + MyGLRenderer.lightRad
                    ){
                MyGLRenderer.lightX = outPoint2[0] / outPoint2[3];
                MyGLRenderer.lightY = outPoint2[1] / outPoint2[3];
            }
            float e = MyGLRenderer.lightX;
            float f = MyGLRenderer.lightY;*/
            MyGLRenderer.offsetX += distanceX / 200;
            MyGLRenderer.offsetY -= distanceY / 200;
            MyGLRenderer.initTransformMatrix();
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            MyGLRenderer.scale = 1.5f;
            MyGLRenderer.offsetX = 0;
            MyGLRenderer.offsetY = 0;
            MyGLRenderer.initTransformMatrix();

            return true;
        }
    }

}