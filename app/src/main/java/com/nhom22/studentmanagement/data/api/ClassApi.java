package com.nhom22.studentmanagement.data.api;

import com.nhom22.studentmanagement.data.model.Class;
import com.nhom22.studentmanagement.data.model.StudentIdRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClassApi {
    @GET("classes")
    Call<List<Class>> getAllClasses();
    @GET("classes/user/{userId}")
    Call<List<Class>> getClassesByUserId(@Path("userId") String userId);
    @GET("classes/{identifier}")
    Call<Class> getClassByIdentifier(@Path("identifier") String classIdentifier);
    @POST("classes")
    Call<Class> createClass(@Body Class newClass);
    @POST("classes/{classIdentifier}/student")
    Call<Class> addStudentToClass(@Path("classIdentifier") String classIdentifier, @Body StudentIdRequest request);
    @PUT("classes/{identifier}")
    Call<Class> updateClass(@Path("identifier") String classIdentifier, @Body Class updatedClass);
    @DELETE("classes/{classIdentifier}")
    Call<Void> deleteClass(@Path("classIdentifier") String classIdentifier);
    @DELETE("classes/{classIdentifier/student/{studentId}")
    Call<Void> removeStudentFromClass(@Path("classIdentifier") String classIdentifier, @Path("studentId") String studentId);
}
