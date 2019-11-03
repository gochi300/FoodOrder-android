package com.mubita.foodorderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mubita.foodorderapp.adapters.CheckoutListAdapter;
import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.Product;
import com.mubita.foodorderapp.utilities.PaymentConfig;
import com.mubita.foodorderapp.viewmodel.OrderViewModel;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    // **
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private Button btnConfirm;
    private TextView total_cost;
    // **
    private OrderViewModel orderViewModel;
    Spinner spinnerPaymentOptions;


    /*PAYPAL*/
    public static final int PAYPAL_REQUEST_CODE =7171;

    public static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaymentConfig.PAYPAL_CLIENT_ID);

    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        /*RecyclerView*/
        mRecyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(CheckoutActivity.this, LinearLayoutManager.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(CheckoutActivity.this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new CheckoutListAdapter(CheckoutActivity.this, AppDataStore.getInstance().getProductList());
        mRecyclerView.setAdapter(mAdapter);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderConfirmation();
            }
        });


        // Calculate total cost
        Double totalAmount = 0.00;

        for(Product product: AppDataStore.getInstance().getProductList()){
            totalAmount = totalAmount + product.getProductPrice();
        }

        AppDataStore.getInstance().setTotalAmount(totalAmount);

        // set total items
        AppDataStore.getInstance().setTotalItems(AppDataStore.getInstance().getProductList().size());

        total_cost = findViewById(R.id.total_cost);
        total_cost.setText("K"+AppDataStore.getInstance().getTotalAmount());


        List<String> stringList = new ArrayList<>();
        stringList.add("Paypal");
        stringList.add("Cash");

        spinnerPaymentOptions = findViewById(R.id.spinner_filter);
        spinnerPaymentOptions.setAdapter(new ArrayAdapter<>( this,R.layout.support_simple_spinner_dropdown_item, stringList));
    }

    public void showOrderConfirmation() {
        AlertDialog alertDialog = new AlertDialog.Builder(CheckoutActivity.this).create();
        alertDialog.setTitle("Place order");
        alertDialog.setMessage("Place order for K"+AppDataStore.getInstance().getTotalAmount());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // **
                        if(spinnerPaymentOptions.getSelectedItem().equals("Paypal")){
                            processPayment();
                        }
                        else{
                            processOrder();
                            dialog.dismiss();
                        }

                    }
                });
        alertDialog.show();
    }

    public void postOrder(List<Order> orderList){
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);

        Call<List<Order>> call = apiInterface.postOrder(orderList);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                //Toast.makeText(CartActivity.this, "Your order has been posted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "There was a problem!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processOrder() {
        // **
        List<Product> productList = AppDataStore.getInstance().getProductList();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //traverse arrayList and save to db
        List<Order> orderList = new ArrayList<>();

        for (Product product: productList)
        {
            int productId = product.getId();
            String productName = product.getProductName();
            Integer productPrice = product.getProductPrice();
            Integer productQty = product.getProductQty();

            // **
            Order order = new Order();
            order.setProductId(productId);
            order.setProductName(productName);
            order.setOrderNumber(timestamp.getTime());
            order.setUserId(1);
            order.setOrderQty(productQty);
            order.setTotalPrice(productPrice);
            order.setOrderStatus("pending");

            // **
            orderViewModel = ViewModelProviders.of(CheckoutActivity.this).get(OrderViewModel.class);
            orderViewModel.insert(order);

            orderList.add(order);

        }

        postOrder(orderList);
        Toast.makeText(CheckoutActivity.this, "Your order has been placed!", Toast.LENGTH_SHORT).show();
        // **
        AppDataStore.getInstance().getProductList().clear();
        // **
        startActivity(new Intent(CheckoutActivity.this, OrdersActivity.class));
    }

    public void processPayment(){
        amount = String.valueOf(AppDataStore.getInstance().getTotalAmount());
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), "USD", "Payment for order", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetailsActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}
