package com.hurk.game_project;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class PlayGame extends Engine {

    protected int points, canvasWidth, canvasHeight, update, startUpdate, xDir, yDir, xDirEvil, yDirEvil;
    protected MediaPlayer mp;
    int canvasRatio;
    TextPrinter textPrinter;
    private Canvas canvas;
    private Timer timer;
    private Sprite clickMonster, evilMonster;
    private Texture clickMonsterImage, evilMonsterImage;
    private boolean soundOn, newPosition, gameOver, level, firstDraw;
    private Intent intent;
    private Point touch;
    private Random rand;

    public PlayGame() {
        canvas = null;
        clickMonster = null;
        evilMonster = null;
        clickMonsterImage = null;
        evilMonsterImage = null;
        timer = new Timer();
        touch = new Point();
        rand = new Random();
        newPosition = true;
        firstDraw = true;
        startUpdate = 1000;
        textPrinter = new TextPrinter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        Bundle extras = intent.getExtras();
        soundOn = extras.getBoolean(MainActivity.MESSAGE, true);
        level = extras.getBoolean(MainActivity.LEVELMESS, true);
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

            evilMonster = new Sprite(this);
        evilMonsterImage = new Texture(this);
        if (!evilMonsterImage.loadFromAsset("evil_monster.png")) {
            super.fatalError("Error loading evilMonster image");
        }
        evilMonster.setTexture(evilMonsterImage);
    }

    public void draw() {
        if (!gameOver) {
            getCanvas().drawColor(Color.argb(255, 154, 246, 240));
            canvas = getCanvas();

            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
            canvasRatio = Math.round(canvasHeight / canvasWidth);

            if (level && firstDraw) {
                evilMonster.setNewRandPosition(canvas);
                firstDraw = false;
            }

            while (newPosition) {
                clickMonster.setNewRandPosition(canvas);

                if (level) {
                    while (clickMonster.collision(evilMonster)) {
                        clickMonster.setNewRandPosition(canvas);
                    }
                } else {
                    // Do Nothing
                }

                if (points > 0) {
                    xDir = rand.nextInt(points * 2) - points;
                    yDir = canvasRatio * (rand.nextInt(points * 2) - points);

                    if (level) {
                        xDirEvil = rand.nextInt(points * 8) - points * 4;
                        yDirEvil = ((int) (canvasRatio * 10) * (rand.nextInt(points * 8) - points * 4)) / 10;
                    }
                }
                newPosition = false;
            }

            if (points == 0) {
                update = startUpdate;
            } else {
                update = Math.max(startUpdate / points, 1);
            }
            update = 10;

            if (timer.stopWatch(update)) {

                xDir = clickMonster.edgeDetection(new Point(xDir, yDir), canvas).x;
                yDir = clickMonster.edgeDetection(new Point(xDir, yDir), canvas).y;

                if (level) {
                    xDirEvil = evilMonster.edgeDetection(new Point(xDirEvil, yDirEvil), canvas).x;
                    yDirEvil = evilMonster.edgeDetection(new Point(xDirEvil, yDirEvil), canvas).y;
                    if (clickMonster.collision(evilMonster)) {
                        gameOver = true;
                    }
                }
            }

            clickMonster.draw();

            if (level) {
                evilMonster.draw();
            }

        } else {
            gameOver();
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
                    if ((touch.x >= clickMonster.getPosition().x && touch.x <= clickMonster.getPosition().x + clickMonster.getTexture().getWidth()) &&
                            (touch.y >= clickMonster.getPosition().y && touch.y <= clickMonster.getPosition().y + clickMonster.getTexture().getHeight())) {
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

    private void gameOver() {
        textPrinter.setCanvas(canvas);
        textPrinter.setColor(Color.BLACK);
        textPrinter.setTextSize(80);
        textPrinter.draw("GAME OVER", 20, canvasHeight / 2);
        textPrinter.draw("", 20, canvasHeight / 2);
        textPrinter.setTextSize(50);
        textPrinter.draw("Press back to return to main menu", 20, canvasHeight / 2 + 30);
    }
}
