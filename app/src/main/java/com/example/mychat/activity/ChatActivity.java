package com.example.mychat.activity;



import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.adapter.ChatRecyclerAdapter;
import com.example.mychat.models.ChatMessage;
import com.example.mychat.models.ChatRoom;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_VIDEO_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;


    User otherUser;
    ChatRoom chatRoom;
    String chatRoomId;
    ChatRecyclerAdapter adapter;
    EditText messageInput;
    ImageButton sendMessageBtn, backBtn,showImageBtn,buttonCamera,buttonFile;
    TextView otherUserName;
    RecyclerView recyclerView;
    ImageView imageView;

    LinearLayout hiddenButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatRoomId = FirebaseUtil.getChatRoomId(FirebaseUtil.currentUserId(), otherUser.getUserId());


        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUserName = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_RecyclerView);
        imageView = findViewById(R.id.profile_pic_image_view);

        showImageBtn = findViewById(R.id.show_image);
        hiddenButtons = findViewById(R.id.hidden_buttons);
        buttonCamera = findViewById(R.id.button_camera);
        buttonFile = findViewById(R.id.button_file);
        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this, uri, imageView);
                    }
                });
        showImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenButtons.getVisibility() == View.GONE) {
                    hiddenButtons.setVisibility(View.VISIBLE);
                    showImageBtn.setVisibility(View.GONE);
                } else {
                    hiddenButtons.setVisibility(View.GONE);
                }
            }
        });

        messageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showImageBtn.getVisibility() == View.GONE) {
                    showImageBtn.setVisibility(View.VISIBLE);
                    hiddenButtons.setVisibility(View.GONE);
                } else {
                    hiddenButtons.setVisibility(View.GONE);
                }
            }
        });

        buttonCamera.setOnClickListener(v -> openFileChooser(PICK_IMAGE_VIDEO_REQUEST));
        buttonFile.setOnClickListener(v -> openFileChooser(PICK_FILE_REQUEST));



        backBtn.setOnClickListener((v) -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        otherUserName.setText(otherUser.getUsername());

        sendMessageBtn.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();

            if (!message.isEmpty()) {
                sendMessageToUser(message,"text");
            }
        });

        getOnCreateChatRoomModel();
        setUpChatRecyclerView();
    }


    void setUpChatRecyclerView() {
        Query query = FirebaseUtil.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        // lam cho no chay text len

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    void sendMessageToUser(String message, String type) {
        chatRoom.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatRoom.setLastMessageTimestamp(Timestamp.now());
        if (type.equals("image")) {
            chatRoom.setLastMessage("Sent an image");
        } else if (type.equals("video")) {
            chatRoom.setLastMessage("Sent a video");
        } else if (type.equals("file")) {
            chatRoom.setLastMessage("Sent a file");
        } else {
            chatRoom.setLastMessage(message);
        }
        FirebaseUtil.getChatroomReference(chatRoomId).set(chatRoom);
        ChatMessage chatMessage = new ChatMessage(message, FirebaseUtil.currentUserId(), Timestamp.now(),type);

        FirebaseUtil.getChatRoomMessageReference(chatRoomId).add(chatMessage)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageInput.setText("");
//                            sendNotification(message,type);
                        }
                    }
                });
    }

    void getOnCreateChatRoomModel() {
        FirebaseUtil.getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoom = task.getResult().toObject(ChatRoom.class);
                if (chatRoom == null) {
                    chatRoom = new ChatRoom(
                            chatRoomId,
                            Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatRoomId).set(chatRoom);
                }
            }
        });
    }

//    void sendNotification(String message, String type){
//
//        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
//            if(task.isSuccessful()){
//                User currentUser = task.getResult().toObject(User.class);
//                try{
//                    JSONObject jsonObject  = new JSONObject();
//
//                    JSONObject notificationObj = new JSONObject();
//                    notificationObj.put("title",currentUser.getUserName());
//                    notificationObj.put("body",type.equals("image") ? "Sent an image" : (type.equals("video") ? "Sent a video" : message));
//
//                    JSONObject dataObj = new JSONObject();
//                    dataObj.put("userId",currentUser.getUserId());
//
//                    jsonObject.put("notification",notificationObj);
//                    jsonObject.put("data",dataObj);
//                    jsonObject.put("to",otherUser.getFcmToken());
//
//                    callApi(jsonObject);
//
//
//                }catch (Exception e){
//
//                }
//
//            }
//        });
//
//    }

//    void callApi(JSONObject jsonObject){
//        MediaType JSON = MediaType.get("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//        String url = "https://fcm.googleapis.com/fcm/send";
//        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .header("Authorization","Bearer AAAAWupq0IU:APA91bF3VtCO6Ky6P8BlYFs0xu9nd7PfLoLCN_JB5mSVhm5dPBFq1b4FQvM_5gdcfN73QwOyHB1PD-_3-JxQaBDugFkSYoZny7e0i4FWVnD9w--vlJh2phaXpUcNPsUwI1bCRLSOuMpy")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//            }
//        });
//
//    }

    void openFileChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (requestCode == PICK_IMAGE_VIDEO_REQUEST) {
            intent.setType("*/*");
            String[] mimeTypes = {"image/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else if (requestCode == PICK_FILE_REQUEST) {
            intent.setType("*/*");
        }

        startActivityForResult(Intent.createChooser(intent, "Select File"), requestCode);
    }

    private void uploadFileToFirebaseStorage(Uri fileUri, String fileType) {
        if (fileUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("chat_files/" + System.currentTimeMillis() + "." + getFileExtension(fileUri));

            storageReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String fileUrl = uri.toString();
                    sendMessageToUser(fileUrl, fileType);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(ChatActivity.this, "Failed to upload file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            if (requestCode == PICK_IMAGE_VIDEO_REQUEST) {
                String mimeType = getContentResolver().getType(fileUri);
                if (mimeType.startsWith("image/")) {
                    uploadFileToFirebaseStorage(fileUri, "image");
                } else if (mimeType.startsWith("video/")) {
                    uploadFileToFirebaseStorage(fileUri, "video");
                }
            } else if (requestCode == PICK_FILE_REQUEST) {
                uploadFileToFirebaseStorage(fileUri, "file");
            }
        }

    }

}
