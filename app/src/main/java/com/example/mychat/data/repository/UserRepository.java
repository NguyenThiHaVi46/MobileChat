package com.example.mychat.data.repository;

import android.content.Context;

import com.example.mychat.data.RoomData.Data;
import com.example.mychat.models.MessageAi;
import com.example.mychat.models.User;

import java.util.List;

public class UserRepository {
//

    Context context;

    public UserRepository(Context context) {
        this.context = context;
    }

    public void saveUser(User user) {
        Data.getInstance(context).userDAO().insertUser(user);
    }

    public void updateUser(User user) {
        Data.getInstance(context).userDAO().updateUser(user);
    }
    public List<User> getAllUser() {
        return Data.getInstance(context).userDAO().getListUser();
    }

    public User getUserById(String userId) {
        return Data.getInstance(context).userDAO().getUserById(userId);
    }


    public User getUserByEmail(String email) {
        return Data.getInstance(context).userDAO().getUserByEmail(email);
    }
}
