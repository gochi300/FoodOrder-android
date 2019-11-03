package com.mubita.foodorderapp.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mubita.foodorderapp.AppDataStore;
import com.mubita.foodorderapp.R;
import com.mubita.foodorderapp.models.Order;
import com.mubita.foodorderapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private Context mContext;
    //private ArrayList<Product> mProduct;
    private List<Product> mProduct;
    private List<Product> orderList = new ArrayList<>();

    public CartListAdapter(Context mContext, List<Product> mProduct) {
        this.mContext = mContext;
        this.mProduct = mProduct;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_cart_list_item,viewGroup,false);
        return new CartListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartListAdapter.ViewHolder viewHolder, final int position) {

        //viewHolder.mName.setText(""+mProduct.get(position).getId());
        viewHolder.sName.setText(mProduct.get(position).getProductName());
        viewHolder.qty.setText("("+ mProduct.get(position).getProductQty()+")");
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Removed from cart!", Toast.LENGTH_SHORT).show();
                List<Product> productList = new ArrayList();
                productList = AppDataStore.getInstance().getProductList();
                productList.remove(mProduct.get(position));
                AppDataStore.getInstance().setProductList(productList);
            }
        });
        viewHolder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mProduct.size();
    }


/*    public void setmPayments(List<Payment> mProduct) {
        this.mProduct = mProduct;
        notifyDataSetChanged();
    }*/




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView sName;
        public TextView qty;
        public ImageView remove;
        public RelativeLayout listItem;
        public ViewHolder(View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.mProduct);
            qty = itemView.findViewById(R.id.mQty);
            remove = itemView.findViewById(R.id.mRemove);
            listItem = itemView.findViewById(R.id.relative_layout);
        }
    }
}

