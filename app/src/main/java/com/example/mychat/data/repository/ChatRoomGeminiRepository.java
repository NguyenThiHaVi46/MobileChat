package com.example.mychat.data.repository;

import android.content.Context;
import com.example.mychat.data.RoomData.Data;
import com.example.mychat.models.ChatRoomGemini;

import java.util.List;

public class ChatRoomGeminiRepository {
    private Data database;

    public ChatRoomGeminiRepository(Context context) {
        database = Data.getInstance(context);
    }

    public long createChatRoom(String name) {
        ChatRoomGemini chatRoom = new ChatRoomGemini(name);
        long newChatRoomId = database.chatRoomGeminiDAO().insertChatRoomGemini(chatRoom);
        return newChatRoomId;
    }

    public List<ChatRoomGemini> getAllChatRooms() {
        return database.chatRoomGeminiDAO().getListChatRoomGemini();
    }

    public void updateChatRoomName(long id, String newName) {
        ChatRoomGemini existingChatRoom = database.chatRoomGeminiDAO().getChatRoomById(id);
        if (existingChatRoom != null) {
            existingChatRoom.setName(newName);
            database.chatRoomGeminiDAO().updateChatRoomGemini(existingChatRoom);
        }
    }

    public void deleteChatRoom(long id) {
        ChatRoomGemini existingChatRoom = database.chatRoomGeminiDAO().getChatRoomById(id);
        if (existingChatRoom != null) {
            database.chatRoomGeminiDAO().delete(existingChatRoom);
        }
    }

}
