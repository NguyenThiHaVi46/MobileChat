package com.example.mychat.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mychat.R;
import com.example.mychat.activity.ChatGeminiAiActivity;
import com.example.mychat.adapter.ChatRoomGeminiAdapter;
import com.example.mychat.data.repository.ChatRoomGeminiRepository;
import com.example.mychat.models.ChatRoomGemini;

import java.util.List;

public class ChatGeminiFragment extends Fragment {

    RecyclerView recyclerView;
    private ChatRoomGeminiAdapter adapter;
    private List<ChatRoomGemini> chatRoomList;
    private ChatRoomGeminiRepository chatRoomRepository;
    Button newChat;

    public ChatGeminiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_gemini, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        newChat = view.findViewById(R.id.btn_create_new_chat);
        chatRoomRepository = new ChatRoomGeminiRepository(getContext());

        loadChatRooms();

        newChat.setOnClickListener(v -> {
            String chatRoomName = "New Chat Room";
            long newChatRoomId = chatRoomRepository.createChatRoom(chatRoomName);

            Intent intent = new Intent(getContext(), ChatGeminiAiActivity.class);
            intent.putExtra("ROOM_ID", newChatRoomId);
            startActivity(intent);
        });

        return view;
    }

    private void loadChatRooms() {
        chatRoomList = chatRoomRepository.getAllChatRooms();

        adapter = new ChatRoomGeminiAdapter(getContext(), chatRoomList,
                chatRoomId -> {
                    Intent intent = new Intent(getContext(), ChatGeminiAiActivity.class);
                    intent.putExtra("ROOM_ID", chatRoomId);
                    startActivity(intent);
                },
                chatRoom -> showEditNameDialog(chatRoom),
                chatRoom -> showDeleteConfirmationDialog(chatRoom)
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    private void showEditNameDialog(ChatRoomGemini chatRoom) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Chat Room Name");

        final EditText input = new EditText(getContext());
        input.setText(chatRoom.getName());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = input.getText().toString();
            chatRoomRepository.updateChatRoomName(chatRoom.getId(), newName);
            loadChatRooms(); // Reload the chat rooms
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(ChatRoomGemini chatRoom) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Chat Room");
        builder.setMessage("Are you sure you want to delete this chat room?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            chatRoomRepository.deleteChatRoom(chatRoom.getId());
            loadChatRooms(); // Reload the chat rooms
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
