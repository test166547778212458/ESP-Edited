package com.example.yahya.esp.db;


public class Message {
    int id;
    String message;
    String date;

    public Message(){

    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "message "+message+ " date "+date;
    }
}
