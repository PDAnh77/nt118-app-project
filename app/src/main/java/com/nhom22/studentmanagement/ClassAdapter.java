package com.nhom22.studentmanagement;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.Class;
import com.nhom22.studentmanagement.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<Class> classList;
    private OnItemClickListener listener;
    UserApi userApi = ApiClient.getClient().create(UserApi.class);

    public interface OnItemClickListener {
        void onItemClick(Class selectedClass);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ClassAdapter(List<Class> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class classItem = classList.get(position);
        holder.classIdText.setText(classItem.getClassCode());
        holder.classNameText.setText(classItem.getClassName());

        Call<User> callUser = userApi.getUserById(classItem.getTeacherId());
        callUser.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User foundUser = response.body();
                    assert foundUser != null;

                    if (foundUser.getFullname() == null) {
                        holder.teacherNameText.setText(foundUser.getUsername());
                    } else {
                        holder.teacherNameText.setText(foundUser.getFullname());
                    }
                } else {
                    Log.e("UserAPI", "Lỗi tải thông tin người dùng: " + response.code());
                    holder.teacherNameText.setText("Lỗi tải tên");
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("UserAPI", "Gọi API thất bại", t);
                holder.teacherNameText.setText("Lỗi tải tên");
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(classItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classIdText, classNameText, teacherNameText;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classIdText = itemView.findViewById(R.id.classIdText);
            classNameText = itemView.findViewById(R.id.classNameText);
            teacherNameText = itemView.findViewById(R.id.teacherNameText);
        }
    }
}

