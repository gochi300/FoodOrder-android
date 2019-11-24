package com.mubita.foodorderapp.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Notification notification);

    @Update
    void update(Notification notification);

    @Delete
    void delete(Notification notification);

    @Query("DELETE FROM notifications")
    void deleteAllNotifications();

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    LiveData<List<Notification>> getAllNotifications();

    @Query("SELECT * FROM notifications WHERE read = 0 ")
    LiveData<List<Notification>> getUnreadNotifications();

}
