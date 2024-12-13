package com.example.nnnn;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText signupEmail, signupPassword, signupConfirm, signupBirthdate, signupSecurityAnswer;
    private Spinner signupSecurityQuestion;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupConfirm = findViewById(R.id.signup_confirm);
        signupBirthdate = findViewById(R.id.signup_birthdate);
        signupSecurityQuestion = findViewById(R.id.signup_security_question);
        signupSecurityAnswer = findViewById(R.id.signup_security_answer);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        // Set click listener for birthdate EditText
        signupBirthdate.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format and set the birthdate in EditText as "Day/Month/Year"
                        String birthdate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        signupBirthdate.setText(birthdate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Set sign up button click listener
        signupButton.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString();
            String confirmPassword = signupConfirm.getText().toString();
            String birthdate = signupBirthdate.getText().toString();
            String securityQuestion = signupSecurityQuestion.getSelectedItem().toString();
            String securityAnswer = signupSecurityAnswer.getText().toString();

            if (email.equals("") || password.equals("") || confirmPassword.equals("") || birthdate.equals("") || securityAnswer.equals("")) {
                Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else if (securityQuestion.equals("Select a security question")) {
                Toast.makeText(SignupActivity.this, "Please select a valid security question", Toast.LENGTH_SHORT).show();
            } else {
                // Check if email is valid
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(SignupActivity.this);
                    Boolean checkUserEmail = databaseHelper.checkEmail(email);
                    if (!checkUserEmail) {
                        Boolean insert = databaseHelper.insertData(email, password, securityQuestion, securityAnswer);
                        if (insert) {
                            Toast.makeText(SignupActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e("Signup", "Signup failed for email: " + email);
                            Toast.makeText(SignupActivity.this, "Signup Failed! Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SignupActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // Redirect to login page
        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
