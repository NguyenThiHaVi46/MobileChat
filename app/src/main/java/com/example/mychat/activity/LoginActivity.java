package com.example.mychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychat.R;

public class LoginActivity extends AppCompatActivity {

    EditText lgPhone,lgPassword;
    Button login,signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        lgPhone = findViewById(R.id.login_phone);
        lgPassword = findViewById(R.id.login_password);
        login = findViewById(R.id.login_loginBtn);
        signup = findViewById(R.id.login_goto_signupBtn);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginPhoneNumberActivity.class));
            }
        });
    }
}