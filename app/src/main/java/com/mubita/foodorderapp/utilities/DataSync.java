package com.mubita.foodorderapp.utilities;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mubita.foodorderapp.OrdersActivity;
import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.AppDatabase;
import com.mubita.foodorderapp.models.Notification;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.OrderDao;
import com.mubita.foodorderapp.repository.NotificationRepository;
import com.mubita.foodorderapp.repository.OrderRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSync extends AppCompatActivity {

    ApiInterface apiInterface;
    NotificationRepository notificationRepository;
    OrderRepository orderRepository;

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

    // **
    public void getOrders(Context context){
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        orderRepository = new OrderRepository(context);

        Call<List<Order>> call = apiInterface.getOrdersByUserId(1);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.body() != null){
                    List<Order> orderList = response.body();

                    orderRepository.deleteAllOrders();
                    for (Order order: orderList )
                    {
                        orderRepository.insert(order);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                System.out.println("Network error!");
            }
        });

    }

}
