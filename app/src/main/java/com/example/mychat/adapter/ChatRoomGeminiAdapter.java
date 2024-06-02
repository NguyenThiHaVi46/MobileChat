package com.example.mychat.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.models.ChatRoomGemini;

import java.util.List;

public class ChatRoomGeminiAdapter extends RecyclerView.Adapter<ChatRoomGeminiAdapter.ChatRoomGeminiHolder> {

    private List<ChatRoomGemini> chatRooms;
    private Context context;
    private OnChatRoomClickListener listener;
    private OnEditNameClickListener editListener;
    private OnDeleteRoomClickListener deleteListener;

    public ChatRoomGeminiAdapter(@NonNull Context context, List<ChatRoomGemini> chatRooms, OnChatRoomClickListener listener,
                                 OnEditNameClickListener editListener, OnDeleteRoomClickListener deleteListener) {
        this.context = context;
        this.chatRooms = chatRooms;
        this.listener = listener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ChatRoomGeminiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_room_gemini_recycler, parent, false);
        return new ChatRoomGeminiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomGeminiHolder holder, int position) {
        ChatRoomGemini chatRoom = chatRooms.get(position);
        Log.d("ChatRoomGeminiAdapter", "Binding ChatRoom ID: " + chatRoom.getId() + ", Name: " + chatRoom.getName());

//        holder.lassMessageTime.setText();
        holder.nameRoom.setText(chatRoom.getName());
        holder.itemView.setOnClickListener(v -> listener.onChatRoomClick(chatRoom.getId()));
        holder.editNameRoom.setOnClickListener(v -> editListener.onEditNameClick(chatRoom));
        holder.deleteRoom.setOnClickListener(v -> deleteListener.onDeleteRoomClick(chatRoom));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatRoomGeminiHolder extends RecyclerView.ViewHolder {
        TextView nameRoom;
        ImageButton editNameRoom,deleteRoom;

        public ChatRoomGeminiHolder(@NonNull View itemView) {
            super(itemView);
            nameRoom = itemView.findViewById(R.id.Gemini_name_room);
            editNameRoom = itemView.findViewById(R.id.Gemini_edit_name_room);
            deleteRoom = itemView.findViewById(R.id.Gemini_delete_room);
        }
    }

    public interface OnChatRoomClickListener {
        void onChatRoomClick(long chatRoomId);
    }

    public interface OnEditNameClickListener {
        void onEditNameClick(ChatRoomGemini chatRoom);
    }

    public interface OnDeleteRoomClickListener {
        void onDeleteRoomClick(ChatRoomGemini chatRoom);
    }
}
