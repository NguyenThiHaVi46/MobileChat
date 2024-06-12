package com.example.mychat.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mychat.models.ChatRoomGemini;
import com.example.mychat.models.MessageAi;

import java.util.List;

@Dao
public interface MessageAIDAO {

    @Insert
    void insertMessageAi(MessageAi messageAi);

    @Insert
    void insertMessageAis(List<MessageAi> messageAis);

    @Delete
    void delete(MessageAi messageAi);

    @Query("SELECT * FROM messageAi WHERE roomId = :roomId")
    List<MessageAi> getListMessageAi(long roomId);



}
