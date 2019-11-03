package com.mubita.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView versionNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String versionName = BuildConfig.VERSION_NAME;
        versionNameTextView = findViewById(R.id.text1);
        versionNameTextView.setText("Food Order App "+ versionName);
    }
}
