package com.example.mychat.models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class MessageAi {
    private long id;
    private String text;
    private boolean isSentByUser;
    private Bitmap image;
    private long timestamp;

    public MessageAi(long id, String text, boolean isSentByUser, Bitmap image, long timestamp) {
        this.id = id;
        this.text = text;
        this.isSentByUser = isSentByUser;
        this.image = image;
        this.timestamp = timestamp;
    }

    public MessageAi(String text, boolean isSentByUser, Bitmap image, long timestamp) {
        this(0, text, isSentByUser, image, timestamp);
    }

    public MessageAi(String text, boolean isSentByUser) {
        this(text, isSentByUser, null, System.currentTimeMillis());
    }

    public MessageAi(String messageText, boolean b, Bitmap selectedImageBitmap) {
        this(messageText, b, selectedImageBitmap, System.currentTimeMillis());
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public Bitmap getImage() {
        return image;
    }

    public long getTimestamp() {
        return timestamp;
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
