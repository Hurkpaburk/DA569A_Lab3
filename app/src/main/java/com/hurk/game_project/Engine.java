package com.hurk.game_project;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View.OnTouchListener;
import android.view.Window;


public abstract class Engine extends Activity implements Runnable, OnTouchListener {
    private SurfaceView surfaceView;
    private Canvas canvas;
    private Thread thread;
    private boolean running, paused;
    private int pauseCount, numPoints;
    private Paint paintDraw, paintFont;
    private Typeface typeface;
    private Point touchPoint;
    private long prefFrameRate, sleepTime;

    public Engine

    {
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
        touchPoint = new Point(0, 0);

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
}
