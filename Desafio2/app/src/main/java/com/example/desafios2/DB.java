package com.example.desafios2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DB {
    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String NOTE_TABLE = "Notes";
    public final static String NOTE_ID = "_id";
    public final static String NOTE_TITLE = "title";
    public final static String NOTE_BODY = "body";

    /**
     * @param context
     */
    public DB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long createRecords(String id, String title, String body) {
        ContentValues values = new ContentValues();
        values.put(NOTE_ID, id);
        values.put(NOTE_TITLE, title);
        values.put(NOTE_BODY, body);
        return database.insert(NOTE_TABLE, null, values);
    }

    public ArrayList<Note> selectRecords() {
        ArrayList<Note> notes = new ArrayList<>();
        String[] cols = new String[]{NOTE_ID, NOTE_TITLE, NOTE_BODY};
        Cursor mCursor = database.query(true, NOTE_TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            do {
                String id = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_ID));
                String title = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_TITLE));
                String body = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_BODY));
                notes.add(new Note(id,title,body));
            } while (mCursor.moveToNext());

        }
        return notes; // iterate to get each value.
    }

    public boolean deleteTitle(String id)
    {
        return database.delete(NOTE_TABLE, NOTE_ID + "=" + id, null) > 0;
    }
}
