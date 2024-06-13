package com.example.mychat.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mychat.R;
import com.example.mychat.models.ChatMessage;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessage, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessage model) {

        holder.allVideoLayout.setVisibility(View.GONE);
        holder.allChatLayout.setVisibility(View.GONE);
        holder.allImageLayout.setVisibility(View.GONE);
        holder.allFileLayout.setVisibility(View.GONE);

        if (model.getSenderId().equals(FirebaseUtil.currentUserId())) {
            if (model.getType().equals("text")) {
                holder.rightChatTextView.setText(model.getMessage());
                holder.allChatLayout.setVisibility(View.VISIBLE);
                holder.leftChatLayout.setVisibility(View.GONE);
            } else if (model.getType().equals("image")) {
                Glide.with(holder.rightChatImageView.getContext()).load(model.getMessage()).into(holder.rightChatImageView);
                holder.allImageLayout.setVisibility(View.VISIBLE);
                holder.leftImageLayout.setVisibility(View.GONE);
            } else if (model.getType().equals("video")) {
                handleVideo(holder.rightChatVideoView, holder.rigthThumbnail, model.getMessage(),holder.rigthThumbnailRela);
                holder.leftVideoLayout.setVisibility(View.GONE);
                holder.allVideoLayout.setVisibility(View.VISIBLE);

            }else if (model.getType().equals("file")) {
                handleFile(holder.rightChatFileView, holder.rightFileLayout, model.getMessage());
                holder.allFileLayout.setVisibility(View.VISIBLE);
                holder.leftFileLayout.setVisibility(View.GONE);


            }
        } else {
            if (model.getType().equals("text")) {
                holder.leftChatTextView.setText(model.getMessage());
                holder.rightChatLayout.setVisibility(View.GONE);
                holder.allChatLayout.setVisibility(View.VISIBLE);
            } else if (model.getType().equals("image")) {
                Glide.with(holder.leftChatImageView.getContext()).load(model.getMessage()).into(holder.leftChatImageView);
                holder.rightImageLayout.setVisibility(View.GONE);
                holder.allImageLayout.setVisibility(View.VISIBLE);
            } else if (model.getType().equals("video")) {
                handleVideo(holder.leftChatVideoView, holder.leftThumbnail, model.getMessage(),holder.leftThumbnailRela);
                holder.rightVideoLayout.setVisibility(View.GONE);
                holder.allVideoLayout.setVisibility(View.VISIBLE);
            }else if (model.getType().equals("file")) {
                handleFile(holder.leftChatFileView, holder.leftFileLayout, model.getMessage());
                holder.allFileLayout.setVisibility(View.VISIBLE);
                holder.rightFileLayout.setVisibility(View.GONE);
            }
        }
    }




    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent,false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftChatLayout, rightChatLayout,leftImageLayout, rightImageLayout,
                leftVideoLayout, rightVideoLayout,allChatLayout, allImageLayout,allVideoLayout,
                allFileLayout,leftFileLayout, rightFileLayout;
        TextView leftChatTextView, rightChatTextView,leftChatFileView,rightChatFileView;
        ImageView leftChatImageView, rightChatImageView,leftThumbnail,rigthThumbnail;
        VideoView leftChatVideoView, rightChatVideoView;

        RelativeLayout leftThumbnailRela,rigthThumbnailRela;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            allChatLayout = itemView.findViewById(R.id.all_chat_layout);
            allImageLayout = itemView.findViewById(R.id.all_image_layout);
            allVideoLayout = itemView.findViewById(R.id.all_video_layout);
            allFileLayout = itemView.findViewById(R.id.all_file_layout);


            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftImageLayout = itemView.findViewById(R.id.left_image_layout);
            rightImageLayout = itemView.findViewById(R.id.right_image_layout);
            leftVideoLayout = itemView.findViewById(R.id.left_video_layout);
            rightVideoLayout = itemView.findViewById(R.id.right_video_layout);
            leftFileLayout = itemView.findViewById(R.id.left_file_layout);
            rightFileLayout = itemView.findViewById(R.id.right_file_layout);

            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
            leftChatImageView = itemView.findViewById(R.id.left_chat_image);
            rightChatImageView = itemView.findViewById(R.id.right_chat_image);
            leftChatVideoView = itemView.findViewById(R.id.left_chat_video);
            rightChatVideoView = itemView.findViewById(R.id.right_chat_video);
            leftChatFileView = itemView.findViewById(R.id.left_chat_fileView);
            rightChatFileView = itemView.findViewById(R.id.right_chat_fileView);


            rigthThumbnail = itemView.findViewById(R.id.right_chat_video_thumbnail);
            leftThumbnail = itemView.findViewById(R.id.left_chat_video_thumbnail);
            leftThumbnailRela = itemView.findViewById(R.id.left_chat_video_thumbnail_Rela);
            rigthThumbnailRela = itemView.findViewById(R.id.right_chat_video_thumbnail_Rela);
        }


    }


    private void handleVideo(VideoView videoView, ImageView thumbnailView, String videoPath, RelativeLayout relativeLayout) {
        Glide.with(thumbnailView.getContext())
                .load(videoPath)
                .thumbnail(0.1f)
                .into(thumbnailView);

        thumbnailView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        videoView.setVideoURI(Uri.parse(videoPath));

        thumbnailView.setOnClickListener(v -> {
            thumbnailView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            videoView.start();
        });

        videoView.setOnClickListener(v -> {
            if (!videoView.isPlaying()) {
                videoView.start();
            } else {
                videoView.pause();
            }
        });
    }


    private void handleFile(TextView fileNameTextView, LinearLayout downloadLL, String fileUrl) {
        String fileName = Uri.parse(fileUrl).getLastPathSegment();
        fileNameTextView.setText(fileName);
        downloadLL.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent);
        });
    }


}