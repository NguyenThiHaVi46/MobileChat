package com.example.mychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mychat.R;
//import com.example.mychat.data.dataHelper.UserDatabaseHelper;
import com.example.mychat.models.User;
import com.example.mychat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {

    EditText userNameInput,password,confirmPassword;
    Button letMeInBtn;
    ProgressBar progressBar;
    User user;

    String phoneNumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_user_name);


        userNameInput = findViewById(R.id.signup_userName);
        letMeInBtn = findViewById(R.id.signup_letMeIn_btn);
        progressBar = findViewById(R.id.signup_progress_bar);
        phoneNumber = getIntent().getExtras().getString("phone");
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);

        getUserName();


        letMeInBtn.setOnClickListener((v -> {
            setUserName();
        }));
    }

    void setUserName(){

        String userName = userNameInput.getText().toString();
        String passWord = password.getText().toString();
        String confirmPassWord = confirmPassword.getText().toString().trim();
        if(userName.isEmpty()||userName.length()<3){
            userNameInput.setError("Username length should be at least 3 chars");
            return;
        }
        if (passWord.equals(confirmPassWord)) {
//            UserDatabaseHelper db = new UserDatabaseHelper(this);
//            User user = db.getUser(FirebaseUtil.currentUserId());
            if (user != null) {
                user.setUsername(userName);
                user.setPhoneNumber(phoneNumber);
//                user.setPassword(passWord);
//                db.updateUser(user);
            } else {
                user = new User(phoneNumber, userName, Timestamp.now(), FirebaseUtil.currentUserId(), passWord);
//                db.addUser(user);
            }
        } else {
            Toast.makeText(LoginUserNameActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        setInProgress(true);


        FirebaseUtil.currentUserDetails().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);

                if(task.isSuccessful()){
                    Intent intent  = new Intent(LoginUserNameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

//    void getUserName(){
//        setInProgress(true);
//        UserDatabaseHelper db = new UserDatabaseHelper(this);
//        User user = db.getUser(FirebaseUtil.currentUserId());
//        userNameInput.setText(user.getUsername());
//        setInProgress(false);
//
//    }

    void getUserName(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    user =  task.getResult().toObject(User.class);
                   if(user!=null){
                       userNameInput.setText(user.getUsername());
                   }
                }else {

                }

            }
        });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}