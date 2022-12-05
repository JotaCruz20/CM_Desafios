package com.example.desafio3;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTask {
    final Executor executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onComplete();
        void onCompleteH(ArrayList<Humidity> response);
        void onCompleteT(ArrayList<Temperature> response);
        void onCompleteV(double value, String type);
    }

    public void executeAsyncGetHumidity(Long epoch, DB db, Callback callback){
        executor.execute(() -> {
            ArrayList<Humidity> notes = db.selectHum(epoch);
            handler.post(() -> {
                callback.onCompleteH(notes);
            });
        });
    }

    public void executeAsyncGetTemp(Long epoch, DB db, Callback callback){
        executor.execute(() -> {
            ArrayList<Temperature> notes = db.selectTemp(epoch);
            handler.post(() -> {
                callback.onCompleteT(notes);
            });
        });
    }

    public void executeAsyncCreateHum(Long timestamp, double value, DB db, Callback callback){
        executor.execute(() -> {
            db.createHumRecord(timestamp, value);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncCreateTemp(Long timestamp, double value, DB db, Callback callback){
        executor.execute(() -> {
            db.createTemperatureRecord(timestamp, value);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncCreateTresh(String type, double value, DB db, Callback callback){
        executor.execute(() -> {
            db.createValues(type, value);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncUpdateTresh(String type, double value, DB db, Callback callback){
        executor.execute(() -> {
            db.updateValue(type, value);
            handler.post(() -> {
                callback.onComplete();
            });
        });
    }

    public void executeAsyncGetTresh(String type, DB db, Callback callback){
        executor.execute(() -> {
            double v = db.selectValue(type);
            handler.post(() -> {
                callback.onCompleteV(v, type);
            });
        });
    }
}
