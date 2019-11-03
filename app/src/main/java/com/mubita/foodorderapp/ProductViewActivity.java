package com.mubita.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.Product;

public class ProductViewActivity extends AppCompatActivity {

    private ImageView answerImageView1;
    private TextView productNameTextView,priceTextView, quantityTextView;
    private Button btnBuyNow , btnAddToCart , btnAdd, btnRemove;

    private String productName;
    private Integer productPrice;
    String webUrl = "http://10.0.2.2/foodorder/public/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        // Get the transferred data from source activity.
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        productName = intent.getStringExtra("productName");
        productPrice = intent.getIntExtra("productPrice", 0);
        String description = intent.getStringExtra("description");
        String myImage = intent.getStringExtra("myImage");

        //Initialize actionbar and title
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(name);

        answerImageView1 = findViewById(R.id.answerImageView1);
        this.productNameTextView = findViewById(R.id.productName);
        priceTextView = findViewById(R.id.priceTextView);
        quantityTextView = findViewById(R.id.quantityTextView);
        btnAdd = findViewById(R.id.action_add);
        btnRemove = findViewById(R.id.action_remove);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // **
        //answerImageView1.setImageResource(answerFile1b);
        Glide.with(this)
                .load(webUrl+myImage)
                .into(answerImageView1);
        this.productNameTextView.setText(productName);
        priceTextView.setText("K"+productPrice);

        // add product
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pCount1 = quantityTextView.getText().toString();
                int pCount2 = Integer.parseInt(pCount1);
                pCount2++;
                quantityTextView.setText(Integer.toString(pCount2));
            }
        });

        // remove product
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pCount1 = quantityTextView.getText().toString();
                int pCount2 = Integer.parseInt(pCount1);
                if(pCount2 > 1){
                    pCount2--;
                    quantityTextView.setText(Integer.toString(pCount2));
                }
            }
        });


        // remove product
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDataStore.getInstance().getProductList().clear();
                Product product = AppDataStore.getInstance().getProduct();
                product.setProductQty(Integer.parseInt(quantityTextView.getText().toString()));
                AppDataStore.getInstance().getProductList().add(product);
                startActivity(new Intent(ProductViewActivity.this, CheckoutActivity.class));
            }
        });

        // remove product
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cartItems = AppDataStore.getInstance().getTotalItems();
                cartItems++;
                AppDataStore.getInstance().setTotalItems(cartItems);
                Toast.makeText(ProductViewActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                //**
                Product product = AppDataStore.getInstance().getProduct();
                product.setProductQty(Integer.parseInt(quantityTextView.getText().toString()));
                AppDataStore.getInstance().getProductList().add(product);
                // **
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductViewActivity.this, AppDataStore.getInstance().getProductList().size(), R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            startActivity(new Intent(ProductViewActivity.this,CartActivity.class));
            return true;
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(ProductViewActivity.this,NotificationsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
