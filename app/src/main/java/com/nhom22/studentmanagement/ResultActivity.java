package com.nhom22.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.AcademicTranscriptApi;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.model.AcademicTranscript;
import com.nhom22.studentmanagement.data.model.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    AcademicTranscriptApi academicTranscriptApi = ApiClient.getClient().create(AcademicTranscriptApi.class);
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_result);
        SharedPreferences sharedPreferences = getSharedPreferences("current_user", MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("id", null);
        String currentUserRole = sharedPreferences.getString("role", null);
        TextView titleTextView = findViewById(R.id.title_txt);

        if (Objects.equals(currentUserRole, "teacher")) {
            titleTextView.setText("Bảng điểm các lớp:");
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_result) {
                return true;
            } else if (itemId == R.id.bottom_search) {
                if (currentUserRole.equals("teacher")) {
                    startActivity(new Intent(getApplicationContext(), CreateClassActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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

        RecyclerView recyclerView = findViewById(R.id.grade_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyView = findViewById(R.id.emptyView);


        if (Objects.equals(currentUserRole, "teacher")) {
            Call<List<Class>> getClasses = classApi.getClassesByUserId(currentUserId);
            getClasses.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() == null || response.body().isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText("Chưa có lớp nào");
                        } else {
                            List<Class> classList = response.body();

                            ClassAdapter classAdapter = new ClassAdapter(classList);
                            recyclerView.setAdapter(classAdapter);

                            classAdapter.setOnItemClickListener(ResultActivity.this::showClassGrades);
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("ClassAPI", "Lỗi tải danh sách lớp", t);
                }
            });
        } else {
            // Gọi API để lấy bảng điểm theo studentId
            Call<List<AcademicTranscript>> getStudentGrades = academicTranscriptApi.getAcademicTranscriptsByStudentId(currentUserId);
            getStudentGrades.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<AcademicTranscript>> call, Response<List<AcademicTranscript>> response) {
                    if (response.isSuccessful()) {
                        List<AcademicTranscript> transcriptList = response.body();

                        if (transcriptList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText("Chưa có bảng điểm");
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                            AcademicTranscriptAdapter adapter = new AcademicTranscriptAdapter(transcriptList, "class");
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("Không thể tải bảng điểm");
                    }
                }

                @Override
                public void onFailure(Call<List<AcademicTranscript>> call, Throwable t) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("Lỗi kết nối: " + t.getMessage());
                }
            });
        }
    }

    private void showClassGrades(Class selectedClass) {
        Intent intent = new Intent(this, GradeDetailActivity.class);
        intent.putExtra("classId", selectedClass.getId());
        startActivity(intent);
    }
}
