package com.nhom22.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.AcademicTranscriptApi;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.model.AcademicTranscript;
import com.nhom22.studentmanagement.data.model.Class;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradeDetailActivity extends AppCompatActivity {
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);
    AcademicTranscriptApi academicTranscriptApi = ApiClient.getClient().create(AcademicTranscriptApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String classId = intent.getStringExtra("classId");
        RecyclerView recyclerView = findViewById(R.id.grade_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyView = findViewById(R.id.emptyView);
        ImageButton btnBack = findViewById(R.id.back_button);
        TextView titleTextView = findViewById(R.id.title_txt);

        Call<Class> getClassInfo = classApi.getClassByIdentifier(classId);
        getClassInfo.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if(response.isSuccessful()) {
                    Class classFound = response.body();
                    if (classFound != null) {
                        titleTextView.setText("Bảng điểm lớp " + classFound.getClassName() + " (" + classFound.getClassCode() + ")");
                    }
                } else {
                    Log.d("GetClass", "Lỗi lấy thông tin lớp: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Log.d("ClassAPI", "Lỗi lấy thông tin lớp", t);
            }
        });

        Call<List<AcademicTranscript>> classGrade = academicTranscriptApi.getAcademicTranscriptsByClassIdentifier(classId);
        classGrade.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<AcademicTranscript>> call, Response<List<AcademicTranscript>> response) {
                if (response.isSuccessful()) {
                    List<AcademicTranscript> transcriptList = response.body();
                    if (transcriptList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("Chưa có bảng điểm");
                    } else {
                        AcademicTranscriptAdapter adapter = new AcademicTranscriptAdapter(transcriptList, "student");
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("GradeAPI", "Lỗi tải bảng điểm", t);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}