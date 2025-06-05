package com.nhom22.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.model.Class;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClassActivity extends AppCompatActivity {
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        SharedPreferences sharedPreferences = getSharedPreferences("current_user", MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("id", null);

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

        EditText className = findViewById(R.id.class_name_txt);
        EditText classCode = findViewById(R.id.class_code_txt);
        EditText classDescription = findViewById(R.id.class_description_txt);
        TextView emptyView = findViewById(R.id.emptyView);
        Button confirmButton = findViewById(R.id.btnConfirm);

        confirmButton.setOnClickListener(v -> {
            if (className.getText().toString().isEmpty() || classCode.getText().toString().isEmpty()) {
                emptyView.setText("Vui lòng nhập đầy đủ mã lớp và tên lớp");
                emptyView.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    emptyView.setVisibility(View.GONE);
                }, 6000);
            } else {
                Class createdClass = new Class(classCode.getText().toString(), className.getText().toString(), classDescription.getText().toString(), currentUserId);
                Call<Class> createClass = classApi.createClass(createdClass);
                createClass.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            className.setText("");
                            classCode.setText("");
                            classDescription.setText("");

                            emptyView.setText("Tạo lớp học thành công!");
                            emptyView.setVisibility(View.VISIBLE);

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                emptyView.setVisibility(View.GONE);
                            }, 6000);
                        } else {
                            Log.d("CreateClass", "Lỗi tạo lớp học: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                         Log.d("CreateClass", "Lỗi tạo lớp học: " + t.getMessage());
                    }
                });
            }
        });

    }
}