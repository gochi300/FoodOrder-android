package com.mubita.foodorderapp.utilities;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.Notification;
import com.mubita.foodorderapp.repository.NotificationRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSync extends AppCompatActivity {

    ApiInterface apiInterface;
    NotificationRepository notificationRepository;

    // **
    public void getNotifications(Context context){
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        notificationRepository = new NotificationRepository(context);

        Call<List<Notification>> call = apiInterface.getNotificationsByUserId(1);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {

                if(response.body() != null){

                    List<Notification> notification1 = response.body();

                    for (Notification notification: notification1)
                    {
                        notification.setRead(false);
                        notificationRepository.insert(notification);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                System.out.println("Network error!");
            }
        });

    }

}
