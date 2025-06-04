package com.nhom22.studentmanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    UserApi userApi = ApiClient.getClient().create(UserApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_profile) {
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
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        EditText emailText = findViewById(R.id.email_txt);
        EditText birthdayText = findViewById(R.id.birthday_txt);
        EditText fullnameText = findViewById(R.id.fullname_txt);
        EditText academicYearText = findViewById(R.id.academicYear_txt);
        TextView roleText = findViewById(R.id.role_txt);
        TextView usernameText = findViewById(R.id.username_txt);
        ConstraintLayout academicYearInputField = findViewById(R.id.academicYear_inputfield);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnLogout = findViewById(R.id.btnLogout);
        SharedPreferences sharedPreferences = getSharedPreferences("current_user", MODE_PRIVATE);
        String currentUserRole = sharedPreferences.getString("role", "");
        String currentUserId = sharedPreferences.getString("id", "");

        roleText.setText(currentUserRole);
        AtomicBoolean editMode = new AtomicBoolean(false);

        btnCancel.setVisibility(View.GONE);
        emailText.setEnabled(false);
        birthdayText.setEnabled(false);
        fullnameText.setEnabled(false);
        academicYearText.setEnabled(false);

        if (currentUserRole.equals("teacher")) {
            academicYearInputField.setVisibility(View.GONE);
        }

        Call<User> getUser = userApi.getUserById(currentUserId);
        getUser.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    User currentUser = response.body();
                    emailText.setText(currentUser.getUsername());
                    if (currentUser.getBrithday() != null) {
                        birthdayText.setText(new SimpleDateFormat("dd/MM/yyyy").format(currentUser.getBrithday()));
                    } else {
                        birthdayText.setText("");
                    }
                    fullnameText.setText(currentUser.getFullname());
                    if(currentUser.getAcademicYear() != null) {
                        academicYearText.setText(currentUser.getAcademicYear().toString());
                    } else {
                        academicYearText.setText("");
                    }
                    usernameText.setText(currentUser.getUsername());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        birthdayText.setFocusable(false);

        birthdayText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ProfileActivity.this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        Date selectedDate = selectedCalendar.getTime();

                        // Chuyển sang timestamp (epoch millis)
                        long timestamp = selectedDate.getTime();
                        birthdayText.setTag(timestamp);
                        birthdayText.setText(new SimpleDateFormat("dd/MM/yyyy").format(selectedDate));
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnEdit.setOnClickListener(v -> {
            if (!editMode.get()) {
                emailText.setEnabled(true);
                birthdayText.setEnabled(true);
                fullnameText.setEnabled(true);
                academicYearText.setEnabled(true);
                editMode.set(true);
                btnEdit.setText("Lưu");
                btnCancel.setVisibility(View.VISIBLE);
            } else {
                // Lấy timestamp ngày sinh đã lưu trong tag
                Object birthdayTag = birthdayText.getTag();
                Date birthdayDate = null;
                if (birthdayTag instanceof Long) {
                    birthdayDate = new Date((Long) birthdayTag);
                }

                Integer academicYearInt = null;
                try {
                    String academicYearStr = academicYearText.getText().toString().trim();
                    if (!academicYearStr.isEmpty()) {
                        academicYearInt = Integer.parseInt(academicYearStr);
                    }
                } catch (NumberFormatException e) {
                    academicYearInt = null;
                }

                User updatedUser = new User(
                        emailText.getText().toString(),
                        fullnameText.getText().toString(),
                        birthdayDate,
                        academicYearInt
                );

                Call<User> updateUser = userApi.updateUser(currentUserId, updatedUser);
                updateUser.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            emailText.setEnabled(false);
                            birthdayText.setEnabled(false);
                            fullnameText.setEnabled(false);
                            academicYearText.setEnabled(false);
                            editMode.set(false);
                            btnEdit.setText("Chỉnh sửa");
                            btnCancel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            emailText.setEnabled(false);
            birthdayText.setEnabled(false);
            fullnameText.setEnabled(false);
            academicYearText.setEnabled(false);
            editMode.set(false);
            btnEdit.setText("Chỉnh sửa");
            btnCancel.setVisibility(View.GONE);
        });
    }
}