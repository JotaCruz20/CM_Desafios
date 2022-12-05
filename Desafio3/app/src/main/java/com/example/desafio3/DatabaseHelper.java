package com.example.desafio3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "store";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE_1 = "create table Temperature( _id integer primary key autoincrement , timestamp BIGINT, percentage DOUBLE);";
    private static final String DATABASE_CREATE_2 = "create table Hum( _id integer primary key autoincrement , timestamp BIGINT, percentage DOUBLE );";
    private static final String DATABASE_CREATE_3 = "create table ValuesThresh( name varchar primary key, percentage DOUBLE );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_1);
        database.execSQL(DATABASE_CREATE_2);
        database.execSQL(DATABASE_CREATE_3);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS Temperature");
        database.execSQL("DROP TABLE IF EXISTS Hum");
        database.execSQL("DROP TABLE IF EXISTS ValuesThresh");
        onCreate(database);
    }
}
