package com.hurk.game_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutGame extends Activity {

    protected Intent intent;
    protected int points;
    protected TextView currScore, textViewHigh;
    private int highScore;
    private SharedPreferences scorePref;
    private String scoreFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        points = Math.max(0, intent.getIntExtra(MainActivity.POINTMESSAGE, 0));
        scorePref = getSharedPreferences(scoreFile, Activity.MODE_PRIVATE);
        highScore = scorePref.getInt(scoreFile, 0);

        if (points > highScore) {
            highScore = points;
            setHighScore(highScore);
        }
        setContentView(R.layout.activity_about_game);

        currScore = new TextView(this);
        currScore = (TextView) findViewById(R.id.currScore);
        currScore.setText(Integer.toString(points));

        textViewHigh = new TextView(this);
        textViewHigh = (TextView) findViewById(R.id.highScore);
        textViewHigh.setText(Integer.toString(highScore));
    }

    public void resetScore(View view) {
        highScore = 0;
        currScore.setText(Integer.toString(points));
        setHighScore(highScore);
        textViewHigh = new TextView(this);
        textViewHigh = (TextView) findViewById(R.id.highScore);
        textViewHigh.setText(Integer.toString(highScore));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setHighScore(int highScore) {
        scorePref = getSharedPreferences(scoreFile, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePref.edit();
        editor.putInt(scoreFile, highScore);
        editor.apply();
    }
}