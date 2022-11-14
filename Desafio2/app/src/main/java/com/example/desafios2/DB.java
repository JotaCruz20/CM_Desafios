package com.example.desafios2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DB {
    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String NOTE_TABLE = "Notes";
    public final static String TOPIC_TABLE = "Topic";
    public final static String NOTE_ID = "_id";
    public final static String NOTE_TITLE = "title";
    public final static String NOTE_BODY = "body";
    public final static String NOTE_ACCEPTED = "accepted";
    public final static String TOPIC_NAME = "name";

    /**
     * @param context
     */
    public DB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, 0 ,1);
    }


    public long createRecords(String title, String body, Boolean status) {
        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, title);
        values.put(NOTE_BODY, body);
        values.put(NOTE_ACCEPTED, status);
        return database.insert(NOTE_TABLE, null, values);
    }

    public long createTopic(String topic_name) {
        ContentValues values = new ContentValues();
        values.put(TOPIC_NAME, topic_name);
        return database.insert(TOPIC_TABLE, null, values);
    }

    public boolean deleteTopic(String topic_name) {
        return database.delete(TOPIC_TABLE, TOPIC_NAME + "='" + topic_name + "'", null) > 0;
    }

    public ArrayList<Topic> getTopics() {
        ArrayList<Topic> topics = new ArrayList<>();
        String[] cols = new String[]{TOPIC_NAME};
        Cursor mCursor = database.query(true, TOPIC_TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()){
                String name = mCursor.getString(mCursor.getColumnIndexOrThrow(TOPIC_NAME));
                topics.add(new Topic(name));
            }

        }
        return topics; // iterate to get each value.
    }

    public ArrayList<Note> selectNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String[] cols = new String[]{NOTE_ID, NOTE_TITLE, NOTE_BODY, NOTE_ACCEPTED};
        Cursor mCursor = database.query(true, NOTE_TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()){
                String id = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_ID));
                String title = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_TITLE));
                String body = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_BODY));
                String status = mCursor.getString(mCursor.getColumnIndexOrThrow(NOTE_ACCEPTED));

                Note a = new Note(id,title,body, status);
                Log.w(TAG, "1status: " + a.getStatus());
                notes.add(a);
            }

        }
        return notes; // iterate to get each value.
    }

    public Note selectNote(String id) {
        String[] cols = new String[]{NOTE_ID, NOTE_TITLE, NOTE_BODY, NOTE_ACCEPTED};
        String where = NOTE_ID + " = ?";
        String[] whereArgs = {id};

        Cursor cursor =  database.query(NOTE_TABLE,cols,where,whereArgs,null,null,null,null);

        cursor.moveToNext();
        return new Note(cursor.getString(cursor.getColumnIndexOrThrow(NOTE_ID)),cursor.getString(cursor.getColumnIndexOrThrow(NOTE_TITLE)),cursor.getString(cursor.getColumnIndexOrThrow(NOTE_BODY)),
                cursor.getString(cursor.getColumnIndexOrThrow(NOTE_ACCEPTED))); // iterate to get each value.
    }

    public boolean deleteNote(String id)
    {
        return database.delete(NOTE_TABLE, NOTE_ID + "=" + id, null) > 0;
    }

    public boolean update(String id,String title, String body)
    {
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TITLE, title);
        cv.put(NOTE_BODY,body);
        return database.update(NOTE_TABLE, cv,NOTE_ID + "= ?",  new String[]{id}) > 0;
    }

    public boolean updateTitle(String id,String title)
    {
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TITLE, title);
        return database.update(NOTE_TABLE, cv,NOTE_ID + "= ?",  new String[]{id}) > 0;
    }

    public boolean updateStatus(String id,Boolean status)
    {
        ContentValues cv = new ContentValues();
        cv.put(NOTE_ACCEPTED, status);
        return database.update(NOTE_TABLE, cv,NOTE_ID + "= ?",  new String[]{id}) > 0;
    }
}
