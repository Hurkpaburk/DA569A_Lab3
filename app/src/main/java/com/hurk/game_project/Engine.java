package com.hurk.game_project;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

/* Code base taken from course literature */
public abstract class Engine extends Activity implements Runnable, OnTouchListener {
    private SurfaceView surfaceView;
    private Canvas canvas;
    private Thread thread;
    private boolean running, paused;
    private int pauseCount, numPoints;
    private Paint paintDraw, paintFont;
    private Typeface typeface;
    private Point[] touchPoint;
    private long prefFrameRate, sleepTime;

    public Engine() {
        surfaceView = null;
        canvas = null;
        thread = null;
        paintDraw = null;
        paintFont = null; // Remove
        typeface = null;
        running = false;
        paused = false;
        numPoints = 0;
        prefFrameRate = 40;
        sleepTime = 1000 / prefFrameRate;
        pauseCount = 0;
    }

    public abstract void init();

    public abstract void load();

    public abstract void draw();

    public abstract void update();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init();

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceView.setOnTouchListener(this);

        int numTouchPoints = 5;
        touchPoint = new Point[numTouchPoints];
        for (int x = 0; x < numTouchPoints; x++) {
            touchPoint[x] = new Point(0, 0);
        }

        paintDraw = new Paint();
        paintDraw.setColor(Color.WHITE);
        paintFont = new Paint(); // Remove
        paintFont.setColor(Color.WHITE); // Remove
        paintFont.setTextSize(24); // Remove

        load();

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Timer frameTimer = new Timer();
        int frameCount = 0;
        int frameRate = 0;
        long startTime = 0;
        long timeDiff = 0;

        while (running) {
            if (paused) {
                continue;
            }

            frameCount = frameCount + 1;
            startTime = frameTimer.getElapsed();

            if (frameTimer.stopWatch(1000)) {
                frameRate = frameCount;
                frameCount = 0;


                numPoints = 0;
            }
            update();

            if (beginDrawing()) {
                canvas.drawColor(Color.BLUE);
                draw();
                int x = canvas.getWidth() - 150;
                endDrawing();
            }

            timeDiff = frameTimer.getElapsed() - startTime;
            long updatePeriod = sleepTime - timeDiff;

            if (updatePeriod > 0) {
                try {
                    Thread.sleep(updatePeriod);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
        System.exit(RESULT_OK);
    }

    private boolean beginDrawing() {
        if (!surfaceView.getHolder().getSurface().isValid()) {
            return false;
        }
        canvas = surfaceView.getHolder().lockCanvas();
        return true;
    }

    private void endDrawing() {
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void onResume() {
        super.onResume();
        paused = false;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
        pauseCount++;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        numPoints = motionEvent.getPointerCount();

        for (int x = 0; x < numPoints; x++) {
            touchPoint[x].x = (int) motionEvent.getX(x);
            touchPoint[x].y = (int) motionEvent.getY(x);
        }
        return true;
    }

    public void fatalError(String msg) {
        Log.e("FATAL ERROR", msg);
        System.exit(0);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setScreenOrientation(ScreenModes screenModes) {
        setRequestedOrientation(screenModes.val);
    }

    public enum ScreenModes {
        LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
        PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int val;

        ScreenModes(int mode) {
            this.val = mode;
        }
    }

}
