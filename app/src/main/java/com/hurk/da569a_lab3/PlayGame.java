package com.hurk.da569a_lab3;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.graphics.*;

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

    public class DrawView extends SurfaceView implements Runnable {
        // LÖS
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
