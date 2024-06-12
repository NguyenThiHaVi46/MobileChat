package com.example.mychat.data.repository;

import android.content.Context;

import com.example.mychat.data.RoomData.Data;
import com.example.mychat.models.MessageAi;
import com.example.mychat.models.User;

import java.util.List;

public class UserRepository {

    private Data database;
    private String userId;

    public UserRepository(Data database, String userId) {
        this.database = database;
        this.userId = userId;
    }

    public void saveUser(User user) {
        database.userDAO().insertUser(user);
    }

    public void updateUser(User user) {
        database.userDAO().updateUser(user);
    }
    public List<User> getAllUser() {
        return database.userDAO().getListUser();
    }

    public User getUserById(String userId) {
        return database.userDAO().getUserById(userId);
    }
}
