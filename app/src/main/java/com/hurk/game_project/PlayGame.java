package com.hurk.game_project;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class PlayGame extends Engine {

    protected int clickMonsterHeight, clickMonsterWidth, clickMonsterXStart, clickMonsterYStart, points,
            evilMonsterHeight, evilMonsterWidth, canvasWidth, canvasHeight, update, startUpdate, xDir, yDir,
            newXpos, newYpos;
    protected MediaPlayer mp;
    float canvasRatio;
    TextPrinter textPrinter;
    private Paint paint;
    private Canvas canvas;
    private Timer timer;
    private Sprite clickMonster, evilMonster;
    private Texture clickMonsterImage, evilMonsterImage;
    private boolean soundOn, newPosition, gameOver;
    private Intent intent;
    private Point touch, newPos;
    private Random rand;

    public PlayGame() {
        paint = new Paint();
        canvas = null;
        clickMonster = null;
        evilMonster = null;
        clickMonsterImage = null;
        evilMonsterImage = null;
        timer = new Timer();
        touch = new Point();
        newPos = new Point();
        rand = new Random();
        newPosition = false;
        startUpdate = 1000;
        textPrinter = new TextPrinter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        soundOn = intent.getBooleanExtra(MainActivity.MESSAGE, true);
        points = 0;
        mp = MediaPlayer.create(this, R.raw.sound);
    }

    public void init() {
        super.setScreenOrientation(ScreenModes.PORTRAIT);
    }

    public void load() {
        clickMonster = new Sprite(this);
        clickMonsterImage = new Texture(this);
        if (!clickMonsterImage.loadFromAsset("blinking_green.png")) {
            super.fatalError("Error loading clickMonster image");
        }
        clickMonster.setTexture(clickMonsterImage);
        clickMonsterWidth = clickMonsterImage.getWidth();
        clickMonsterHeight = clickMonsterImage.getHeight();

        evilMonster = new Sprite(this);
        evilMonsterImage = new Texture(this);
        if (!evilMonsterImage.loadFromAsset("Evil_Monster.png")) {
            super.fatalError("Error loading evilMonster image");
        }
        evilMonster.setTexture(evilMonsterImage);
        evilMonsterWidth = evilMonsterImage.getWidth();
        evilMonsterHeight = evilMonsterImage.getHeight();
    }

    public void draw() {
        if (!gameOver) {
            getCanvas().drawColor(Color.argb(255, 154, 246, 240));
            canvas = super.getCanvas();

            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
            canvasRatio = (float) canvasWidth / canvasHeight;


            while (newPosition) {
                clickMonsterXStart = rand.nextInt(canvasWidth - clickMonsterWidth);
                clickMonsterYStart = rand.nextInt(canvasHeight - clickMonsterHeight);

                newPos.set(clickMonsterXStart, clickMonsterYStart);
                clickMonster.setPosition(newPos);

                xDir = rand.nextInt(points * 8) - points * 4;
                yDir = ((int) (canvasRatio * 10) * (rand.nextInt(points * 8) - points * 4)) / 10;

                newPosition = false;
            }

            if (points == 0) {
                update = startUpdate;
            } else {
                update = Math.max(startUpdate / points, 1);
            }


            if (timer.stopWatch(update)) {

                if ((clickMonster.getPosition().x + clickMonsterWidth >= canvasWidth || clickMonster.getPosition().x <= 0) ||
                        (clickMonster.getPosition().y + clickMonsterHeight >= canvasHeight || clickMonster.getPosition().y <= 0)) {
                    xDir = -xDir;
                    yDir = -yDir;
                } else {
                    // Do nothing
                }

                newXpos = clickMonster.getPosition().x + xDir;
                newYpos = clickMonster.getPosition().x + xDir;
                Point move = new Point(newXpos, newYpos);
                clickMonster.setPosition(move);

            }

            clickMonster.draw();

        } else {
            textPrinter.setCanvas(canvas);
            textPrinter.setColor(Color.BLACK);
            textPrinter.setTextSize(80);
            textPrinter.draw("GAME OVER", 20, canvasHeight / 2);
            textPrinter.setTextSize(50);
            textPrinter.draw("Press back to return to main menu", 20, canvasHeight / 2 + 30);
        }
    }

    public void update() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        touch.set((int) event.getX(), (int) event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (points < 0) {
                    gameOver = true;
                } else {
                    if ((touch.x >= clickMonster.getPosition().x && touch.x <= clickMonster.getPosition().x + clickMonsterWidth) &&
                            (touch.y >= clickMonster.getPosition().y && touch.y <= clickMonster.getPosition().y + clickMonsterHeight)) {
                        if (soundOn) {
                            mp.start();
                        } else {
                            // Do not play sound
                        }
                        points = points + 1;
                        newPosition = true; // new position
                    } else {
                        points = points - 1;
                    }
                }
                break;
            default:
                // Do nothing
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.POINTMESSAGE, points);
        Log.v("onBackPressed", Integer.toString(points));
        setResult(1, intent);
        finish();
    }
}
