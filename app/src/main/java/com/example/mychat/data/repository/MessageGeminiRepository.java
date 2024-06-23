package com.example.mychat.data.repository;

import android.content.Context;
import com.example.mychat.data.RoomData.Data;
import com.example.mychat.models.MessageAi;

import java.util.List;

public class MessageGeminiRepository {
    private Data database;
    private long chatRoomId;

    public MessageGeminiRepository(Context context, long chatRoomId) {
        database = Data.getInstance(context);
        this.chatRoomId = chatRoomId;
    }

    public void saveMessage(MessageAi message) {
        database.messageAIDAO().insertMessageAi(message);
    }

    public List<MessageAi> getAllMessages(long roomId) {
        return database.messageAIDAO().getListMessageAi(roomId);
    }
}
