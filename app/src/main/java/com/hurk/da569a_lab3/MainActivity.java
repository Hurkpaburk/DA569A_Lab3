package com.hurk.da569a_lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE = "MESSAGE";
    public static final String POINTMESSAGE = "points";
    protected boolean soundOn = true;
    protected int points;
    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  points = 10;

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxSound:
                if (checked) {
                    soundOn = true;
                }
                else {
                    soundOn = false;
                }
                break;
        }
    }

    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGame.class);
        intent.putExtra(MESSAGE, soundOn);
        intent.putExtra(POINTMESSAGE,points);
        startActivityForResult(intent, 1);
    }

    public void aboutGame(View view) {
        Intent intent = new Intent(this, AboutGame.class);
        intent.putExtra(POINTMESSAGE, points);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                points = data.getIntExtra(POINTMESSAGE, 0);
                Log.v("onActivityResult", Integer.toString(points));
            }
        }
    }
}

