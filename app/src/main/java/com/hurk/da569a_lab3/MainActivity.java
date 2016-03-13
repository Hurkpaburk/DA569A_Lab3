package com.hurk.da569a_lab3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxSound:
                if (checked) {
                    // Put some meat on the sandwich
                }
                else {
                    // Remove the meat
                }
                break;
        }
    }
    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGame.class);
        startActivity(intent);
    }

    public void aboutGame(View view) {
        Intent intent = new Intent(this, AboutGame.class);
        startActivity(intent);
    }
}

