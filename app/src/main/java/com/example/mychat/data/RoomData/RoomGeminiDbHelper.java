package com.example.mychat.data.RoomData;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RoomGeminiDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_IS_SENT_BY_USER = "is_sent_by_user";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_CHAT_ROOM_ID = "chat_room_id";

    public static final String TABLE_CHAT_ROOMS = "chat_rooms";
    public static final String COLUMN_ROOM_NAME = "name";

    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE " + TABLE_MESSAGES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEXT + " TEXT, "
            + COLUMN_IS_SENT_BY_USER + " INTEGER, "
            + COLUMN_IMAGE + " BLOB, "
            + COLUMN_TIMESTAMP + " INTEGER, "
            + COLUMN_CHAT_ROOM_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_CHAT_ROOM_ID + ") REFERENCES chat_rooms(_id)"
            + ");";

    private static final String TABLE_CREATE_CHAT_ROOMS =
            "CREATE TABLE " + TABLE_CHAT_ROOMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ROOM_NAME + " TEXT" +
                    ");";

    public RoomGeminiDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.execSQL(TABLE_CREATE_CHAT_ROOMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_ROOMS);
        onCreate(db);
    }


}
