package com.example.android_app;


import com.google.firebase.Timestamp;

// Model class for Shopping List
public class ShoppingList {
    String title;
    String content;
    Timestamp timestamp;

    public ShoppingList() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
