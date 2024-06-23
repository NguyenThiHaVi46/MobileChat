package com.example.mychat.models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "messageAi")
public class MessageAi {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String text;
    private boolean isSentByUser;
    private Bitmap image;
    private long timestamp;

    private long roomId;

    public MessageAi(String text, boolean isSentByUser, Bitmap image, long timestamp, long roomId) {
        this.text = text;
        this.isSentByUser = isSentByUser;
        this.image = image;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
