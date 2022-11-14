package com.example.desafios2;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTask {
    final Executor executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onCompleteNote(Note note);
        void onCompleteNotes(ArrayList<Note> notes, boolean isFilter);
    }

    public void executeAsyncDelete(String id, DB db, Callback callback){
        executor.execute(() -> {
            db.deleteNote(id);
            ArrayList<Note> notes = db.selectNotes();
            handler.post(() -> {
                callback.onCompleteNotes(notes, false);
            });
        });
    }

    public void executeAsyncSelectNote(String id, DB db, Callback callback){
        executor.execute(() -> {
            Note note = db.selectNote(id);
            handler.post(() -> {
                callback.onCompleteNote(note);
            });
        });
    }

    public void executeAsyncSelectNotes(DB db, Callback callback, boolean isFilter){
        executor.execute(() -> {
            ArrayList<Note> notes = db.selectNotes();
            handler.post(() -> {
                callback.onCompleteNotes(notes, isFilter);
            });
        });
    }

    public void executeAsyncUpdate(String id, String title, String body, DB db, Callback callback){
        executor.execute(() -> {
            db.update(id,title,body);
            ArrayList<Note> notes = db.selectNotes();
            handler.post(() -> {
                callback.onCompleteNotes(notes, false);
            });
        });
    }

    public void executeAsyncUpdateTitle(String id, String title, DB db, Callback callback){
        executor.execute(() -> {
            db.updateTitle(id,title);
            ArrayList<Note> notes = db.selectNotes();
            handler.post(() -> {
                callback.onCompleteNotes(notes, false);
            });
        });
    }

    public void executeAsyncCreate(DB db, String title, String body, Callback callback){
        executor.execute(() -> {
            db.createRecords(title, body);
            ArrayList<Note> notes = db.selectNotes();
            handler.post(() -> {
                callback.onCompleteNotes(notes, false);
            });
        });
    }
}
