package com.mubita.foodorderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mubita.foodorderapp.NotificationViewActivity;
import com.mubita.foodorderapp.R;
import com.mubita.foodorderapp.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_notification_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, final int i) {

        final int id = mNotifications.get(i).getId();
        final String notificationHeader = mNotifications.get(i).getSubject();
        final String notificationInfo = mNotifications.get(i).getMessage();

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.notificationHeader.setText(notificationHeader);
        viewHolder.notificationInfo.setText(notificationInfo);

        viewHolder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, NotificationViewActivity.class);
                intent.putExtra("notificationSubject", mNotifications.get(i).getSubject());
                intent.putExtra("notificationMessage", mNotifications.get(i).getMessage());
                intent.putExtra("createdAt", mNotifications.get(i).getCreatedAt());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    /**/
    public void setmNotifications(List<Notification> mNotifications) {
        this.mNotifications = mNotifications;
        notifyDataSetChanged();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView notificationID;
        public TextView notificationHeader;
        public TextView notificationInfo;
        public TextView createdAt;
        public CardView listItem;
        public ViewHolder(View itemView) {
            super(itemView);
            notificationHeader = itemView.findViewById(R.id.notificationHeader);
            notificationInfo = itemView.findViewById(R.id.notificationInfo);
            createdAt = itemView.findViewById(R.id.textView6);
            listItem = itemView.findViewById(R.id.notification_list_item_id);
        }
    }
}

