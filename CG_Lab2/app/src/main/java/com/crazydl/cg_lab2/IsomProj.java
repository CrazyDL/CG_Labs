package com.crazydl.cg_lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;

public class IsomProj extends View {
    Paint pFigure = new Paint();
    Paint pText = new Paint();

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

/*public class IsomProj extends SurfaceView implements SurfaceHolder.Callback {

    Paint pFigure = new Paint();
    Paint pText = new Paint();

    private DrawThread drawThread;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private static float viewWidth, viewHeight;

    public IsomProj(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public IsomProj(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
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
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorCanvasBackground));
        pText.setTextSize(64);
        pText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("DO", viewWidth / 2, viewHeight / 2, pText);
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
    }*/
