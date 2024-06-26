package com.example.mychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.activity.ChatActivity;
import com.example.mychat.activity.LoginActivity;
import com.example.mychat.activity.LoginPhoneNumberActivity;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;

import java.util.List;


public class AccountConversionAdapter extends RecyclerView.Adapter<AccountConversionAdapter.UserModelViewHolder>  {

    private List<User> users;
    private Context context;

    public AccountConversionAdapter(List<User> users, Context context) { // ham contructor
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // xet item layout cho adapter (search_user_recycler_row)
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelViewHolder holder, int position) { // hien thi tung item layout ra adpater
        User user = users.get(position);
        holder.usernameText.setText(user.getUsername());
        holder.phoneText.setText(user.getPhoneNumber());
        if (user.getUserId().equals(FirebaseUtil.currentUserId())) {
            holder.usernameText.setText(user.getUsername() + " (Me)");
        }
        // Ensure the correct variable is used
        FirebaseUtil.getOtherProfilePicStorageRef(user.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        // Ensure the correct variable is used
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });
        holder.itemView.setOnClickListener(v -> {  // khi ma click thi chuyen sang LoginActivity vaf logout user hien tai
            Intent intent = new Intent(context, LoginActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,user);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            FirebaseUtil.logout();
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class UserModelViewHolder extends RecyclerView.ViewHolder {  // set cac thuoc tinh trong item layout
        TextView usernameText, phoneText;
        ImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
