package com.example.mychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychat.R;
import com.example.mychat.data.repository.UserRepository;
import com.example.mychat.models.User;

import com.example.mychat.utils.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.mindrot.jbcrypt.BCrypt;

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;

public class LoginActivity extends AppCompatActivity {

    EditText lgEmail,lgPassword;
    Button login,signup;
    UserRepository userRepository;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lgEmail = findViewById(R.id.login_email);
        lgPassword = findViewById(R.id.login_password);
        login = findViewById(R.id.login_loginBtn);
        signup = findViewById(R.id.login_goto_signupBtn);

        User user1 = AndroidUtil.getUserModelFromIntent(getIntent());

        lgEmail.setText(user1.getEmail());


        userRepository = new UserRepository(getApplication());

        signup.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginPhoneNumberActivity.class)));

        login.setOnClickListener(v -> {
            String emailInput = lgEmail.getText().toString();
            String password = lgPassword.getText().toString();

            if (emailInput.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter phone number and password", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User user = userRepository.getUserByEmail(emailInput);

                Log.d("PASS LOGIN: ", password);
                Log.d("HASH PASS LOGIN: ", BCrypt.hashpw(password, BCrypt.gensalt()));

                Log.d("HASH PASS LOGIN data: ", user.getPassword());

                if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                    runOnUiThread(() -> signInWithEmail(emailInput, user.getPassword()));
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void signInWithEmail(String email,String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý ngoại lệ khi không thể tạo JWT token
        }
    }
}