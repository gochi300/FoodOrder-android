package com.mubita.foodorderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mubita.foodorderapp.adapters.OrderListAdapter;
import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    // **
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private OrderViewModel orderViewModel;
    private TextView textView1;
    // **
    private ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        textView1 = findViewById(R.id.textView1);

        getOrdersFromDb();
    }

    public void getOrdersFromDb(){

        /*RecyclerView*/
        mRecyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(OrdersActivity.this, LinearLayoutManager.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(OrdersActivity.this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final OrderListAdapter adapter = new OrderListAdapter(OrdersActivity.this, orders);
        mRecyclerView.setAdapter(adapter);


        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> orders1) {
                adapter.setmOrders(orders1);
                if(!orders1.isEmpty()){
                    textView1.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // **
        //getOrdersFromAPI();

    }

    public void getOrdersFromAPI(){
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);

        Call<List<Order>> call = apiInterface.getOrdersByUserId(1);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.body() != null){
                    List<Order> orderList = response.body();

                    for (Order order: orderList )
                    {
                        orderViewModel.insert(order);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrdersActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
