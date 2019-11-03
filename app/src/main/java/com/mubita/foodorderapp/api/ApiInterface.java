package com.mubita.foodorderapp.api;

import com.mubita.foodorderapp.models.Notification;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.Product;
import com.mubita.foodorderapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("login/{mobileNumber}/{password}")
    Call<User> authenticateUser(@Path("mobileNumber") String mobileNumber, @Path("password") String password);

    @POST("register")
    Call<User> registerUser(@Body User user);

    @GET("products")
    Call<List<Product>> getProducts();

    @POST("order")
    Call<List<Order>> postOrder(@Body List<Order> order);

    @GET("orders/{userId}")
    Call<List<Order>> getOrdersByUserId(@Path("userId") int userId);

    @GET("notifications/{userId}")
    Call<List<Notification>> getNotificationsByUserId(@Path("userId") int userId);
}