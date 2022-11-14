package com.example.desafios2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import java.util.Objects;

public class Note {
    private String id,title, body;
    private boolean status;

    public Note(){};

    public Note(String id, String title, String body, String status) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.status = parseBool(status);
    }

    private Boolean parseBool(String bool) {
        return !Objects.equals(bool, "0");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
