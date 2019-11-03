package com.mubita.foodorderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mubita.foodorderapp.AppDataStore;
import com.mubita.foodorderapp.ProductViewActivity;
import com.mubita.foodorderapp.R;
import com.mubita.foodorderapp.models.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productList;


    public ProductsAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.grid_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String webUrl = "http://10.0.2.2/foodorder/public/images/";
        holder.mRecipe.setText(productList.get(position).getProductName());
        holder.mPrice.setText("K"+ productList.get(position).getProductPrice());
        //holder.thumbnail.setImageResource(productList.get(position).getThumbnail());
        Glide.with(mContext)
                .load(webUrl+productList.get(position).getProductImage())
                .into(holder.thumbnail);
        holder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppDataStore.getInstance().setProduct(productList.get(position));

                Intent intent = new Intent(mContext, ProductViewActivity.class);
                // passing data to the book activity
                intent.putExtra("productName", productList.get(position).getProductName());
                intent.putExtra("productPrice", productList.get(position).getProductPrice());
                intent.putExtra("myImage", productList.get(position).getProductImage());
                // start the activity
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setmProducts(List<Product> productList1) {
        this.productList = productList1;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mRecipe;
        public TextView mPrice;
        public ImageView thumbnail;
        public RelativeLayout relative_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            mRecipe = (TextView) itemView.findViewById(R.id.recipe_id) ;
            mPrice = (TextView) itemView.findViewById(R.id.price_id) ;
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_id);
            relative_layout = itemView.findViewById(R.id.relative_layout_id);

        }
    }
}
