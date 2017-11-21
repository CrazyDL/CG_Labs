package com.crazydl.cg_lab4;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

class MyGLSurfaceView extends GLSurfaceView {

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    public MyGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyGLRenderer(context));

        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
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
            float newScale = MyGLRenderer.scale * scaleGestureDetector.getScaleFactor();
            if (newScale > 0.4 && newScale < 2) {
                MyGLRenderer.scale = newScale;
                MyGLRenderer.initTransformMatrix();
            }
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            MyGLRenderer.offsetX -= distanceX / 200;
            MyGLRenderer.offsetY -= distanceY / 200;
            MyGLRenderer.initTransformMatrix();
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            MyGLRenderer.scale = 1f;
            MyGLRenderer.offsetX = 0;
            MyGLRenderer.offsetY = 0;
            MyGLRenderer.initTransformMatrix();

            return true;
        }
    }

}