package com.example.mychat.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mychat.models.ChatRoomGemini;

import java.util.List;

@Dao
public interface ChatRoomGeminiDAO {


    @Insert
    long insertChatRoomGemini(ChatRoomGemini chatRoomGemini);

    @Insert
    void insertChatRoomGeminis(List<ChatRoomGemini> chatRoomGeminis);

    @Delete
    void delete(ChatRoomGemini chatRoomGemini);

    @Query("SELECT * FROM chatRoomGemini")
    List<ChatRoomGemini> getListChatRoomGemini();

    @Query("SELECT * FROM chatRoomGemini WHERE id = :id")
    ChatRoomGemini getChatRoomById(long id);

    @Update
    void updateChatRoomGemini(ChatRoomGemini chatRoomGemini);


}
