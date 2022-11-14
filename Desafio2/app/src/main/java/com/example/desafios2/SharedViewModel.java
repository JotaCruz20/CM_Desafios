package com.example.desafios2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;


public class SharedViewModel extends AndroidViewModel {
    private MutableLiveData<String> noteId = new MutableLiveData<>();
    private MutableLiveData<String> newFromTopic = new MutableLiveData<>();
    private final MutableLiveData<DB> db = new MutableLiveData<>();
    private final AsyncTask asyncTask = new AsyncTask();
    private MQTTHelper helper;
    private String name = "name";

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getNewFromTopic() {
        if (newFromTopic == null) {
            newFromTopic = new MutableLiveData<String>();
        }
        return newFromTopic;
    }

    public void setDB(DB db){
        this.db.setValue(db);
    }

    public void updateNote(String id,String title, String body, AsyncTask.Callback callback){
        asyncTask.executeAsyncUpdate(id,title,body,this.db.getValue(),callback);
    }

    public void insertNote(String title, String body) {
        this.db.getValue().createRecords(title, body);
    }

    public void createNote(String title, String body, AsyncTask.Callback callback){
        asyncTask.executeAsyncCreate(this.db.getValue(), title, body,callback);
    }

    public String getNoteId() {
        return this.noteId.getValue();
    }

    public void setNoteId(String noteId) {
        this.noteId.setValue(noteId);
    }

    public void updateTitleNote(String id, String title, AsyncTask.Callback callback){
        asyncTask.executeAsyncUpdateTitle(id,title,this.db.getValue(),callback);
    }

    public void deleteNote(String id, AsyncTask.Callback callback){
        asyncTask.executeAsyncDelete(id,this.db.getValue(),callback);
    }

    public void getNotes(AsyncTask.Callback callback, boolean isFilter){
        asyncTask.executeAsyncSelectNotes(this.db.getValue(),callback, isFilter);
    }

    public void getNote(String id,AsyncTask.Callback callback){
        asyncTask.executeAsyncSelectNote(id,this.db.getValue(),callback);
    }

    public Note getNote(String id){
        try {
            return this.db.getValue().selectNote(id);
        } catch (NullPointerException e) {
            return null;
        }
    }


    public long insertTopic(String topic_name){
        return Objects.requireNonNull(this.db.getValue()).createTopic(topic_name);
    }

    public boolean deleteTopic(String topic_name){
        return Objects.requireNonNull(this.db.getValue()).deleteTopic(topic_name);
    }

    public ArrayList<Topic> getTopics(){
        return this.db.getValue().getTopics();
    }

    // -------------------- MQTT -----------------------------

    public MQTTHelper getHelper() {
        return helper;
    }

    public void connection() {
        helper = new MQTTHelper(getApplication().getApplicationContext(), name);

        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                ArrayList<Topic> topics = Objects.requireNonNull(db.getValue()).getTopics();

                for (Topic topic: topics) {
                    helper.subscribeToTopic(topic.getTopic_name());
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                //disconnect
            }

            @Override
            // here!
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String[] arrOfStr = message.toString().split("\\|", 2);
                String id = arrOfStr[0];

                //if (getNote(id) == null) {
                    getNewFromTopic().setValue(arrOfStr[1]);
                //}
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
        helper.connect();
    }

    public void publishNote(MQTTHelper client, String msg, int qos, String topic, boolean init)
    {
        try {
            byte[] encodedPayload;

            if (init)
                msg = client.getName().toUpperCase() + ":" + msg;

            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(qos);

            client.mqttAndroidClient.publish(topic, message);
        } catch (Exception e){
            Log.w(TAG, "MQTT Exception");
        }
    }
}
