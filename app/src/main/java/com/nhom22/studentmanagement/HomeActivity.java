package com.nhom22.studentmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.api.NotificationApi;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.Notification;
import com.nhom22.studentmanagement.data.model.User;
import com.nhom22.studentmanagement.data.model.Class;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    UserApi userApi = ApiClient.getClient().create(UserApi.class);
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);
    NotificationApi notificationApi = ApiClient.getClient().create(NotificationApi.class);
    String currentUserId = null;
    String currentUserRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView = findViewById(R.id.hello_txt);
        SharedPreferences sharedPreferences = getSharedPreferences("current_user", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("id", null);
        currentUserRole = sharedPreferences.getString("role", null);
        ImageButton btnNotification = findViewById(R.id.btnNotification);
        TextView titleTextView = findViewById(R.id.title_txt);
        TextView emptyView = findViewById(R.id.emptyView);

        Call<User> call = userApi.getUserById(currentUserId);
        call.enqueue(new Callback<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    User currentUser = (User) response.body();
                    assert currentUser != null;
                    textView.setText("Xin chào " + currentUser.getUsername() + "!");

                    if (Objects.equals(currentUser.getRole(), "teacher")) {
                        titleTextView.setText("Các lớp đang mở:");
                    } else {
                        titleTextView.setText("Các lớp đang tham gia:");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_result) {
                startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

        Call<List<Class>> callClasses = classApi.getClassesByUserId(currentUserId);
        callClasses.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                RecyclerView classesRecyclerView = findViewById(R.id.class_list);
                classesRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                if (!response.isSuccessful()) {
                    classesRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    emptyView.setVisibility(View.GONE);
                }

                ClassAdapter classAdapter = new ClassAdapter(response.body());
                classesRecyclerView.setAdapter(classAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("ClassAPI", "Lỗi tải danh sách lớp", t);
            }
        });

        btnNotification.setOnClickListener(v -> showNotificationBottomSheet());
    }

    private void showNotificationBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.notification_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gọi API lấy thông báo
        Call<List<Notification>> call = notificationApi.getAllNotifications();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Notification>> call, @NonNull Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notifications = response.body();
                    NotificationAdapter adapter = new NotificationAdapter(notifications, new NotificationAdapter.NotificationActionListener() {
                        @Override
                        public void onAccept(Notification notification) {
                            notification.setStatus(1);
                            updateNotificationStatus(notification);
                        }

                        @Override
                        public void onReject(Notification notification) {
                            notification.setStatus(2);
                            updateNotificationStatus(notification);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Không có thông báo nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Notification>> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi tải thông báo", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }

    private void updateNotificationStatus(Notification notification) {
        notificationApi.updateNotification(notification.getId(), notification).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Notification> call, @NonNull Response<Notification> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Đã cập nhật trạng thái: " + notification.getStatus(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Lỗi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Notification> call, @NonNull Throwable t) {
                Log.e("NotificationAPI", "Lỗi gọi API: " + t.getMessage(), t);
                Toast.makeText(HomeActivity.this, "Lỗi tải thông báo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}