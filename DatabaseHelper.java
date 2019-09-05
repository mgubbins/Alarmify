package com.example.mgubb.alarmify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by mgubb on 8/17/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "alarm_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "alarmID";
    private static final String COL3 = "alarmTime";
    private static final String COL4 = "song";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " , " + COL3 + ", " + COL4 + ")";
        sqLiteDatabase.execSQL(createTable);
        Log.d("DB", "Data base created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(int alarmID, String alarmTime, String song){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, alarmID);
        contentValues.put(COL3, alarmTime);
        contentValues.put(COL4, song);

        Log.d(TAG, "addData: Adding: " + alarmID + ", " + alarmTime + ", " + song + " to " + TABLE_NAME);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return true;
        }else{
            return false;
        }
    }

    public long numEntries(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);
        sqLiteDatabase.close();
        return  count;
    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if(!isEmpty()) {
            String query = "SELECT * FROM " + TABLE_NAME;
            Cursor data = sqLiteDatabase.rawQuery(query, null);
            return data;
        }else{
            Cursor data = sqLiteDatabase.rawQuery("", null);
            return data;
        }
    }

    public Cursor getAlarmId(String alarmTime){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if(!isEmpty()) {
            String query = "SELECT " + COL2 + " FROM " + TABLE_NAME + " WHERE " + COL3 + " = '" + alarmTime + "'";
            Cursor data = sqLiteDatabase.rawQuery(query, null);
            return data;
        }else{
            Cursor data = sqLiteDatabase.rawQuery("", null);
            return data;
        }
    }

    public void deleteAllEntries(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE EXISTS (SELECT * FROM " + TABLE_NAME + ")");
    }

    public boolean isEmpty(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if(DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME) == 0){
            return true;
        }else{
            return false;
        }
    }

    public void deleteEntry(String alarmTime){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        SQLiteDatabase sqLiteDatabase1 = this.getReadableDatabase();
        Cursor data = sqLiteDatabase1.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        data.moveToNext();



        Log.d("DB", "input = " + alarmTime + " database entry = " + data.getString(3));
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL3 + " = '" + alarmTime + "'");
    }
}
