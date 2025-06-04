package com.nhom22.studentmanagement.data.api;

import com.nhom22.studentmanagement.data.model.AcademicTranscript;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AcademicTranscriptApi {
    @GET("grades")
    Call<List<AcademicTranscript>> getAllAcademicTranscripts();
    @GET("grades/{id}")
    Call<AcademicTranscript> getAcademicTranscriptById(@Path("id") String transcriptId);
    @GET("grades/class/{identifier}")
    Call<List<AcademicTranscript>> getAcademicTranscriptsByClassIdentifier(@Path("userId") String userId);
    @GET("grades/student/{studentId}")
    Call<List<AcademicTranscript>> getAcademicTranscriptsByStudentId(@Path("studentId") String studentId);
    @GET("grades/student/{studentId}/class/{identifier}")
    Call<AcademicTranscript> getAcademicTranscriptByStudentIdAndClassIdentifier(@Path("studentId") String studentId, @Path("identifier") String classIdentifier);
    @POST("grades")
    Call<AcademicTranscript> createAcademicTranscript(@Body AcademicTranscript newTranscript);
    @PUT("grades/{id}")
    Call<AcademicTranscript> updateAcademicTranscript(@Path("id") String transcriptId, @Body AcademicTranscript updatedTranscript);
    @DELETE("grades/{id}")
    Call<Void> deleteAcademicTranscript(@Path("id") String transcriptId);
}
