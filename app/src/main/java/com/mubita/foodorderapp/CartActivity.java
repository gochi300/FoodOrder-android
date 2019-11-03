package com.mubita.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mubita.foodorderapp.adapters.CartListAdapter;

public class CartActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private TextView cartTextView;
    private LinearLayout cartLinearLayout;
    private Button btnProceedToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartTextView = findViewById(R.id.cartTextView);
        cartLinearLayout = findViewById(R.id.cartLinearLayout);

        if(AppDataStore.getInstance().getProductList().isEmpty()){
            cartTextView.setVisibility(View.VISIBLE);
        } else {
            getProducts();
        }
    }

    private void getProducts() {
        /*RecyclerView*/
        mRecyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(CartActivity.this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new CartListAdapter(CartActivity.this, AppDataStore.getInstance().getProductList());
        mRecyclerView.setAdapter(mAdapter);


        btnProceedToCart = findViewById(R.id.btnProceedToCart);
        btnProceedToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });

        // **
        cartLinearLayout.setVisibility(View.VISIBLE);

    }
}
