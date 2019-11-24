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
import com.mubita.foodorderapp.models.Product;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {

    private Context mContext;
    //private ArrayList<Product> orderList;
    private List<Order> orderList;

    public ReceiptAdapter(Context mContext, List<Order> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ReceiptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_receipt_list_item,viewGroup,false);
        return new ReceiptAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceiptAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.sName.setText(orderList.get(i).getProductName());
        viewHolder.qty.setText("("+ orderList.get(i).getOrderQty()+")");
        viewHolder.price.setText("K"+ orderList.get(i).getTotalPrice());
        viewHolder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public void setmProducts(List<Order> orderList1) {
        this.orderList = orderList1;
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

