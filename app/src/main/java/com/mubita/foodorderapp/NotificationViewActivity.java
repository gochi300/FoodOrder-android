package com.mubita.foodorderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mubita.foodorderapp.adapters.ReceiptAdapter;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewActivity extends AppCompatActivity {

    public TextView notificationMessageTextView;
    public TextView createdAtTextView;
    // **
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Order> orderList = new ArrayList<>();
    // **
    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        Intent intent = getIntent();
        String notificationSubject = intent.getStringExtra("notificationSubject");
        String notificationMessage = intent.getStringExtra("notificationMessage");
        long orderNumber = intent.getLongExtra("orderNumber", 0);
        String createdAt = intent.getStringExtra("createdAt");

        //Initialize actionbar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(notificationSubject);

        notificationMessageTextView = findViewById(R.id.notificationInfo);
        createdAtTextView = findViewById(R.id.textView6);

        notificationMessageTextView.setText(notificationMessage);
        createdAtTextView.setText(createdAt);
        
        // **
        if(orderNumber != 0){
            showReceipt(orderNumber);
        }
    }

    private void showReceipt(long orderNumber) {
        /*RecyclerView*/
        mRecyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(NotificationViewActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ReceiptAdapter adapter = new ReceiptAdapter(NotificationViewActivity.this, orderList);
        mRecyclerView.setAdapter(adapter);


        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.getOrdersByOrderNumber(orderNumber).observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> orderList) {
                adapter.setmProducts(orderList);
                if(!orderList.isEmpty()){
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
