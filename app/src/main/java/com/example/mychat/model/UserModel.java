package com.example.mychat.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phoneNumber;
    private String username;
    private Timestamp timestamp;

    public UserModel(String phoneNumber, String username, Timestamp timestamp, String userId) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public UserModel(String phoneNumber, String username) {
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    private String userId;
    private String fcmToken;
    private String password;

    public UserModel() {

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
    public UserModel(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // New constructor
    public UserModel(String phoneNumber, String username, Timestamp timestamp) {
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