package com.example.mychat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.adapter.SearchUserRecyclerAdapter;
import com.example.mychat.model.UserModel;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {
    AutoCompleteTextView searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;
    SharedPreferences sharedPreferences;
    ArrayAdapter<String> searchAdapter;

    private static final String PREFS_NAME = "SearchPreferences";
    private static final String SEARCH_HISTORY_KEY = "SearchHistory";
    private static final int MAX_HISTORY_SIZE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user);


        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setupSearchHistory();

        searchInput.requestFocus();

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                searchInput.setError("Invalid Username");
                return;
            }
            saveSearchTerm(searchTerm);
            setupSearchRecyclerView(searchTerm);
        });
    }

    private void setupSearchHistory() {
        String[] searchHistory = getSearchHistory();
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchHistory);
        searchInput.setAdapter(searchAdapter);
        searchInput.setThreshold(1);
    }

    private void saveSearchTerm(String term) {
        String[] searchHistory = getSearchHistory();
        List<String> searchHistoryList = new ArrayList<>(Arrays.asList(searchHistory));

        searchHistoryList.remove(term);

        searchHistoryList.add(0, term);


        if (searchHistoryList.size() > MAX_HISTORY_SIZE) {
            searchHistoryList = searchHistoryList.subList(0, MAX_HISTORY_SIZE);
        }

        // Save the updated search history
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SEARCH_HISTORY_KEY, TextUtils.join(",", searchHistoryList));
        editor.apply();

        // Update the adapter
        searchAdapter.clear();
        searchAdapter.addAll(searchHistoryList);
        searchAdapter.notifyDataSetChanged();
    }

    private String[] getSearchHistory() {
        String history = sharedPreferences.getString(SEARCH_HISTORY_KEY, "");
        if (history.isEmpty()) {
            return new String[0];
        } else {
            return history.split(",");
        }
    }

    void setupSearchRecyclerView(String searchTerm) {
        Query query;
        if (AndroidUtil.isNumeric(searchTerm)) {
            query = com.example.mychat.utils.FirebaseUtil.allUserCollectionReference()
                    .whereGreaterThanOrEqualTo("phone", searchTerm)
                    .whereLessThanOrEqualTo("phone", searchTerm + '\uf8ff');
        } else {
            query = FirebaseUtil.allUserCollectionReference()
                    .whereGreaterThanOrEqualTo("userName", searchTerm)
                    .whereLessThanOrEqualTo("userName", searchTerm + '\uf8ff');
        }

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
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