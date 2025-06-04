package com.nhom22.studentmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTabFragment extends Fragment {
    UserApi userApi = ApiClient.getClient().create(UserApi.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("current_user", Context.MODE_PRIVATE);
        EditText username = view.findViewById(R.id.login_username);
        EditText password = view.findViewById(R.id.login_password);
        Button loginBtn = view.findViewById(R.id.login_button);

        loginBtn.setOnClickListener(v -> {
            Call<String> callLogin = userApi.login(new User(username.getText().toString(), password.getText().toString()));
            callLogin.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String currentUserId = response.body();
                        sharedPreferences.edit().putString("id", currentUserId).apply();

                        Call<User> callUserData = userApi.getUserById(currentUserId);
                        callUserData.enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User currentUser = response.body();
                                    sharedPreferences.edit().putString("role", currentUser.getRole()).apply();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }  else if (response.code() == 401) {
                        Toast.makeText(getContext(), "Username hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}