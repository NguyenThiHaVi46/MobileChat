package com.example.mychat.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.activity.ChatActivity;
import com.example.mychat.models.MessageAi;
import com.example.mychat.utils.AndroidUtil;

import java.util.List;

public class ChatGeminiRecyclerAdapter extends RecyclerView.Adapter<ChatGeminiRecyclerAdapter.ChatGeminiHolder>{

    private List<MessageAi> messages;
    private Context context;

    public ChatGeminiRecyclerAdapter(Context context, List<MessageAi> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatGeminiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent,false);
        return new ChatGeminiRecyclerAdapter.ChatGeminiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGeminiHolder holder, int position) {
        holder.allVideoLayout.setVisibility(View.GONE);
        holder.allImageLayout.setVisibility(View.GONE);
        holder.allFileLayout.setVisibility(View.GONE);

        MessageAi message = messages.get(position);
        if (message.isSentByUser()) {
            holder.allChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(message.getText());
            holder.leftChatLayout.setVisibility(View.GONE);
            if (message.getImage() != null) {
                holder.allImageLayout.setVisibility(View.VISIBLE);
                holder.rightChatImageView.setImageBitmap(message.getImage());
                holder.leftImageLayout.setVisibility(View.GONE);
            }
        } else {
            holder.allChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(message.getText());
            holder.rightChatLayout.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    class ChatGeminiHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout, rightChatLayout,leftImageLayout, rightImageLayout,
                allChatLayout, allImageLayout,allVideoLayout,
                allFileLayout;
        TextView leftChatTextView, rightChatTextView;
        ImageView leftChatImageView, rightChatImageView;
        public ChatGeminiHolder(@NonNull View itemView) {
            super(itemView);

            allChatLayout = itemView.findViewById(R.id.all_chat_layout);
            allImageLayout = itemView.findViewById(R.id.all_image_layout);
            allVideoLayout = itemView.findViewById(R.id.all_video_layout);
            allFileLayout = itemView.findViewById(R.id.all_file_layout);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftImageLayout = itemView.findViewById(R.id.left_image_layout);
            rightImageLayout = itemView.findViewById(R.id.right_image_layout);


            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
            leftChatImageView = itemView.findViewById(R.id.left_chat_image);
            rightChatImageView = itemView.findViewById(R.id.right_chat_image);


        }
    }
}
