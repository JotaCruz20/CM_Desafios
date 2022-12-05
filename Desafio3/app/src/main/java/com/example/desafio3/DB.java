package com.example.desafio3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DB {
    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String TEMP_TABLE = "Temperature";
    public final static String HUM_TABLE = "Hum";
    public final static String TEMP_ID = "_id";
    public final static String HUM_ID = "_id";
    public final static String HUM_TS = "timestamp";
    public final static String HUM_VALUE = "percentage";
    public final static String TEMP_TS = "timestamp";
    public final static String TEMP_VALUE = "percentage";
    public final static String VALUES_TABLE = "ValuesThresh";
    public final static String VALUES_NAME = "name";
    public final static String VALUES_VALUE = "percentage";



    public DB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, 1 ,0);
    }

    public long createValues(String type, double value){
        ContentValues values = new ContentValues();
        values.put(VALUES_NAME,type);
        values.put(VALUES_VALUE,value);
        return database.insert(VALUES_TABLE, null, values);
    }

    public long updateValue(String type, double value){
        ContentValues values = new ContentValues();
        values.put(VALUES_VALUE,value);
        return database.update(VALUES_TABLE, values,VALUES_NAME + "= ?",  new String[]{type});
    }

    public double selectValue(String type) {
        String[] cols = new String[]{VALUES_VALUE};
        Cursor mCursor = database.query(true, VALUES_TABLE, cols, VALUES_NAME +" LIKE  ?"
                , new String[] {type+"%"}, null, null, null, null);
        double percentage = 0;
        if (mCursor != null) {
            while (mCursor.moveToNext()){
                percentage = mCursor.getDouble(mCursor.getColumnIndexOrThrow(VALUES_VALUE));
            }

        }
        return percentage;
    }

    public long createTemperatureRecord(Long timestamp, double percentage){
        ContentValues values = new ContentValues();
        values.put(TEMP_TS, timestamp);
        values.put(TEMP_VALUE, percentage);
        return database.insert(TEMP_TABLE, null, values);
    }

    public long createHumRecord(Long timestamp, double percentage){
        ContentValues values = new ContentValues();
        values.put(HUM_TS, timestamp);
        values.put(HUM_VALUE, percentage);
        return database.insert(HUM_TABLE, null, values);
    }

    public ArrayList<Temperature> selectTemp(long beforeTimestamp) {
        ArrayList<Temperature> temps = new ArrayList<>();
        String[] cols = new String[]{TEMP_TS, TEMP_VALUE};
        Cursor mCursor = database.query(true, TEMP_TABLE, cols, TEMP_TS +" > " + beforeTimestamp
                , null, null, null, TEMP_TS, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()){
                Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(TEMP_TS));
                double percentage = mCursor.getDouble(mCursor.getColumnIndexOrThrow(TEMP_VALUE));
                Temperature a = new Temperature(timestamp,percentage);
                temps.add(a);
            }

        }
        return temps;
    }

    public ArrayList<Humidity> selectHum(long beforeTimestamp) {
        ArrayList<Humidity> hums = new ArrayList<>();
        String[] cols = new String[]{HUM_TS, HUM_VALUE};
        Cursor mCursor = database.query(true, HUM_TABLE, cols,  HUM_TS +" > " + beforeTimestamp
                , null, null, null, HUM_TS, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()){
                Long timestamp = mCursor.getLong(mCursor.getColumnIndexOrThrow(HUM_TS));
                double percentage = mCursor.getDouble(mCursor.getColumnIndexOrThrow(HUM_VALUE));
                Humidity a = new Humidity(timestamp,percentage);
                hums.add(a);
            }

        }
        return hums;
    }

}
