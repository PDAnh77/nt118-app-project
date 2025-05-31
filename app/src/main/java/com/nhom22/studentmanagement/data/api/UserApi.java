package com.nhom22.studentmanagement.data.api;

import com.nhom22.studentmanagement.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("login")
    Call<String> login(@Body User user);

    @POST("signup")
    Call<User> signup(@Body User user);

    @GET("users/{userid}")
    Call<User> getUserById(@Path("userid") String userId);
}
