package com.nhom22.studentmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.model.AcademicTranscript;
import com.nhom22.studentmanagement.data.model.Grade;
import com.nhom22.studentmanagement.data.model.Class;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademicTranscriptAdapter extends RecyclerView.Adapter<AcademicTranscriptAdapter.AcademicTranscriptHolder> {
    private List<AcademicTranscript> transcriptList;
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);

    public AcademicTranscriptAdapter(List<AcademicTranscript> transcriptList) {
        this.transcriptList = transcriptList;
    }

    @NonNull
    @Override
    public AcademicTranscriptHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new AcademicTranscriptHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcademicTranscriptHolder holder, int position) {
        AcademicTranscript transcript = transcriptList.get(position);
        Grade grade = transcript.getGrade();

        holder.tvDiemQT.setText(grade.getProcess() != null ? String.valueOf(grade.getProcess()) : "0");
        holder.tvDiemGK.setText(grade.getMidterm() != null ? String.valueOf(grade.getMidterm()) : "0");
        holder.tvDiemCK.setText(grade.getFinalGrade() != null ? String.valueOf(grade.getFinalGrade()) : "0");

        Double average = grade.getAverageGrade();
        holder.tvDiemTB.setText(average != null ? String.format("%.2f", average) : "0.00");

        // Gọi API để lấy tên lớp dựa trên classId
        Call<Class> classCall = classApi.getClassByIdentifier(transcript.getClassId());
        classCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    Class classFound = response.body();
                    if (classFound != null) {
                        holder.tvMaLop.setText(classFound.getClassCode());
                    }
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                // Xử lý lỗi nếu có
            }
        });
    }

    @Override
    public int getItemCount() {
        return transcriptList.size();
    }

    static class AcademicTranscriptHolder extends RecyclerView.ViewHolder {
        TextView tvMaLop, tvDiemQT, tvDiemGK, tvDiemCK, tvDiemTB;

        public AcademicTranscriptHolder(@NonNull View itemView) {
            super(itemView);
            tvMaLop = itemView.findViewById(R.id.tvMaLop);
            tvDiemQT = itemView.findViewById(R.id.tvDiemQT);
            tvDiemGK = itemView.findViewById(R.id.tvDiemGK);
            tvDiemCK = itemView.findViewById(R.id.tvDiemCK);
            tvDiemTB = itemView.findViewById(R.id.tvDiemTB);
        }
    }
}
