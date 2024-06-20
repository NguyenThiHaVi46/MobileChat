package com.example.mychat.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.util.Objects;

@Entity(tableName = "user")
public class User {
    private String phoneNumber;
    private String username;
    private Timestamp timestamp;

    @PrimaryKey()
    @NonNull
    private String userId;
    private String fcmToken;
    private String password;

    private  String email;

    public User() {

    }
    public User(String phone, String userName, Timestamp createdTimestamp, String userId, String password,String email) {
        this.phoneNumber = phone;
        this.username = userName;
        this.timestamp = createdTimestamp;
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    // Thêm vào lớp User của bạn
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}