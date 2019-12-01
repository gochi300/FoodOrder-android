package com.mubita.foodorderapp;

import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.Product;
import com.mubita.foodorderapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class AppDataStore {
    private static AppDataStore _selfInstance = null;

    private User user;
    private Product product; // current product
    private List<Product> productList = new ArrayList<>();
    private List<Order> cartArrayList = new ArrayList<>();
    private List<Order> orderArrayList = new ArrayList<>();

    private int notifCount;
    private int totalItems = 2;
    private Double totalAmount = 0.00;
    /*timer*/
    private boolean isRunning = false;

    /**
     * Private constructor to prevent further instantiation
     */
    private AppDataStore() {
    }

    /**
     * Factory method to get the instance of this class. This method ensures
     * that this class will have one and only one instance at any point of
     * time. This is the only way to get the instance of this class. No other
     * way will be made available to the programmer to instantiate this class.
     *
     * @return the object of this class.
     */
    public static AppDataStore getInstance() {
        if (_selfInstance == null) {
            _selfInstance = new AppDataStore();
        }
        return _selfInstance;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Order> getCartArrayList() {
        return cartArrayList;
    }

    public void setCartArrayList(List<Order> cartArrayList) {
        this.cartArrayList = cartArrayList;
    }

    public List<Order> getOrderArrayList() {
        return orderArrayList;
    }

    public void setOrderArrayList(List<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getNotifCount() {
        return notifCount;
    }

    public void setNotifCount(int notifCount) {
        this.notifCount = notifCount;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
