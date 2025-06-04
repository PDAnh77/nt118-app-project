package com.nhom22.studentmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupTabFragment extends Fragment {
    UserApi userApi = ApiClient.getClient().create(UserApi.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        Spinner roleOptions = view.findViewById(R.id.my_spinner);
        EditText username = view.findViewById(R.id.signup_username);
        EditText password = view.findViewById(R.id.signup_password);
        EditText confirmPassword = view.findViewById(R.id.signup_confirm);
        Button signupBtn = view.findViewById(R.id.signup_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.dropdown_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roleOptions.setAdapter(adapter);

        signupBtn.setOnClickListener(v -> {
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();
            String userRole = (roleOptions.getSelectedItemPosition() == 0) ? "student" : "teacher";

            if (!passwordStr.equals(confirmPasswordStr)) {
                Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            Call<User> call = userApi.signup(new User(username.getText().toString(), password.getText().toString(), userRole));
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        password.setText("");
                        confirmPassword.setText("");
                        roleOptions.setSelection(0);
                    } else {
                        Toast.makeText(getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}