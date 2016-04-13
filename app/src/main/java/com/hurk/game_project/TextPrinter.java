package com.hurk.game_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/* Code base taken from course literature */
public class TextPrinter {

    private Canvas canvas;
    private Paint paint;
    private float x_pos, y_pos, spacing;

    public TextPrinter() {
        this(null);
    }

    public TextPrinter(Canvas can) {
        canvas = can;
        paint = new Paint();
        x_pos = 0;
        y_pos = 0;
        spacing = 22;
        setTextSize(18);
        setColor(Color.WHITE);
    }

    public void setCanvas(Canvas can) {
        canvas = can;
    }

    public void setLineSpacing(float space) {
        spacing = space;
    }

    public void setTextSize(float size) {
        paint.setTextSize(size);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void draw(String txt, float x, float y) {
        x_pos = x;
        y_pos = y;
        draw(txt);
    }

    public void draw(String txt) {
        canvas.drawText(txt, x_pos, y_pos, paint);
        y_pos += spacing;
    }
}

