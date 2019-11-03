package com.mubita.foodorderapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mubita.foodorderapp.models.AppDatabase;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.OrderDao;

import java.util.List;

public class OrderRepository {
    private OrderDao orderDao;
    private LiveData<List<Order>> allOrders;

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDao = database.orderDao();
        allOrders = orderDao.getAllOrders();
    }

    public void insert(Order order) {
        new InsertOrderAsyncTask(orderDao).execute(order);
    }

    public void update(Order order) {
        new UpdateOrderAsyncTask(orderDao).execute(order);
    }

    public void delete(Order order) {
        new DeleteOrderAsyncTask(orderDao).execute(order);
    }

    public void deleteAllOrders() {
        new DeleteAllOrdersAsyncTask(orderDao).execute();
    }

    public LiveData<List<Order>> getAllOrders() {
        return allOrders;
    }

    private static class InsertOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        private InsertOrderAsyncTask(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            orderDao.insert(orders[0]);
            return null;
        }
    }

    private static class UpdateOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        private UpdateOrderAsyncTask(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            orderDao.update(orders[0]);
            return null;
        }
    }

    private static class DeleteOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        private DeleteOrderAsyncTask(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            orderDao.delete(orders[0]);
            return null;
        }
    }

    private static class DeleteAllOrdersAsyncTask extends AsyncTask<Void, Void, Void> {
        private OrderDao orderDao;

        private DeleteAllOrdersAsyncTask(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            orderDao.deleteAllOrders();
            return null;
        }
    }
}
