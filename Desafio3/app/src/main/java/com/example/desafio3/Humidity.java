package com.example.desafio3;

public class Humidity {
    long timestamp;
    double percentage;

    public Humidity(long timestamp, double percentage) {
        this.timestamp = timestamp;
        this.percentage = percentage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(long percentage) {
        this.percentage = percentage;
    }
}
