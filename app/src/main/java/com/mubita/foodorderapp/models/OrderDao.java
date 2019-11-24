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
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("DELETE FROM orders")
    void deleteAllOrders();

    @Query("SELECT * FROM orders Where orderNumber = :orderNumber ORDER BY orderNumber DESC")
    LiveData<List<Order>> getOrdersByOrderNumber(long orderNumber);

    @Query("SELECT * FROM orders GROUP BY orderNumber ORDER BY orderNumber DESC")
    LiveData<List<Order>> getAllOrders();

}
