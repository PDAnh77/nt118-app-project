package com.nhom22.studentmanagement.data.api;

import com.nhom22.studentmanagement.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @POST("login")
    Call<String> login(@Body User user);
    @POST("signup")
    Call<User> signup(@Body User user);
    @GET("users")
    Call<List<User>> getAllUsers();
    @GET("users/{userid}")
    Call<User> getUserById(@Path("userid") String userId);
    @PUT("users/{userid}")
    Call<User> updateUser(@Path("userid") String userId, @Body User user);
    @DELETE("user/{userid}")
    Call<Void> deleteUser(@Path("userid") String userId);
}
