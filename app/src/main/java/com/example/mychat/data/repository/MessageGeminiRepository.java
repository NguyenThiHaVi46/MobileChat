package com.example.mychat.data.repository;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.mychat.data.dataHelper.RoomGeminiDbHelper;
import com.example.mychat.models.MessageAi;

import java.util.ArrayList;
import java.util.List;

public class MessageGeminiRepository {
    private RoomGeminiDbHelper dbHelper;
    private long chatRoomId;

    public MessageGeminiRepository(Context context, long chatRoomId) {
        dbHelper = new RoomGeminiDbHelper(context);
        this.chatRoomId = chatRoomId;
    }



    public void saveMessage(MessageAi message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RoomGeminiDbHelper.COLUMN_TEXT, message.getText());
        values.put(RoomGeminiDbHelper.COLUMN_IS_SENT_BY_USER, message.isSentByUser() ? 1 : 0);
        values.put(RoomGeminiDbHelper.COLUMN_IMAGE, message.getImage() != null ? MessageAi.bitmapToBytes(message.getImage()) : null);
        values.put(RoomGeminiDbHelper.COLUMN_TIMESTAMP, message.getTimestamp());
        values.put(RoomGeminiDbHelper.COLUMN_CHAT_ROOM_ID, chatRoomId);
        db.insert(RoomGeminiDbHelper.TABLE_MESSAGES, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<MessageAi> getAllMessages() {
        List<MessageAi> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = RoomGeminiDbHelper.COLUMN_CHAT_ROOM_ID + " = ?";
        String[] selectionArgs = { String.valueOf(chatRoomId) };

        Cursor cursor = db.query(RoomGeminiDbHelper.TABLE_MESSAGES, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(RoomGeminiDbHelper.COLUMN_ID));
                String text = cursor.getString(cursor.getColumnIndex(RoomGeminiDbHelper.COLUMN_TEXT));
                boolean isSentByUser = cursor.getInt(cursor.getColumnIndex(RoomGeminiDbHelper.COLUMN_IS_SENT_BY_USER)) == 1;
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(RoomGeminiDbHelper.COLUMN_IMAGE));
                Bitmap image = imageBytes != null ? MessageAi.bytesToBitmap(imageBytes) : null;
                long timestamp = cursor.getLong(cursor.getColumnIndex(RoomGeminiDbHelper.COLUMN_TIMESTAMP));
                messages.add(new MessageAi(id, text, isSentByUser, image, timestamp));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return messages;
    }

}
