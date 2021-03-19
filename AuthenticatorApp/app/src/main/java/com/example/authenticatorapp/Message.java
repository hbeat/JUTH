package com.example.authenticatorapp;

public class Message {
    String message;
    String name;
    String key;
    String uid;

    public Message(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Message(String message, String name, String uid) {
        this.message = message;
        this.name = name;
        this.uid = uid;
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
