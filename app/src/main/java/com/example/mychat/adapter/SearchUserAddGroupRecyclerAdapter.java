package com.example.mychat.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchUserAddGroupRecyclerAdapter extends FirestoreRecyclerAdapter<User, SearchUserAddGroupRecyclerAdapter.UserModelViewHolder> {


    private Map<User, Boolean> addedUsersMap = new HashMap<>(); // Thêm thuộc tính này
    private final Context context;
    private final OnUserClickListener onUserClickListener;

    public SearchUserAddGroupRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context, OnUserClickListener onUserClickListener) {
        super(options);
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    // Thêm phương thức này để cập nhật danh sách người dùng đã thêm
    public void updateAddedUsersMap(Map<User, Boolean> addedUsersMap) {
        this.addedUsersMap = addedUsersMap;
        notifyDataSetChanged(); // Thông báo adapter rằng dữ liệu đã thay đổi
    }


    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder userModelViewHolder, int i, @NonNull User user) {
        userModelViewHolder.usernameText.setText(user.getUsername());
        userModelViewHolder.phoneText.setText(user.getPhoneNumber());
        if (user.getUserId().equals(FirebaseUtil.currentUserId())) {
            userModelViewHolder.usernameText.setText(user.getUsername() + " (Me)");
        }
        FirebaseUtil.getOtherProfilePicStorageRef(user.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, userModelViewHolder.profilePic);
                    }
                });

        Boolean isAdded = addedUsersMap.get(user);
        if (isAdded != null && isAdded) {
            userModelViewHolder.checkCircle.setVisibility(View.VISIBLE);
            userModelViewHolder.circleOutline.setVisibility(View.GONE);
        } else {
            userModelViewHolder.checkCircle.setVisibility(View.GONE);
            userModelViewHolder.circleOutline.setVisibility(View.VISIBLE);
        }

        userModelViewHolder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(user));
    }


    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_add_group_row, parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText, phoneText;
        ImageView profilePic;
        ImageButton checkCircle, circleOutline;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            checkCircle = itemView.findViewById(R.id.icon_check_circle);
            circleOutline = itemView.findViewById(R.id.icon_add_circle_outline);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}