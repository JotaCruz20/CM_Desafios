package com.example.desafio3;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;

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
    private MutableLiveData<String> newFromTopicTemp = new MutableLiveData<>();
    private MutableLiveData<String> newFromTopicHum = new MutableLiveData<>();
    private final MutableLiveData<DB> db = new MutableLiveData<>();
    private MQTTHelper helper;
    private MutableLiveData<Double> treshTemp = new MutableLiveData<>();
    private MutableLiveData<Double> treshHum = new MutableLiveData<>();
    private final AsyncTask asyncTask = new AsyncTask();

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getNewFromTopicTemp() {
        if (newFromTopicTemp == null) {
            newFromTopicTemp = new MutableLiveData<String>();
        }
        return newFromTopicTemp;
    }

    public MutableLiveData<String> getNewFromTopicHum() {
        if (newFromTopicHum == null) {
            newFromTopicHum = new MutableLiveData<String>();
        }
        return newFromTopicHum;
    }

    public void setDB(DB db){
        this.db.setValue(db);
    }

    public Double getTreshTemp() {
        return treshTemp.getValue();
    }

    public void setTreshTemp(Double treshTemp) {
        this.treshTemp.setValue(treshTemp);
    }

    public Double getTreshHum() {
        return treshHum.getValue();
    }

    public void setTreshHum(Double treshHum) {
        this.treshHum.setValue(treshHum);
    }

    public void getHumidity(long epoch, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncGetHumidity(epoch, this.db.getValue(), callback);
    }

    public void getTemperature(long epoch, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncGetTemp(epoch, this.db.getValue(), callback);
    }

    public void createHumRecord(Long ts, double value, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncCreateHum(ts, value, this.db.getValue(), callback);
    }

    public void createTempRecord(Long ts, double value, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncCreateTemp(ts, value, this.db.getValue(), callback);
    }

    public void updateTreshValue(String type, double value, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncCreateTresh(type, value, this.db.getValue(), callback);
        if(type.equals("temp")){
            this.setTreshTemp(value);
        }
        else{
            this.setTreshHum(value);
        }
    }

    public void createTreshValue(String type, double value, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncUpdateTresh(type, value, this.db.getValue(), callback);
    }

    public void getTreshValue(String type, AsyncTask.Callback callback){
        this.asyncTask.executeAsyncGetTresh(type, this.db.getValue(), callback);
    }


    // -------------------- MQTT -----------------------------

    public MQTTHelper getHelper() {
        return helper;
    }

    public void connection() {
        helper = new MQTTHelper(getApplication().getApplicationContext());

        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                helper.subscribeToTopic("TempCM2223");
                helper.subscribeToTopic("HumCM2223");
            }

            @Override
            public void connectionLost(Throwable cause) {
                //disconnect
            }

            @Override
            // here!
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String messageString = message.toString();

                if(topic.equals("TempCM2223")){
                    getNewFromTopicTemp().setValue(messageString);
                }
                else if(topic.equals("HumCM2223")){
                    getNewFromTopicHum().setValue(messageString);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
        helper.connect();
    }

    public void changeTemp(MQTTHelper client, int qos){
        try{
            byte[] encodedPayload;

            String msg = "tempSwitch";

            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(qos);

            client.mqttAndroidClient.publish("ledCM2223", message);
        } catch (Exception e){
            Log.w(TAG, "MQTT Exception");
        }
    }

    public void changeHum(MQTTHelper client, int qos){
        try{
            byte[] encodedPayload;

            String msg = "humSwitch";

            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(qos);

            client.mqttAndroidClient.publish("ledCM2223", message);
        } catch (Exception e){
            Log.w(TAG, "MQTT Exception");
        }
    }

    public void changeLed(MQTTHelper client,String msg, int qos){
        try{
            byte[] encodedPayload;

            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(qos);

            client.mqttAndroidClient.publish("ledCM2223", message);
        } catch (Exception e){
            Log.w(TAG, "MQTT Exception");
        }
    }

}
