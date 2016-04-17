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

    protected int bitMapHeight, bitMapWidth, bitMapXStart, bitMapYStart, points, canvasWidth, canvasHeight, update, startUpdate;
    protected MediaPlayer mp;
    float xDir, yDir, canvasRatio;
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

        evilMonster = new Sprite(this);
        evilMonsterImage = new Texture(this);
        if (!evilMonsterImage.loadFromAsset("blinking_green.png")) {
            super.fatalError("Error loading evilMonster image");
        }
        evilMonster.setTexture(evilMonsterImage);
    }

    public void draw() {
        if (!gameOver) {
            paint.setColor(Color.argb(240, 255, 154, 246));
            canvas = super.getCanvas();

            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
            canvasRatio = (float) canvasWidth / canvasHeight;
            bitMapWidth = clickMonsterImage.getWidth();
            bitMapHeight = clickMonsterImage.getHeight();

            while (newPosition) {
                bitMapXStart = rand.nextInt(canvasWidth - bitMapWidth);
                bitMapYStart = rand.nextInt(canvasHeight - bitMapHeight);
                newPosition = false;
                newPos.set(bitMapXStart, bitMapYStart);
                clickMonster.setPosition(newPos);

                xDir = rand.nextInt(points * 8) - points * 4;
                yDir = canvasRatio * (rand.nextInt(points * 8) - points * 4);
            }

            if (points == 0) {
                update = startUpdate;
            } else {
                update = Math.max(startUpdate / points, 1);
            }

            if (timer.stopWatch(update)) {
                Point move = new Point(clickMonster.getPosition().x + (int) xDir, clickMonster.getPosition().y + (int) yDir);
                clickMonster.setPosition(move);

            }

            clickMonster.draw();

        } else {
            textPrinter.setCanvas(canvas);
            textPrinter.setColor(Color.BLACK);
            textPrinter.setTextSize(100);
            textPrinter.draw("GAME OVER", canvasWidth / 2, canvasHeight / 2);
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
                    if ((touch.x >= clickMonster.getPosition().x && touch.x <= clickMonster.getPosition().x + bitMapWidth) &&
                            (touch.y >= clickMonster.getPosition().y && touch.y <= clickMonster.getPosition().y + bitMapHeight)) {
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



   /*

    protected boolean soundOn, newPosition;
    protected Intent intent;
    protected DrawView drawView;
    protected int bitMapHeight, bitMapWidth, bitMapXStart, bitMapYStart, points, canvasWidth, canvasHeight;
    protected MediaPlayer mp;
    protected Random rand = new Random();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent = getIntent();
        soundOn = intent.getBooleanExtra(MainActivity.MESSAGE, true);
        points = intent.getIntExtra(MainActivity.POINTMESSAGE, 0);
        newPosition = true;
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((touchX >= bitMapXStart && touchX <= bitMapXStart + bitMapWidth) &&
                        (touchY >= bitMapYStart && touchY <= bitMapYStart + bitMapHeight)) {
                    if (soundOn) {
                        mp.start();
                    } else {
                        // Do not play sound
                    }
                    points = points + 1;
                    newPosition = true; // new position
                } else {
                    // Do nothing
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

    public class DrawView extends SurfaceView implements Runnable {

        Thread gameloop = null;
        SurfaceHolder surface;
        volatile boolean running = false;
        AssetManager assetManager = null;
        BitmapFactory.Options options = null;
        Bitmap clickMonster = null;
        Texture clickMonsterImage = null;
        int frame = 0;


        public DrawView(Context context) {
            super(context);
            surface = getHolder();

            clickMonsterImage = new Texture(context);
            clickMonsterImage.loadFromAsset("blinking_green.png");
            clickMonster = clickMonsterImage.getBitmap();
            bitMapHeight = clickMonster.getHeight();
            bitMapWidth = clickMonster.getWidth();

        }

        public void resume() {
            running = true;
            gameloop = new Thread(this);
            gameloop.start();
        }

        public void pause() {
            running = false;
            try {
                gameloop.join();
            } catch (InterruptedException e) {
                }
        }

        @Override
        public void run() {
            while (running) {
                if (!surface.getSurface().isValid()) continue;

                Canvas canvas = surface.lockCanvas();
                canvas.drawARGB(255, 154, 246, 240);
                canvasWidth = canvas.getWidth();
                canvasHeight = canvas.getHeight();
                while (newPosition) {
                    bitMapXStart = rand.nextInt(canvasWidth - bitMapWidth);
                    bitMapYStart = rand.nextInt(canvasHeight - bitMapHeight);
                    newPosition = false;
                }
                canvas.drawBitmap(clickMonster, bitMapXStart, bitMapYStart, null);

                surface.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
*/