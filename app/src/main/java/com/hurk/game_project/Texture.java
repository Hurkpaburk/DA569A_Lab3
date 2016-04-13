package com.hurk.game_project;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/* Code base taken from course literature */
public class Texture {

    private Context context;
    private Bitmap bitmap;

    public Texture(Context con) {
        context = con;
        bitmap = null;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean loadFromAsset(String filename) {
        InputStream inputStream = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            inputStream = context.getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
