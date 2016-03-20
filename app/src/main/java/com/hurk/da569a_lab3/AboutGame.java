package com.hurk.da569a_lab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutGame extends Activity {

    protected Intent intent;
    protected int points;
    protected TextView currScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        points = intent.getIntExtra(MainActivity.POINTMESSAGE, 0);
        setContentView(R.layout.activity_about_game);

        currScore = new TextView(this);

        currScore = (TextView) findViewById(R.id.currScore);
        currScore.setText(Integer.toString(points));
    }

    public void resetScore(View view) {
        points = 0;
        currScore.setText(Integer.toString(points));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.POINTMESSAGE, points);
        setResult(1, intent);
        finish();
    }
}