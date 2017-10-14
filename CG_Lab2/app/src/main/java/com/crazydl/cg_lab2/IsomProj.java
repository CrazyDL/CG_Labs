package com.crazydl.cg_lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class IsomProj extends SurfaceView implements SurfaceHolder.Callback {
    Paint pFigure = new Paint();

    private DrawThread drawThread;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float viewWidth, viewHeight;

    public IsomProj(Context context) {
        super(context);
    }

    public IsomProj(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        viewHeight = i2;
        viewWidth = i1;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry){
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException ignored){}
        }
    }

    private void DrawFigure(Canvas canvas){

    }

    private class DrawThread extends Thread{
        private SurfaceHolder surfaceHolder;
        private boolean running = false;

        DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running){
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas == null){
                        continue;
                    }
                    DrawFigure(canvas);
                } finally {
                    if(canvas != null){
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
