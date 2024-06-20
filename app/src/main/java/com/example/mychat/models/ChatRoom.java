package com.example.mychat.models;


import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoom {
    private String chatRoomId;
    private String chatRoomName;
    private com.google.firebase.Timestamp lastMessageTimestamp;
    private String lastMessageSenderId;
    private String lastMessage;
    private List<String> userIds;

    public ChatRoom() {
    }

    // Constructor có tham số
    public ChatRoom(String chatRoomId, String chatRoomName, com.google.firebase.Timestamp lastMessageTimestamp, String lastMessageSenderId, String lastMessage, List<String> userIds) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
        this.lastMessage = lastMessage;
        this.userIds = userIds;
    }

    // Getter và Setter cho tất cả các trường
    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public com.google.firebase.Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(com.google.firebase.Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
