package com.mubita.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public ApiInterface apiInterface;
    private EditText mobileNumberEditText, passwordEditText;
    private Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileNumberEditText = findViewById(R.id.editText1);
        passwordEditText = findViewById(R.id.editText2);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumber = mobileNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // **
                authenticateUser(mobileNumber, password);
                // **
                //bypassLogin();
            }
        });

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void attemptLogin(String mobileNumber, String password) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void bypassLogin() {
        User user = new User();
        user.setId(1);
        user.setFullName("John Doe");
        user.setNrcNumber("123456/78/9");
        user.setDateOfBirth("01/09/1993");
        user.setMobileNumber("0977123456");
        user.setEmailAddress("johnDoe@gmail.com");

        // save user to dataStore
        AppDataStore.getInstance().setUser(user);

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void authenticateUser(String mobileNumber, String password) {

        apiInterface = ApiClient.getInstance().create(ApiInterface.class);

        Call<User> call = apiInterface.authenticateUser(mobileNumber, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // **
                if(response.body() != null) {
                    User user1 = response.body();
                    // store user otp object
                    AppDataStore.getInstance().setUser(user1);
                    // **
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                } else {
                    // **
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Oops!");
                    alertDialog.setMessage("User not found");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
