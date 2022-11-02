package com.example.desafios2;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTask {
    final Executor executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onComplete();
    }

    public void executeAsyncDelete(String id, DB db, Callback callback){
        executor.execute(() -> {
            db.deleteNote(id);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncUpdate(String id, String title, String body, DB db, Callback callback){
        executor.execute(() -> {
            db.update(id,title,body);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncUpdateTitle(String id, String title, DB db, Callback callback){
        executor.execute(() -> {
            db.updateTitle(id,title);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncCreate(DB db, Callback callback){
        executor.execute(() -> {
            // QUALQUER COISA AQUI PARA CRIAR
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }
}
