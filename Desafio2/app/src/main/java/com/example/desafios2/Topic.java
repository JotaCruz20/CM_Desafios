package com.example.desafios2;

public class Topic {
    private String topic_name;
    //private Byte[] message;

    public Topic(){};

    public Topic(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }
}
