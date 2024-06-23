package com.example.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychat.R;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getIntent().getExtras() != null) {
            String userId = getIntent().getExtras().getString("userId");


            if (userId != null && !userId.isEmpty()) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                // from notification
                FirebaseUtil.allUserCollectionReference().document(userId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User model = task.getResult().toObject(User.class);

                                Intent intent = new Intent(this, ChatActivity.class);
                                AndroidUtil.passUserModelAsIntent(intent, model);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // Handle the error
                            }
                        });
            } else {
                // Handle the case where userId is null or empty
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (FirebaseUtil.isLoggedIn()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                }, 1000);
            }
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (FirebaseUtil.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }, 1000);
        }
    }
}
