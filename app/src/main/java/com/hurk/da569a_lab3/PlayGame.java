package com.hurk.da569a_lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.graphics.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class PlayGame extends Activity implements OnTouchListener {

    Point touch = new Point(0, 0);
    protected boolean soundOn;
    protected Intent intent;
    DrawView drawView;
    SoundPool soundPool = null;
    HashMap<Integer, Integer> soundPoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent = getIntent();
        soundOn = intent.getBooleanExtra(MainActivity.message, true);
        drawView = new DrawView(this);
        setContentView(drawView);

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap.put(0, soundPool.load(this, R.raw.sound, 1));
    }

    @Override
    public void onResume() {
        super.onResume();
        drawView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        drawView.pause();
    }

    public class DrawView extends SurfaceView implements Runnable {

        Thread gameloop = null;
        SurfaceHolder surface;
        volatile boolean running = false;
        AssetManager assetManager = null;
        BitmapFactory.Options options = null;
        Bitmap monster = null;
        int frame = 0;


        public DrawView(Context context) {
            super(context);
            try {
                surface = getHolder();
                assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("blinking_green.png");
                options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                monster = BitmapFactory.decodeStream(inputStream, null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // CHANGE / REMOVE THIS!!!? ???
        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawARGB(255, 154, 246, 240);
            canvas.drawBitmap(monster, 400, 400, null);
        }


        public void resume() {
            running = true;
            gameloop = new Thread(this);
            gameloop.start();
        }

        public void pause() {
            running = false;
            while (true) {
                try {
                    gameloop.join();
                }
                catch (InterruptedException e) {

                }
            }
        }

        @Override
        public void run() {
            // SIDAN 148 kombinerat med onDraw ovan
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rect.contains(touchX, touchY)) { //Do something smart
                    if (soundOn) {
                        playSound();
                    } else {
                        // Do not play sound
                    }
                    // Rita om Skärmen DrawView
                } else {
                    // Do nothing
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void playSound() {
        soundPool.play(0,1f,1f,1,1,1f);
    }
}
