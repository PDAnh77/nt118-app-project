package com.nhom22.studentmanagement.data.api;

import com.nhom22.studentmanagement.data.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationApi {
    @GET("notifications")
    Call<List<Notification>> getAllNotifications();
    @GET("notifications/{id}")
    Call<Notification> getNotificationById(@Path("id") String notificationId);
    @GET("notifications/user/{userId}")
    Call<List<Notification>> getNotificationsByUserId(@Path("userId") String userId);
    @GET("notifications/class/{classId}")
    Call<List<Notification>> getNotificationsByClassId(@Path("classId") String classId);
    @GET("notifications/user/{userId}/class/{classId}")
    Call<List<Notification>> getNotificationsByUserIdAndClassId(@Path("userId") String userId, @Path("classId") String classId);
    @POST("notifications")
    Call<Notification> createNotification(@Body Notification newNotification);
    @PUT("notifications/{id}")
    Call<Notification> updateNotification(@Path("id") String notificationId, @Body Notification updatedNotification);
    @DELETE("notifications/{id}")
    Call<Void> deleteNotification(@Path("id") String notificationId);
    @DELETE("notifications/user/{userId}")
    Call<Void> deleteNotificationsByUserId(@Path("userId") String userId);
}
