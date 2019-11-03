package com.mubita.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mubita.foodorderapp.api.ApiClient;
import com.mubita.foodorderapp.api.ApiInterface;
import com.mubita.foodorderapp.models.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public ApiInterface apiInterface;
    // **
    private EditText fullNameEditText, nrcEditText, mobileNumberEditText, passwordEditText , confirmPasswordEditText;
    private Button registerBtn;
    private TextView dobTextView;
    // **
    private Timestamp timestamp = null;
    private LocalDate dateOfYield = null;
    private LocalDate currentDate = null;
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd MMM, yyyy");

    // **
    String fullName, nrc, dob, mobileNumber, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        nrcEditText = findViewById(R.id.nrcEditText);
        dobTextView = findViewById(R.id.dobTextView);
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        dobTextView = findViewById(R.id.dobTextView);
        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeliveryDateDialog();
            }
        });
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fullName = fullNameEditText.getText().toString();
                nrc = nrcEditText.getText().toString();
                dob = dobTextView.getText().toString();
                mobileNumber = mobileNumberEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                User user = new User();
                if (password.equals(confirmPassword)){
                    user.setFullName(fullName);
                    user.setDateOfBirth(dob);
                    user.setNrcNumber(nrc);
                    user.setMobileNumber(mobileNumber);
                    user.setPassword(password);

                    // **
                    registerUser(user);

                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(User user) {

        apiInterface = ApiClient.getInstance().create(ApiInterface.class);

        Call<User> call = apiInterface.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // **
                    if(response.body() != null) {
                        User user1 = response.body();
                        // store user otp object
                        AppDataStore.getInstance().setUser(user1);
                        // **
                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                    } else {
                        // **
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
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
                Toast.makeText(RegisterActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // remember to implement DatePickerDialog.OnDateSetListener to class
    private void openDeliveryDateDialog() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusDays(1);
        datePickerDialog.getDatePicker().setMaxDate(dateTime.getMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateOfYield = new LocalDate( year, month+1, dayOfMonth );
        dobTextView.setText(dateFormatter.format(dateOfYield.toDate()));
    }
}
