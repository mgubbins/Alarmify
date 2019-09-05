package com.example.mgubb.alarmify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity {
    static final int ALARM_REQUEST = 1;
    LinearLayout linearLayout;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    //ListView alarmListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //alarmListView = findViewById(R.id.alarmListView);

        //Button at top right of initial window
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When button is clicked, open Popup Window
                Intent i = new Intent(ScrollingActivity.this, Pop.class);
                startActivityForResult(i, ALARM_REQUEST);
                //startActivity(new Intent(ScrollingActivity.this,Pop.class));

                //Message that appears at bottom of screen
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        loadTable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case (ALARM_REQUEST) : {
                if(resultCode == Activity.RESULT_OK){
                    //Intent alarmReceiveIntent = getIntent();
                    //Bundle alarmReceiveExtras = alarmReceiveIntent.getExtras();
                    //Toast.makeText(getApplicationContext(), "alarmTime: " + data.getLongExtra("alarmTime", 0), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "alarmTime: " + data.getStringExtra("alarmTime") + " alarmId: " + data.getStringExtra("alarmID"), Toast.LENGTH_LONG).show();
                    //Long alarmTime =  alarmReceiveExtras.getLong("ALARM_TIME");
                    //Long alarmID = alarmReceiveExtras.getLong("ALARM_ID");
                    //Convert alarmtime to 12hour format
                    String standardTime = convertMilliToStandard(data.getLongExtra("alarmTime", 0));

                    Long alarmTime = data.getLongExtra("alarmTime", 0);
                    int alarmID = data.getIntExtra("alarmID", 0);
                    Log.d("DB", "alarmTime = " + alarmTime);
                    Log.d("DB", "alarmID on set = " + alarmID);
                    String alarmTimeString = alarmTime.toString();

                    //add alarm to list
                    Button button = new Button(getApplicationContext());
                    button.setText(standardTime);
                    Log.d("DB", "here");
                    databaseHelper.addData(alarmID, standardTime, standardTime);

                    //textView.setText(String.valueOf (data.getLongExtra("alarmTime", 0)));
                    linearLayout.addView(button);
                }
            }
        }
    }

    //Takes in time in millisecnods as a long and returns time in 12 hour format
    private String convertMilliToStandard(long timeMilli){
        Date date = new Date(timeMilli);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");

        return simpleDateFormat.format(date).toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            databaseHelper.deleteAllEntries();
            NestedScrollView nestedScrollView = findViewById(R.id.nestedCrollView);
            nestedScrollView.removeAllViews();
            //TextView textView = nestedScrollView.findViewWithTag("myTag0");
            ///nestedScrollView.removeView(textView);
            //need to remake list probably change everything to list view later
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //adds alarm to to list
    public void addData(int alarmID, String alarmTime, String song){
        Log.d("DB", "addData");
        boolean insertData = databaseHelper.addData(alarmID, alarmTime, song);

        if(insertData == true){
            Toast.makeText(getApplicationContext(), "Data inserted succesfully", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Failed Data insert", Toast.LENGTH_LONG).show();
        }

    }

    //loads all alarms into text views
    public void loadTable(){
        int count = 0;
        Cursor data  = databaseHelper.getData();
        //String[] alarms = new String[(int)databaseHelper.numEntries()];

        if(!databaseHelper.isEmpty()) {
            while (data.moveToNext()) {
                //alarms[count] = data.getString(3);
                count++;


                Button button = new Button(getApplicationContext());
                button.setText(data.getString(3));
                button.setId(count);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button button = (Button)view;
                        Log.d("DB", "Button with ID " + view.getId() + " pressed");
                        deleteAlarm(button.getText().toString());
                    }
                });

                /**TextView textView = new TextView(getApplicationContext());
                textView.setText(data.getString(3));
                textView.setTag("myTag" + count);
                textView.setOnLongClickListener(this);**/
                linearLayout.addView(button);
            }
        }

        //ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, alarms);
    }


    //delete specific alarm
    public void deleteAlarm(String alarmTime){
        //needs completed
        //this will retrieve string based on alarm time
        Cursor alarmCursor = databaseHelper.getAlarmId(alarmTime);
        Log.d("DB", "alarmCursor has " + alarmCursor.getCount() + " entries");
        alarmCursor.moveToNext();
        int id = alarmCursor.getInt(0);
        Log.d("DB", "alarmId on receive = " + id);

        Intent deleteIntent = new Intent(getApplicationContext(), Alarm.class);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        //this needs to look the same as intent that started alarm
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), id, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXSTOPPED HEREXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
        //this alarm manager passes the correct pi, but the alarm isn't canceled...gotta figure out why
        //the data base values are negative, but unique. This is probably due to integer wraparound. Might be okay to leave. haven't changed to double because the
        //  id that gets passed in the intent that starts the alarm has to be an int, not a double
        //all your alarm buttons say 6 now??? probably passing bad info to the database in a method I can't remember the name of rn. Just trace the info once the time
        //  selector popup closes
        am.cancel(pi);

        //this will delete table entry based on alarmtime. should change to alarm ID
        databaseHelper.deleteEntry(alarmTime);
    }
}
