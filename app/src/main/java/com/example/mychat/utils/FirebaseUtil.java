package com.example.mychat.utils;

import android.net.wifi.hotspot2.pps.Credential;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.annotation.Documented;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;

    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }


    public static CollectionReference allUserCollectionReference(){
        return  FirebaseFirestore.getInstance().collection("users");
    }

    public  static DocumentReference getChatroomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatRooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomMessageReference(String chatRoomId){
        return getChatroomReference(chatRoomId).collection("chats");
    }
    public static String getChatRoomId(List<String> userIds) {
        Collections.sort(userIds);

        StringBuilder chatRoomId = new StringBuilder();
        for (int i = 0; i < userIds.size(); i++) {
            chatRoomId.append(userIds.get(i));
            if (i < userIds.size() - 1) {
                chatRoomId.append("_");
            }
        }

        return chatRoomId.toString();
    }
    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatRooms");
    }

    public  static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if(userIds.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userIds.get(1));
        }else {
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:mm").format(timestamp.toDate());
    }

    public static StorageReference getCurrentProfilePicStorageRef(String roomId) {
        if(roomId.equals("")){
            return FirebaseStorage.getInstance().getReference().child("profile_pic")
                    .child(FirebaseUtil.currentUserId());
        }else {
            return FirebaseStorage.getInstance().getReference().child("profile_pic")
                    .child(roomId);
        }
    }
    public static StorageReference getOtherProfilePicStorageRef(String otherUserId) {
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserId);
    }
}
