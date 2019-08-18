package com.example.mgubb.alarmify;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity implements View.OnLongClickListener {
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
                    Toast.makeText(getApplicationContext(), "alarmTime: " + data.getLongExtra("alarmTime", 0), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "alarmTime: " + data.getStringExtra("alarmTime") + " alarmId: " + data.getStringExtra("alarmID"), Toast.LENGTH_LONG).show();

                    //Convert alarmtime to 12hour format
                    String standardTime = convertMilliToStandard(data.getLongExtra("alarmTime", 0));

                    //add alarm to list
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(standardTime);
                    Log.d("DB", "here");
                    databaseHelper.addData(standardTime, standardTime, standardTime);

                    //textView.setText(String.valueOf (data.getLongExtra("alarmTime", 0)));
                    linearLayout.addView(textView);
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
            //need to remake list probably change everything to list view later
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //adds alarm to to list
    public void addData(String alarmID, String alarmTime, String song){
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
        //int count = 0;
        Cursor data  = databaseHelper.getData();
        //String[] alarms = new String[(int)databaseHelper.numEntries()];
        Log.d("DB", "hereq");

        if(!databaseHelper.isEmpty()) {
            while (data.moveToNext()) {
                //alarms[count] = data.getString(3);
                //count++;


                TextView textView = new TextView(getApplicationContext());
                textView.setText(data.getString(3));
                textView.setTag(data.getString(3));
                textView.setOnLongClickListener(this);
                linearLayout.addView(textView);
            }
        }

        //ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, alarms);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
