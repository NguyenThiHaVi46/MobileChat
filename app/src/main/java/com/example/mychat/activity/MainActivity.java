package com.example.mychat.activity;


import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychat.R;
import com.example.mychat.fragment.ChatFragment;
import com.example.mychat.fragment.ChatGeminiFragment;
import com.example.mychat.fragment.ProfileFragment;
import com.example.mychat.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;

    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    ChatGeminiFragment chatGeminiFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();
        chatGeminiFragment = new ChatGeminiFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search_btn);

        searchButton.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
        });

        Intent intent = getIntent();
        String fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD");
        if (fragmentToLoad != null && fragmentToLoad.equals("ChatGeminiFragment")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, chatGeminiFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.menu_geminiAi);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, chatFragment).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_chat) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, chatFragment).commit();
                }
                if (menuItem.getItemId() == R.id.menu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, profileFragment).commit();
                }

                if(menuItem.getItemId() == R.id.menu_geminiAi){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, chatGeminiFragment).commit();
                }

                return true;
            }
        });
//        getFCMToken();
    }


//    void getFCMToken(){
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
//            if(task.isSuccessful()){
//                String token = task.getResult();
//                FirebaseUtil.currentUserDetails().update("fcmToken",token);
//
//            }
//        });
//    }
}