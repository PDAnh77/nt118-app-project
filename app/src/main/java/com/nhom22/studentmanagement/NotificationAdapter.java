package com.nhom22.studentmanagement;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom22.studentmanagement.data.ApiClient;
import com.nhom22.studentmanagement.data.api.ClassApi;
import com.nhom22.studentmanagement.data.api.NotificationApi;
import com.nhom22.studentmanagement.data.api.UserApi;
import com.nhom22.studentmanagement.data.model.Class;
import com.nhom22.studentmanagement.data.model.Notification;
import com.nhom22.studentmanagement.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notifications;
    private final NotificationActionListener listener;
    UserApi userApi = ApiClient.getClient().create(UserApi.class);
    ClassApi classApi = ApiClient.getClient().create(ClassApi.class);

    public NotificationAdapter(List<Notification> notifications, NotificationActionListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    public interface NotificationActionListener {
        void onAccept(Notification notification);
        void onReject(Notification notification);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        // Gọi API để lấy thông tin người dùng dựa trên studentId
        Call<User> callUser  = userApi.getUserById(notification.getStudentId());
        callUser.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User foundUser = response.body();
                    assert foundUser != null;
                    holder.tvUsername.setText(foundUser.getUsername());
                } else {
                    Log.e("UserAPI", "Lỗi tải thông tin người dùng: " + response.code());
                    holder.tvUsername.setText("Lỗi tải tên");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserAPI", "Gọi API thất bại", t);
                holder.tvUsername.setText("Lỗi tải tên");
            }
        });

        Call<Class> callClass = classApi.getClassByIdentifier(notification.getClassId());
        callClass.enqueue(new Callback<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Class> call, @NonNull Response<Class> response) {
                if (response.isSuccessful()) {
                    Class foundClass = response.body();
                    assert foundClass != null;
                    holder.tvMessage.setText("Muốn tham gia lớp " + foundClass.getClassName() + " (" + foundClass.getClassCode() + ")");
                } else {
                    Log.e("ClassAPI", "Lỗi tải thông tin lớp: " + response.code());
                    holder.tvMessage.setText("Lỗi tải thông tin lớp");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<Class> call, @NonNull Throwable t) {
                Log.e("ClassAPI", "Gọi API thất bại", t);
                holder.tvMessage.setText("Lỗi tải thông tin lớp");
            }
        });

        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) listener.onAccept(notification);
        });

        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) listener.onReject(notification);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvMessage;
        ImageButton btnAccept, btnReject;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
