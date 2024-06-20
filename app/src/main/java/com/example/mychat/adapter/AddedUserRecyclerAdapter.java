package com.example.mychat.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;

import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// ...

public class AddedUserRecyclerAdapter extends RecyclerView.Adapter<AddedUserRecyclerAdapter.AddedUserViewHolder> {

    private Map<User, Boolean> userMap;
    private final Context context;
    private List<User> userList = new ArrayList<>(); // Danh sách người dùng hiện tại

    public AddedUserRecyclerAdapter(Map<User, Boolean> userMap, Context context) {
        this.userMap = userMap;
        this.context = context;
        updateUserList(); // Cập nhật danh sách người dùng khi khởi tạo
    }

    private void updateUserList() {
        userList = userMap.entrySet().stream()
                .filter(Map.Entry::getValue) // Chỉ lấy người dùng có giá trị true
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void setUserMap(Map<User, Boolean> userMap) {
        this.userMap = userMap;
        updateUserList();
        notifyDataSetChanged(); // Thông báo adapter rằng dữ liệu đã thay đổi
    }

    @NonNull
    @Override
    public AddedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_user_recycler_row, parent, false);
        return new AddedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedUserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameText.setText(user.getUsername());
        FirebaseUtil.getOtherProfilePicStorageRef(user.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class AddedUserViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        ImageView profilePic;

        public AddedUserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.text_name_user);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}