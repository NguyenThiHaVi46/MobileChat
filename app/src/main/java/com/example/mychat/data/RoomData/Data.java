package com.example.mychat.data.RoomData;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mychat.DAO.ChatRoomGeminiDAO;
import com.example.mychat.DAO.MessageAIDAO;
import com.example.mychat.DAO.UserDAO;
import com.example.mychat.models.ChatRoomGemini;
import com.example.mychat.models.MessageAi;
import com.example.mychat.models.User;
import com.example.mychat.utils.Converters;

@Database(entities = {MessageAi.class, ChatRoomGemini.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class Data extends RoomDatabase {
    private static final String DATABASE_NAME = "mychat";

    private static Data instance;

    public abstract MessageAIDAO messageAIDAO();
    public abstract ChatRoomGeminiDAO chatRoomGeminiDAO();

    public static synchronized Data getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, Data.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
