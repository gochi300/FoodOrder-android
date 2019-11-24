package com.mubita.foodorderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mubita.foodorderapp.adapters.NotificationAdapter;
import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.Notification;
import com.mubita.foodorderapp.viewmodel.NotificationViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    // **
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Notification> notifications = new ArrayList<>();
    private TextView textView1;
    // **
    private NotificationViewModel notificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        textView1 = findViewById(R.id.textView1);
        getNotificationsFromDb();
    }

    public void getNotificationsFromDb(){

        /*RecyclerView*/
        mRecyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(NotificationsActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final NotificationAdapter adapter = new NotificationAdapter(NotificationsActivity.this, notifications);
        mRecyclerView.setAdapter(adapter);


        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        notificationViewModel.getAllNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications1) {
                adapter.setmNotifications(notifications1);
                if(!notifications1.isEmpty()){
                    textView1.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // **
        getNotificationsFromAPI();

    }

    public void getNotificationsFromAPI(){
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);

        Call<List<Notification>> call = apiInterface.getNotificationsByUserId(1);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {

                if(response.body() != null){

                    List<Notification> notification1 = response.body();

                    for (Notification notification: notification1)
                    {
                        notification.setRead(false);
                        notificationViewModel.insert(notification);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Toast.makeText(NotificationsActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
