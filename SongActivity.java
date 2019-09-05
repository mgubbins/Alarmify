package com.example.mgubb.alarmify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class SongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        //Get display metrics object to retrieve screen info used to set popup window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //get width and height of screen
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //Set popup screen dimensions
        getWindow().setLayout((int)(width * .8), (int)(height * .8));
    }
}
