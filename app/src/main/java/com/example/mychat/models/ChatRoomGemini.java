package com.example.mychat.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatRoomGemini")
public class ChatRoomGemini {

    @PrimaryKey(autoGenerate = true)


    private long id;

    private String name;

    public ChatRoomGemini(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}