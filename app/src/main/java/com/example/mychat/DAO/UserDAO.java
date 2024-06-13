package com.example.mychat.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mychat.models.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(List<User> users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user")
    List<User> getListUser();

    @Query("SELECT * FROM user WHERE userId = :id")
    User getUserById(String id);

    @Update
    void updateUser(User user);
}
