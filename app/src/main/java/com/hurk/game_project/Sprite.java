package com.hurk.game_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.renderscript.Float2;

/* Code base taken from course literature */
public class Sprite {
    public Point position;
    Canvas frameCanvas;
    private Canvas canvas;
    private Engine engine;
    private Texture texture;
    private Paint paint;
    private Matrix mat_translate, mat_scale, mat_rotate, matrix;
    private Bitmap frameBitmap;
    private int width, height;
    private Float2 scale;
    private float rotation;

    public Sprite(Engine eng) {
        engine = eng;
        canvas = null;
        texture = new Texture(engine);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        position = new Point(0, 0);
        mat_translate = new Matrix();
        mat_rotate = new Matrix();
        mat_scale = new Matrix();
        matrix = new Matrix();
        frameBitmap = null;
        frameCanvas = null;
        scale = new Float2(1.0f, 1.0f);
        rotation = 0.0f;

    }

    public void draw() {
        canvas = engine.getCanvas();

        if (width == 0 || height == 0) {
            width = texture.getBitmap().getWidth();
            height = texture.getBitmap().getHeight();
        }
        //  canvas.drawBitmap(texture.getBitmap(), position.x, position.y, paint);

        if (frameBitmap == null) {
            frameBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            frameCanvas = new Canvas(frameBitmap);
        }
        canvas.drawBitmap(texture.getBitmap(), position.x, position.y, paint);

        mat_scale = new Matrix();
        mat_scale.setScale(scale.x, scale.y);
        mat_rotate = new Matrix();
        mat_rotate.setRotate((float) Math.toDegrees(rotation));
        mat_translate = new Matrix();
        mat_translate.setTranslate(position.x, position.y);
        matrix = new Matrix();
        matrix.postConcat(mat_scale);
        matrix.postConcat(mat_rotate);
        matrix.postConcat(mat_translate);

        canvas.drawBitmap(frameBitmap, matrix, paint);
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

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rot) {
        rotation = rot;
    }
}
