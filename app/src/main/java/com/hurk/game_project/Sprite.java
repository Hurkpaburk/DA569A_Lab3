package com.hurk.game_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

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
    private float rotation;


    public Sprite(Engine eng) {
        engine = eng;
        canvas = null;
        texture = new Texture(engine);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        position = new Point(0, 0);
        frameBitmap = null;
        frameCanvas = null;
        rotation = 0.0f;

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
        Point tmpPoint = null;

        if (position.x + texture.getWidth() >= canv.getWidth()) {
            tmpX = -tmpX;
        }
        if (position.y + texture.getHeight() >= canv.getHeight()) {
            tmpY = -tmpY;
        }
        if (position.x <= 0) {
            tmpX = -tmpX;
        }
        if (position.y <= 0) {
            tmpY = -tmpY;
        }
        tmpPoint = new Point(tmpX, tmpY);
        this.position = new Point(position.x + tmpX, position.y + tmpY);
        return tmpPoint;
    }
}
