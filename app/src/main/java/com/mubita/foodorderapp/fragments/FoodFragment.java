package com.mubita.foodorderapp.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mubita.foodorderapp.R;
import com.mubita.foodorderapp.adapters.ProductsAdapter;
import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.Product;
import com.mubita.foodorderapp.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ApiInterface apiInterface;
    // **
    private View view;
    private ProductsAdapter productsAdapter;
    private ProductViewModel productViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Product> products = new ArrayList<>();
    private String category = "food";


    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food, container, false);

        // **
        swipeRefreshLayout = view.findViewById(R.id.paymentsPullRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        getProductsFromDb();

        return view;
    }

    private void getProductsFromDb() {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        productsAdapter = new ProductsAdapter(requireActivity(), products);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),1));
        recyclerView.setAdapter(productsAdapter);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getProductByCategory(category).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> productList1) {
                productsAdapter.setmProducts(productList1);

            }
        });

        // **
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }

        // **
        getProductsFromApi();
    }

    private void getProductsFromApi() {
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        Call<List<Product>> call = apiInterface.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body()!= null){
                    List<Product> productList1 = response.body();

                    for (Product product:productList1)
                    {
                        productViewModel.insert(product);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                System.out.println("Failed");

            }
        });

    }

    @Override
    public void onRefresh() {
        products.clear();
        getProductsFromDb();
    }

}
