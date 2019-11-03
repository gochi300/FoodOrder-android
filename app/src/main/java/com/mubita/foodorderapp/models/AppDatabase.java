package com.mubita.foodorderapp.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class, Order.class, Notification.class, User.class }, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ProductDao productDao();

    public abstract OrderDao orderDao();

    public abstract NotificationDao notificationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_db")
                    .fallbackToDestructiveMigration()
                    /*remove later*/
                    .allowMainThreadQueries()
                    // **
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // **
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
