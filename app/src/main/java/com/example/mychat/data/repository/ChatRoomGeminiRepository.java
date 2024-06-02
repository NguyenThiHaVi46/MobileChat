package com.example.mychat.data.repository;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mychat.data.dataHelper.RoomGeminiDbHelper;
import com.example.mychat.models.ChatRoomGemini;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomGeminiRepository {
    private RoomGeminiDbHelper dbHelper;

    public ChatRoomGeminiRepository(Context context) {
        dbHelper = new RoomGeminiDbHelper(context);
    }

    public long createChatRoom(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);

        long newChatRoomId = db.insert("chat_rooms", null, values);
        db.close();
        return newChatRoomId;
    }

    @SuppressLint("Range")
    public List<ChatRoomGemini> getAllChatRooms() {
        List<ChatRoomGemini> chatRooms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT c._id, c.name, MAX(m.timestamp) as last_message_time "
                + "FROM chat_rooms c LEFT JOIN messages m ON c._id = m.chat_room_id "
                + "GROUP BY c._id ORDER BY last_message_time DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                Log.d("ChatRoomGeminiRepository", "ChatRoom ID: " + id + ", Name: " + name);

                chatRooms.add(new ChatRoomGemini(id, name));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return chatRooms;
    }


    public void updateChatRoomName(long id, String newName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);

        db.update("chat_rooms", values, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteChatRoom(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("chat_rooms", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
