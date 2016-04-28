package com.hurk.game_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

/* Code base taken from course literature */
public class Sprite {
    public Point position;
    Canvas frameCanvas;
    private Canvas canvas;
    private Engine engine;
    private Texture texture;
    private Paint paint;
    private Bitmap frameBitmap;
    private int width, height;
    private Random rand;


    public Sprite(Engine eng) {
        engine = eng;
        canvas = null;
        texture = new Texture(engine);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        position = new Point(0, 0);
        frameBitmap = null;
        frameCanvas = null;
        rand = new Random(System.currentTimeMillis());
    }

    public void draw() {
        canvas = engine.getCanvas();

        if (width == 0 || height == 0) {
            width = texture.getBitmap().getWidth();
            height = texture.getBitmap().getHeight();
        }

        if (frameBitmap == null) {
            frameBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            frameCanvas = new Canvas(frameBitmap);
        }
        canvas.drawBitmap(texture.getBitmap(), position.x, position.y, paint);

    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture txt) {
        texture = txt;
    }


    public Point edgeDetection(Point dir, Canvas canv) {
        int tmpX = dir.x;
        int tmpY = dir.y;
        Point tmpPoint;

        if (position.x + texture.getWidth() + tmpX > canv.getWidth()) {
            tmpX = -tmpX;
        }
        if (position.y + texture.getHeight() + tmpY >= canv.getHeight()) {
            tmpY = -tmpY;
        }
        if (position.x + tmpX <= 0) {
            tmpX = -tmpX;
        }
        if (position.y + tmpY <= 0) {
            tmpY = -tmpY;
        }
        position = new Point(position.x + tmpX, position.y + tmpY);
        tmpPoint = new Point(tmpX, tmpY);
        return tmpPoint;
    }

    public boolean collision(Sprite sprite) {
        if ((position.x <= sprite.getPosition().x + sprite.getTexture().getWidth()) && (sprite.getPosition().x <= position.x + texture.getWidth())) {
            if ((position.y <= sprite.getPosition().y + sprite.getTexture().getHeight()) && (sprite.getPosition().y <= position.y + texture.getHeight())) {
                return true; // Collision
            }
        }
        if ((position.y <= sprite.getPosition().y + sprite.getTexture().getHeight()) && (sprite.getPosition().y <= position.y + texture.getHeight())) {
            if ((position.x <= sprite.getPosition().x + sprite.getTexture().getWidth()) && (sprite.getPosition().x <= position.x + texture.getWidth())) {
                return true; // Collision
            }
        }
        return false;
    }

    public Point setNewRandPosition(Canvas can) {
        int tmpx = rand.nextInt(can.getWidth() - texture.getWidth());
        int tmpy = rand.nextInt(can.getHeight() - texture.getHeight());
        this.position = new Point(tmpx, tmpy);
        return this.position;
    }
}
