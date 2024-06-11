package com.example.mychat.models;

import com.google.firebase.Timestamp;

public class User {
    private String phoneNumber;
    private String username;
    private Timestamp timestamp;

    private String userId;
    private String fcmToken;
    private String password;

    public User() {

    }
    public User(String phone, String userName, Timestamp createdTimestamp, String userId, String password) {
        this.phoneNumber = phone;
        this.username = userName;
        this.timestamp = createdTimestamp;
        this.userId = userId;
        this.password = password;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Existing constructor
    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //
    public User(String phoneNumber, String username, Timestamp timestamp) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }



}