package com.example.desafio3;

public class Temperature {
    long timestamp;
    double percentage;

    public Temperature(long timestamp, double percentage) {
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
