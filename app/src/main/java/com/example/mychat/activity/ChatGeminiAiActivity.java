package com.example.mychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.adapter.ChatGeminiRecyclerAdapter;
import com.example.mychat.data.repository.MessageGeminiRepository;
import com.example.mychat.models.AiService;
import com.example.mychat.models.MessageAi;

import java.io.InputStream;
import java.util.List;

public class ChatGeminiAiActivity extends AppCompatActivity {

    ImageButton backBtn;
    RecyclerView recyclerView;
    EditText inputMessage;
    ImageButton sendMessageBtn, selectImageButton;
    ImageView selectedImageView;

    Bitmap selectedImageBitmap = null;
    private ChatGeminiRecyclerAdapter adapter;
    private List<MessageAi> messageList;
    long chatRoomId;
    private MessageGeminiRepository messageRepository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_gemini_ai);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        backBtn = findViewById(R.id.Ai_back_btn);
        recyclerView = findViewById(R.id.Ai_chat_RecyclerView);
        inputMessage = findViewById(R.id.Ai_chat_message_input);
        sendMessageBtn = findViewById(R.id.Ai_message_send_btn);
        selectImageButton = findViewById(R.id.Ai_show_image);
        selectedImageView = findViewById(R.id.Ai_selected_image_view);

        chatRoomId = getIntent().getLongExtra("ROOM_ID", -1);
        if (chatRoomId == -1) {
            finish();
            return;
        }

        messageRepository = new MessageGeminiRepository(this, chatRoomId);
        messageList = messageRepository.getAllMessages(chatRoomId);


        adapter = new ChatGeminiRecyclerAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("FRAGMENT_TO_LOAD", "ChatGeminiFragment");
            startActivity(intent);
            finish();
        });

        sendMessageBtn.setOnClickListener(v -> sendMessage());
        selectImageButton.setOnClickListener(v -> selectImage());
    }

    private void sendMessage() {
        String messageText = inputMessage.getText().toString().trim();
        if (!messageText.isEmpty() || selectedImageBitmap != null) {
            long timestamp = System.currentTimeMillis();
            MessageAi userMessage = new MessageAi(messageText, true, selectedImageBitmap, timestamp, chatRoomId);
            messageList.add(userMessage);
            adapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            inputMessage.setText("");
            messageRepository.saveMessage(userMessage);

            AiService.sendMessageAi(this, messageText, selectedImageBitmap, chatRoomId).thenAccept(responseText -> {
                runOnUiThread(() -> {
                    MessageAi aiMessage = new MessageAi(responseText, false, null, System.currentTimeMillis(), chatRoomId);
                    messageList.add(aiMessage);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    messageRepository.saveMessage(aiMessage);
                });
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });

            // Reset selected image after sending the message
            selectedImageBitmap = null;
            selectedImageView.setVisibility(View.GONE);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImageBitmap = BitmapFactory.decodeStream(imageStream);
                selectedImageView.setImageBitmap(selectedImageBitmap);
                selectedImageView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
