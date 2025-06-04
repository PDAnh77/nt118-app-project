package com.nhom22.studentmanagement;

import android.content.Intent;
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

        EditText username = view.findViewById(R.id.login_username);
        EditText password = view.findViewById(R.id.login_password);
        Button loginBtn = view.findViewById(R.id.login_button);

        loginBtn.setOnClickListener(v -> {
            Call<String> call = userApi.login(new User(username.getText().toString(), password.getText().toString()));
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String currentUserId = response.body();
                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", currentUserId);

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().finish();
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