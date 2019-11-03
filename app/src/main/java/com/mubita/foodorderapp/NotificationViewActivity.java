package com.mubita.foodorderapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationViewActivity extends AppCompatActivity {

    public TextView notificationMessageTextView;
    public TextView createdAtTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        Intent intent = getIntent();
        String notificationSubject = intent.getStringExtra("notificationSubject");
        String notificationMessage = intent.getStringExtra("notificationMessage");
        String createdAt = intent.getStringExtra("createdAt");

        //Initialize actionbar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(notificationSubject);

        notificationMessageTextView = findViewById(R.id.notificationInfo);
        createdAtTextView = findViewById(R.id.textView6);

        notificationMessageTextView.setText(notificationMessage);
        createdAtTextView.setText(createdAt);
    }
}
