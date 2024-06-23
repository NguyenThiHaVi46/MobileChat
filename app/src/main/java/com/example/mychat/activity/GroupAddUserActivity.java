package com.example.mychat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.adapter.SearchUserAddGroupRecyclerAdapter;
import com.example.mychat.adapter.AddedUserRecyclerAdapter;
import com.example.mychat.data.repository.UserRepository;
import com.example.mychat.models.User;
import com.example.mychat.utils.AndroidUtil;
import com.example.mychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAddUserActivity extends AppCompatActivity {
    EditText searchInput;
    TextView groupAddTextView;
    ImageButton backButton;
    RecyclerView recyclerViewSearch, recyclerViewAdduser;
    SearchUserAddGroupRecyclerAdapter adapter;
    AddedUserRecyclerAdapter addedUserAdapter;
    SharedPreferences sharedPreferences;
    private Map<User, Boolean> addedUsersMap = new HashMap<>();
    private static final String PREFS_NAME = "SearchPreferences";
    private static final String SEARCH_HISTORY_KEY = "SearchHistory";
    private static final int MAX_HISTORY_SIZE = 5;
    private static final long DEBOUNCE_DELAY = 300;
    private Handler handler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = findViewById(R.id.search_username_input);
        backButton = findViewById(R.id.back_btn);
        recyclerViewSearch = findViewById(R.id.search_user_recycler_view);
        recyclerViewAdduser = findViewById(R.id.add_user_recycler_view);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        groupAddTextView = findViewById(R.id.group_add_textView);

        backButton.setOnClickListener(v -> finish());

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
                    saveSearchTerm(searchTerm);
                    setupSearchRecyclerView(searchTerm);
                };
                handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
            }
        });

        groupAddTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                String listIdUser = FirebaseUtil.getChatRoomId(AndroidUtil.getUserIdsFromMap(addedUsersMap));
                if (listIdUser != null) {
                    intent.putExtra("listIdUser", listIdUser);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d("TAG", listIdUser);
                    startActivity(intent);
                } else {
                    // Handle the case when listIdUser is null
                    Toast.makeText(getApplicationContext(), "Unable to get chat room ID", Toast.LENGTH_SHORT).show();
                }

            }
        });

        setupAddedUserRecyclerView();
        UserRepository userRepository = new UserRepository(this);
        User user = userRepository.getUserById(FirebaseUtil.currentUserId());
        toggleUserInGroup(user);
    }

    private void saveSearchTerm(String term) {
        String[] searchHistory = getSearchHistory();
        List<String> searchHistoryList = new ArrayList<>(Arrays.asList(searchHistory));
        searchHistoryList.remove(term);
        searchHistoryList.add(0, term);

        if (searchHistoryList.size() > MAX_HISTORY_SIZE) {
            searchHistoryList = searchHistoryList.subList(0, MAX_HISTORY_SIZE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SEARCH_HISTORY_KEY, TextUtils.join(",", searchHistoryList));
        editor.apply();
    }

    private String[] getSearchHistory() {
        String history = sharedPreferences.getString(SEARCH_HISTORY_KEY, "");
        return history.isEmpty() ? new String[0] : history.split(",");
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

        adapter = new SearchUserAddGroupRecyclerAdapter(options, getApplicationContext(), this::toggleUserInGroup);
        adapter.updateAddedUsersMap(addedUsersMap);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setAdapter(adapter);
        adapter.startListening();
    }


    private void setupAddedUserRecyclerView() {
        addedUserAdapter = new AddedUserRecyclerAdapter(addedUsersMap, this);
        recyclerViewAdduser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAdduser.setAdapter(addedUserAdapter);
    }


    private void toggleUserInGroup(User user) {
        Boolean isAdded = addedUsersMap.get(user);
        if (isAdded != null && isAdded) {
            addedUsersMap.put(user, false); // Xóa người dùng khỏi nhóm
        } else {
            addedUsersMap.put(user, true); // Thêm người dùng vào nhóm
        }

        if (adapter != null) {
            adapter.updateAddedUsersMap(addedUsersMap); // Cập nhật danh sách addedUsers trong adapter
        } else {
            Log.e("GroupAddUserActivity", "Adapter is null");
            // Xử lý khi adapter là null
        }

        if (addedUserAdapter != null) {
            addedUserAdapter.setUserMap(addedUsersMap); // Cập nhật danh sách người dùng trong addedUserAdapter
            groupAddTextView.setVisibility(addedUsersMap.containsValue(true) ? View.VISIBLE : View.GONE); // Cập nhật trạng thái hiển thị
        } else {
            Log.e("GroupAddUserActivity", "AddedUserAdapter is null");
            // Xử lý khi addedUserAdapter là null
        }
    }






    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.startListening();
    }
}