package com.hurk.game_project;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Float2;
import android.renderscript.Float3;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

import java.math.BigDecimal;

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
                canvas.drawText("ENGINE", x, 20, paintFont);
                canvas.drawText(toString(frameRate) + " FPS", x, 40, paintFont);
                canvas.drawText("Pauses: " + toString(pauseCount), x, 60, paintFont);
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

    public void drawText(String txt, int x, int y) {
        canvas.drawText(txt, x, y, paintFont);
    }

    public SurfaceView getView() {
        return surfaceView;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setFrameRate(int x) {
        prefFrameRate = x;
        sleepTime = 1000 / prefFrameRate;
    }

    public int getTouchInputs() {
        return numPoints;
    }

    public Point getTouchPoint(int idx) {
        if (idx > numPoints) {
            idx = numPoints;
        }
        return touchPoint[idx];
    }

    public void setDrawColor(int color) {
        paintDraw.setColor(color);
    }

    public void setTextColor(int color) {
        paintFont.setColor(color);
    }

    public void setTextSize(int size) {
        paintFont.setTextSize((float) size);
    }

    public void setTextStyle(FontStyles styles) {
        typeface = Typeface.create(Typeface.DEFAULT, styles.val);
        paintFont.setTypeface(typeface);
    }

    public void setScreenOrientation(ScreenModes screenModes) {
        setRequestedOrientation(screenModes.val);
    }

    public double round(double val) {
        return round(val, 2);
    }

    public double round(double val, int precision) {
        try {
            BigDecimal bd = new BigDecimal(val);
            BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
            return rounded.doubleValue();
        } catch (Exception e) {
            Log.e("ENGINE", "Round: Error rounding number");
        }
        return 0;
    }

    public String toString(int val) {
        return Integer.toString(val);
    }

    public String toString(float val) {
        return Float.toString(val);
    }

    public String toString(double val) {
        return Double.toString(val);
    }

    public String toString(Float2 val) {
        String string = "X:" + round(val.x) + "," + "Y:" + round(val.y);
        return string;
    }

    public String toString(Float3 val) {
        String string = "X:" + round(val.x) + "," + "Y:" + round(val.y) + "," + "Z:" + round(val.z);
        return string;
    }

    public enum FontStyles {
        NORMAL(Typeface.NORMAL),
        BOLD(Typeface.BOLD),
        ITALIC(Typeface.ITALIC),
        BOLD_ITALIC(Typeface.BOLD_ITALIC);
        int val;

        FontStyles(int type) {
            this.val = type;
        }
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
