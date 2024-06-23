package com.example.mychat.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.google.firebase.Timestamp;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static byte[] fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @TypeConverter
    public static Bitmap toBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @TypeConverter
    public static Timestamp fromLong(Long value) {
        return value == null ? null : new Timestamp(new Date(value));
    }

    @TypeConverter
    public static Long timestampToLong(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toDate().getTime();
    }
}
