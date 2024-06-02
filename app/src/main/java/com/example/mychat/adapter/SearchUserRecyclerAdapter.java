package com.example.mychat.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mychat.R;
import com.example.mychat.model.UserModel;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder userModelViewHolder, int i, @NonNull UserModel user) {
        userModelViewHolder.usernameText.setText(user.getUsername());
        userModelViewHolder.phoneText.setText(user.getPhoneNumber());
        if(user.getUserId().equals(FirebaseUtil.currentUserId())){
            userModelViewHolder.usernameText.setText(user.getUsername() + "(Me)");
        }
//
//        userModelViewHolder.itemView.setOnClickListener(v->{
//            Intent intent = new Intent(context, ChatActivity.class);
//            AndroidUtil.passUserModelAsIntent(intent,user);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder {

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