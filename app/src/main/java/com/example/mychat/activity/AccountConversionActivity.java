package com.example.mychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.adapter.AccountConversionAdapter;
import com.example.mychat.data.RoomData.Data;
import com.example.mychat.data.repository.UserRepository;
import com.example.mychat.fragment.ProfileFragment;
import com.example.mychat.models.User;

import java.util.List;

public class AccountConversionActivity extends AppCompatActivity {

    ImageButton backBtn,addUserBtn;
    RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_conversion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        backBtn = findViewById(R.id.ac_back_btn);
        addUserBtn = findViewById(R.id.ac_add_user);
        recyclerView = findViewById(R.id.ac_recycler_view);

        backBtn.setOnClickListener(new View.OnClickListener() { // quay lai
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addUserBtn.setOnClickListener(new View.OnClickListener() { // them 1 user chuyen sang LoginPhoneNumberActivity
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginPhoneNumberActivity.class));
            }
        });

        UserRepository userRepository = new UserRepository(this);

        List<User> users= userRepository.getAllUser(); // lay list user trong sql hien thi trong adapter
        recyclerView.setLayoutManager(new GridLayoutManager(this,1)); // xet layout trong adapter
        AccountConversionAdapter adapter = new AccountConversionAdapter(users,this);
        recyclerView.setAdapter(adapter);// chay adapter

    }
}