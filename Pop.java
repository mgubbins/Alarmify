package com.example.mgubb.alarmify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by mgubb on 8/17/2019.
 */

public class Pop extends Activity{
    TimePicker timePicker;
    Button btnAlarm, btnSong;
    EditText timeBox;
    //Opens when new alarm button is clicked
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        //Get display metrics object to retrieve screen info used to set popup window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //get width and height of screen
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //Set popup screen dimensions
        getWindow().setLayout((int)(width * .8), (int)(height * .8));

        //instantiate timePicker
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        //instantiate buttons
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnSong = (Button) findViewById(R.id.btnSong);

        //instantiate editText
        timeBox = (EditText) findViewById(R.id.timeBox);

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Pop.this, Alarm.class);
                //Intent to pass back alarm time
                Intent alarmTimeIntent = new Intent();
                Intent alarmIdIntent = new Intent();
                //PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

                //Create AlarmManager and Calendar object
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                Calendar alarmTime = Calendar.getInstance();

                //Set Calendar object time according to hour and minute from time picker
                alarmTime.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                alarmTime.set(Calendar.MINUTE, timePicker.getMinute());
                alarmTime.set(Calendar.SECOND, 0);

                //ID created from casting alarmTime to int for referencing alarm later
                final int _id = (int) alarmTime.getTimeInMillis();
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), _id, i, PendingIntent.FLAG_ONE_SHOT);

                //non-crucial toast message that displays current time in milli and alarmtime in milli
                //Toast.makeText(getApplicationContext(), "Current Time:" + Calendar.getInstance().getTimeInMillis()  + "AlarmTime:" + alarmTime.getTimeInMillis(), Toast.LENGTH_LONG).show();

                //setting AlarmManager according to time picker time
                am.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pi);

                //Passing alarmTime to ScrollingActivity
                alarmTimeIntent.putExtra("alarmTime", alarmTime.getTimeInMillis());
                setResult(Activity.RESULT_OK, alarmTimeIntent);

                //Passing alarmTime to ScrollingActivity
                //alarmIdIntent.putExtra("alarmID" , String.valueOf(_id));
                //setResult(Activity.RESULT_OK, alarmTimeIntent);

                finish();
            }
        });
    }
}
