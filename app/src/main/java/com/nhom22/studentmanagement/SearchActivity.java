package com.nhom22.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.model.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    String currentUserId = null;
    boolean isSearching = false;
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);
    TextView emptyView;
    private List<Class> availableClasses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        SharedPreferences sharedPreferences = getSharedPreferences("current_user", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("id", null);
        emptyView = findViewById(R.id.emptyView);
        EditText searchInput = findViewById(R.id.search_input);
        ImageButton searchButton = findViewById(R.id.search_button);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_search) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.bottom_result) {
                startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        loadAvailableClasses();

        searchButton.setOnClickListener(v -> {
            if (!isSearching) {
                isSearching = true;
                String query = searchInput.getText().toString().trim();
                if (query.isEmpty()) return;
                searchButton.setImageResource(R.drawable.search_off);

                List<Class> result = new ArrayList<>();
                for (Class c : availableClasses) {
                    if (c.getClassCode().equalsIgnoreCase(query)) {
                        result.add(c);
                        break;
                    }
                }

                RecyclerView classesRecyclerView = findViewById(R.id.class_list);
                classesRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                if (result.isEmpty()) {
                    classesRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("Không tìm thấy lớp hoặc đã tham gia.");
                } else {
                    classesRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    ClassAdapter classAdapter = new ClassAdapter(result);
                    classesRecyclerView.setAdapter(classAdapter);
                }
            } else {
                isSearching = false;
                searchInput.setText("");
                searchButton.setImageResource(R.drawable.search);

                RecyclerView classesRecyclerView = findViewById(R.id.class_list);
                classesRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                if (availableClasses.isEmpty()) {
                    classesRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("Không có lớp nào có thể tham gia.");
                } else {
                    classesRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    ClassAdapter classAdapter = new ClassAdapter(availableClasses);
                    classesRecyclerView.setAdapter(classAdapter);
                }
            }
        });
    }

    private void loadAvailableClasses() {
        Call<List<Class>> allClassesCall = classApi.getAllClasses();
        Call<List<Class>> joinedClassesCall = classApi.getClassesByUserId(currentUserId);

        joinedClassesCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> joinedResponse) {
                List<Class> joinedClasses = joinedResponse.body();
                if (joinedClasses == null) {
                    joinedClasses = new ArrayList<>();
                }

                List<Class> finalJoinedClasses = joinedClasses;

                allClassesCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Class>> call, Response<List<Class>> allResponse) {
                        RecyclerView classesRecyclerView = findViewById(R.id.class_list);
                        classesRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                        if (!allResponse.isSuccessful() || allResponse.body() == null) {
                            classesRecyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            return;
                        }

                        emptyView.setVisibility(View.GONE);
                        List<Class> allClasses = allResponse.body();
                        if (allClasses == null) {
                            allClasses = new ArrayList<>();
                        }
                        Log.d("ClassAPI", "All classes: " + allClasses);
                        availableClasses.clear();

                        for (Class c : allClasses) {
                            boolean isJoined = false;
                            for (Class joined : finalJoinedClasses) {
                                if (Objects.equals(joined.getClassCode(), c.getClassCode())) {
                                    isJoined = true;
                                    break;
                                }
                            }
                            if (!isJoined) {
                                availableClasses.add(c);
                            }
                        }

                        ClassAdapter classAdapter = new ClassAdapter(availableClasses);
                        classesRecyclerView.setAdapter(classAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Class>> call, Throwable t) {
                        Log.e("ClassAPI", "Lỗi tải tất cả lớp", t);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                Log.e("ClassAPI", "Lỗi tải lớp đã tham gia", t);
            }
        });
    }

}