package com.example.vkr1.Entity;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    @SerializedName("msg")
    private String message;

    @SerializedName("from")
    private int senderId;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("room")
    private String room;

    private boolean isMine = false;

    public Message() {

    }

    public Message(String message, int senderId, long timestamp, boolean isMine) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.isMine = isMine;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNormalTime() {
        Date date = new java.util.Date((long) timestamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
