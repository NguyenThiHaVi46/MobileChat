package com.example.mychat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.adapter.SearchUserRecyclerAdapter;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {
    EditText searchInput;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;
    SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SearchPreferences";

    private static final long DEBOUNCE_DELAY = 300;
    private Handler handler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = findViewById(R.id.search_username_input);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        backButton.setOnClickListener(v -> {
            finish();
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(searchRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchTerm = s.toString();
                searchRunnable = () -> {
                    if(searchTerm.equals("")){
                        return;
                    }
                    setupSearchRecyclerView(searchTerm);
                };

                handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
            }
        });
    }



    void setupSearchRecyclerView(String searchTerm) {
        Query query;
        if (AndroidUtil.isNumeric(searchTerm)) {
            query = FirebaseUtil.allUserCollectionReference()
                    .whereGreaterThanOrEqualTo("phoneNumber", searchTerm)
                    .whereLessThanOrEqualTo("phoneNumber", searchTerm + '\uf8ff');
        } else {
            query = FirebaseUtil.allUserCollectionReference()
                    .whereGreaterThanOrEqualTo("username", searchTerm)
                    .whereLessThanOrEqualTo("username", searchTerm + '\uf8ff');
        }

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }
}
