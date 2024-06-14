package com.example.mychat.activity;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychat.R;
import com.example.mychat.data.RoomData.Data;
import com.example.mychat.data.repository.UserRepository;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import org.mindrot.jbcrypt.BCrypt;

public class LoginUserNameActivity extends AppCompatActivity {

    EditText userNameInput,password,confirmPassword,emailInput;
    Button letMeInBtn;
    ProgressBar progressBar;
    User user;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_user_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameInput = findViewById(R.id.signup_userName);
        letMeInBtn = findViewById(R.id.signup_letMeIn_btn);
        progressBar = findViewById(R.id.signup_progress_bar);
        phoneNumber = getIntent().getExtras().getString("phone");
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);
        emailInput = findViewById(R.id.signup_email);
        String userId = getIntent().getExtras().getString("userId");

        getUserName(userId);

        letMeInBtn.setOnClickListener((v -> {
            setUserName(userId);
        }));
    }

    void setUserName(String userId){
        UserRepository userRepository = new UserRepository(getApplication());

        String userName = userNameInput.getText().toString();
        String passWord = password.getText().toString();
        String confirmPassWord = confirmPassword.getText().toString();
        String email = emailInput.getText().toString();
        if(userName.isEmpty()||userName.length()<3){
            userNameInput.setError("Username length should be at least 3 chars");
            return;
        }
        if (passWord.equals(confirmPassWord)) {
            String hashedPassword = BCrypt.hashpw(passWord, BCrypt.gensalt());

            if (user != null) {
                user.setPhoneNumber(phoneNumber);
                user.setPassword(hashedPassword);
                userRepository.updateUser(user);
            } else {
                user = new User(phoneNumber, userName, Timestamp.now(), userId, hashedPassword,email);
                userRepository.saveUser(user);
                linkEmailAndPassword(email,hashedPassword);
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
                    FirebaseUtil.logout();
                    Intent intent  = new Intent(LoginUserNameActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    void getUserName(String userId){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    user = task.getResult().toObject(User.class);
                    if(user != null){
                        userNameInput.setText(user.getUsername());
                    }
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

    public void linkEmailAndPassword(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            currentUser.linkWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Liên kết thành công
                            FirebaseUser user = task.getResult().getUser();
                            String uid = user.getUid();
                            // UID của user vẫn là UID ban đầu
                            Toast.makeText(this, "Email linked successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Liên kết thất bại
                            Toast.makeText(this, "Failed to link email", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No user is currently signed in", Toast.LENGTH_SHORT).show();
        }
    }



}