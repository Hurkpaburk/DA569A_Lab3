package com.hurk.game_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/* Code base taken from course literature */
public class Sprite {
    public Point position;
    private Canvas canvas;
    private Engine engine;
    private Texture texture;
    private Paint paint;

    public Sprite(Canvas canvas) {
        canvas = null;
        texture = new Texture(engine);
        engine = engine;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        position = new Point(0, 0);
    }

    public void draw() {
        canvas = engine.getCanvas();
        canvas.drawBitmap(texture.getBitmap(), position.x, position.y, paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setPaint(Paint pnt) {
        paint = pnt;
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
}
