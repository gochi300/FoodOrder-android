package com.mubita.foodorderapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mubita.foodorderapp.R;
import com.mubita.foodorderapp.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Context mContext;
    //private ArrayList<Product> mProduct;
    private List<Order> mProduct;
    private List<Order> orderList = new ArrayList<>();

    public OrderListAdapter(Context mContext, List<Order> mOrder) {
        this.mContext = mContext;
        this.mProduct = mOrder;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_order_list_item,viewGroup,false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderListAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.sName.setText(String.valueOf(mProduct.get(i).getOrderNumber()));
        viewHolder.price.setText(mProduct.get(i).getOrderStatus());
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


    public void setmOrders(List<Order> mOrder) {
        this.mProduct = mOrder;
        notifyDataSetChanged();
    }




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView sName;
        public TextView price;
        public TextView qty;
        public ImageView add;
        public RelativeLayout listItem;
        public ViewHolder(View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.mProduct);
            price = itemView.findViewById(R.id.mPrice);
            qty = itemView.findViewById(R.id.mQty);
            listItem = itemView.findViewById(R.id.relative_layout);
        }
    }
}

