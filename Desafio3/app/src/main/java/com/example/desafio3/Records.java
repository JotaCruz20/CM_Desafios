package com.example.desafio3;

public class Records {
    String atualizado;
    double percentage;
    String sensorName;

    public Records(String atualizado, double percentage, String sensorName) {
        this.atualizado = atualizado;
        this.percentage = percentage;
        this.sensorName = sensorName;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getAtualizado() {
        return atualizado;
    }

    public void setAtualizado(String atualizado) {
        this.atualizado = atualizado;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(long percentage) {
        this.percentage = percentage;
    }
}
