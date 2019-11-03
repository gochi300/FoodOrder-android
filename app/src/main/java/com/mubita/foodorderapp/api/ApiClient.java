package com.mubita.foodorderapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // localhost
    public static String BASE_URL ="http://10.0.2.2/foodorder/public/api/";

    private static Retrofit instance = null;
    public static Retrofit getInstance(){
        if(instance == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!

            // **
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(7, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .readTimeout(7,TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();


            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return instance;
    }
}
