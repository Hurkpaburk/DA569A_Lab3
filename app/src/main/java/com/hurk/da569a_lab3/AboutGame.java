package com.hurk.da569a_lab3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutGame extends Activity {

    protected Intent intent;
    protected int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        points = intent.getIntExtra(MainActivity.POINTMESSAGE,0);
        setContentView(R.layout.activity_about_game);
    }
}
