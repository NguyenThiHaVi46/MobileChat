package com.example.mychat.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.adapter.AccountConversionAdapter;
import com.example.mychat.adapter.ChatRecyclerAdapter;
import com.example.mychat.adapter.SearchUserRecyclerAdapter;
import com.example.mychat.models.ChatMessage;
import com.example.mychat.models.ChatRoom;
import com.example.mychat.models.NotificationSender;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.common.net.MediaType;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_VIDEO_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;
    private Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLauncher;

    AccountConversionAdapter accountConversionAdapter;
    User otherUser;
    ChatRoom chatRoom;
    String chatRoomId;
    ChatRecyclerAdapter adapter;
    EditText messageInput;
    ImageButton sendMessageBtn, backBtn,showImageBtn,buttonCamera,buttonFile,groupAddBtn,menuBtn;
    TextView otherUserName;
    RecyclerView recyclerView ,membersRecyclerview;
    ImageView imageView;
    private List<User> groupMembers;

    LinearLayout hiddenButtons;

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUserName = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_RecyclerView);
        imageView = findViewById(R.id.profile_pic_image_view);
        groupAddBtn = findViewById(R.id.chat_group_add);
        showImageBtn = findViewById(R.id.show_image);
        hiddenButtons = findViewById(R.id.hidden_buttons);
        buttonCamera = findViewById(R.id.button_camera);
        buttonFile = findViewById(R.id.button_file);
        menuBtn = findViewById(R.id.chat_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        membersRecyclerview = findViewById(R.id.members_recyclerview);



        if(getIntent().getStringExtra("listIdUser") != null){
            chatRoomId = getIntent().getStringExtra("listIdUser");
            menuBtn.setVisibility(View.VISIBLE);
            groupAddBtn.setVisibility(View.GONE);

        }else {
            otherUser = AndroidUtil.getUserModelFromIntent(getIntent());

            menuBtn.setVisibility(View.GONE);
            groupAddBtn.setVisibility(View.VISIBLE);
            imageView.setEnabled(false);
            otherUserName.setEnabled(false);
            List<String> users = new ArrayList<>(List.of(FirebaseUtil.currentUserId(), otherUser.getUserId()));
            chatRoomId = FirebaseUtil.getChatRoomId(users);
            FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                    .addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            Uri uri = t.getResult();
                            AndroidUtil.setProfilePic(this, uri, imageView);
                        }
                    });
            otherUserName.setText(otherUser.getUsername());
        }


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


        sendMessageBtn.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();

            if (!message.isEmpty()) {
                sendMessageToUser(message,"text");
            }
        });

        groupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GroupAddUserActivity.class));
            }
        });
        otherUserName.setOnClickListener(v -> showEditChatRoomNameDialog());

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(this, selectedImageUri, imageView);

                            if (selectedImageUri != null) {
                                FirebaseUtil.getCurrentProfilePicStorageRef(chatRoomId).putFile(selectedImageUri)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                AndroidUtil.showToast(this, "Updated successfully");
                                            } else {
                                                AndroidUtil.showToast(this, "Update failed");

                                            }
                                        });
                            }
                        }
                    }
                });

        imageView.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });



        groupMembers = new ArrayList<>();



        getOnCreateChatRoomModel();
        setUpChatRecyclerView();
        fetchGroupMembers();
        membersRecyclerview.setLayoutManager(new GridLayoutManager(this,1));
        accountConversionAdapter = new AccountConversionAdapter(groupMembers,this);
        membersRecyclerview.setAdapter(accountConversionAdapter);

    }


    void setUpChatRecyclerView() {
        Query query = FirebaseUtil.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        FirebaseUtil.getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoom = task.getResult().toObject(ChatRoom.class);
                if (chatRoom == null) {

                }else{
                    adapter = new ChatRecyclerAdapter(options, getApplicationContext(),chatRoom.getUserIds().size());
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
                            sendNotification(message,type);
//                            NotificationSender.sendNotification(message,type,);
//                            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task1 -> {
//
//                            });
                        }
                    }
                });
    }

    void sendNotification(String message, String type){

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                User currentUser = task.getResult().toObject(User.class);
                try{
                    JSONObject jsonObject  = new JSONObject();

                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title",currentUser.getUsername());
                    notificationObj.put("body",type.equals("image") ? "Sent an image" : (type.equals("video") ? "Sent a video" : message));

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("userId",currentUser.getUserId());

                    jsonObject.put("notification",notificationObj);
                    jsonObject.put("data",dataObj);
                    jsonObject.put("to",otherUser.getFcmToken());

                    callApi(jsonObject);


                }catch (Exception e){

                }

            }
        });

    }


    void callApi(JSONObject jsonObject) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/v1/projects/mobile-chat-3ee15/messages:send";

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String serviceAccount = "";
        InputStream inputStream = new ByteArrayInputStream(serviceAccount.getBytes(StandardCharsets.UTF_8));

        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/firebase.messaging"));
        credentials.refreshIfExpired();


        String token = credentials.getAccessToken().getTokenValue();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                System.out.println("Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Request not successful: " + response.code() + " - " + response.message());
                    System.out.println("Response: " + response.body().string());
                    return;
                }
                System.out.println("Request successful: " + response.body().string());
            }
        });
    }

    void getOnCreateChatRoomModel() {
        FirebaseUtil.getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoom = task.getResult().toObject(ChatRoom.class);
                if (chatRoom == null) {
                    chatRoom = new ChatRoom(
                            chatRoomId,"New group",Timestamp.now(),FirebaseUtil.currentUserId(),
                            "",AndroidUtil.splitStringToList(chatRoomId)
                    );
                    FirebaseUtil.getChatroomReference(chatRoomId).set(chatRoom);
                }else{
                    FirebaseUtil.getOtherProfilePicStorageRef(chatRoom.getChatRoomId()).getDownloadUrl()
                            .addOnCompleteListener(t -> {
                                if (t.isSuccessful()) {
                                    Uri uri = t.getResult();
                                    AndroidUtil.setProfilePic(this,uri,imageView);
                                }
                            });

                    if(chatRoom.getUserIds().size()>=3){
                        otherUserName.setText(chatRoom.getChatRoomName());
                    }
                }

            }
        });
    }





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

    private void showEditChatRoomNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Chat Room Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newChatRoomName = input.getText().toString();
            if (!newChatRoomName.isEmpty()) {
                updateChatRoomName(newChatRoomName);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateChatRoomName(String newChatRoomName) {
        chatRoom.setChatRoomName(newChatRoomName);
        FirebaseUtil.getChatroomReference(chatRoomId).set(chatRoom)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        otherUserName.setText(newChatRoomName);
                        Toast.makeText(ChatActivity.this, "Chat room name updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Failed to update chat room name", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchGroupMembers() {
        List<String> userIds = AndroidUtil.splitStringToList(chatRoomId);

        FirebaseUtil.getChatRoomMembers(userIds, new FirebaseUtil.OnChatRoomMembersListener() {
            @Override
            public void onSuccess(List<User> members) {
                groupMembers.clear();
                groupMembers.addAll(members);
                accountConversionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Xử lý lỗi khi không thể lấy danh sách thành viên
                Toast.makeText(ChatActivity.this, "Failed to fetch members: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
