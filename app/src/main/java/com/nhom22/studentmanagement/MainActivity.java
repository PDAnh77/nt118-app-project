package com.nhom22.studentmanagement;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserApi userApi = ApiClient.getClient().create(UserApi.class);
    String currentUserId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView = findViewById(R.id.hello_txt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentUserId = bundle.getString("user_id");
        }

        Call call = userApi.getUserById(currentUserId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    User currentUser = (User) response.body();
                    textView.setText("Xin chào " + currentUser.getUsername() + "!");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }

        });
    }
}