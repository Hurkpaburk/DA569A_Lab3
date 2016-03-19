package com.hurk.da569a_lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    protected boolean soundOn;
    protected Intent intent;
    protected DrawView drawView;
    protected int bitMapHeight, bitMapWidth, bitMapXStart, bitMapYStart;
    protected MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent = getIntent();
        soundOn = intent.getBooleanExtra(MainActivity.message, true);
        drawView = new DrawView(this);
        setContentView(drawView);
        drawView.setOnTouchListener(this);
        mp = MediaPlayer.create(this, R.raw.sound);
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
            surface = getHolder();
            assetManager = context.getAssets();
            try {
                InputStream inputStream = assetManager.open("blinking_green.png");
                options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                monster = BitmapFactory.decodeStream(inputStream, null, options);
                bitMapHeight = monster.getHeight();
                bitMapWidth = monster.getWidth();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                } catch (InterruptedException e) {

                }
            }
        }

        @Override
        public void run() {
            while (running) {
                if (!surface.getSurface().isValid()) continue;

                Canvas canvas = surface.lockCanvas();
                canvas.drawARGB(255, 154, 246, 240);
                bitMapXStart = 400;
                bitMapYStart = 400;
                canvas.drawBitmap(monster, bitMapXStart, bitMapYStart, null);


                surface.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ( (touchX >= bitMapXStart && touchX <= bitMapXStart+bitMapWidth) &&
                        (touchY >= bitMapYStart && touchY <= bitMapYStart+bitMapHeight )) {
                    if (soundOn) {
                        mp.start();
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

}
