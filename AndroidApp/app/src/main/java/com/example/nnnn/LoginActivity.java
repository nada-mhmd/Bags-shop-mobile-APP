package com.example.nnnn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.nnnn.databinding.ActivityLoginBinding;

import javax.mail.MessagingException;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;
    CheckBox rememberMeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        // Initialize rememberMeCheckbox
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        // Check if user credentials are saved
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            binding.loginEmail.setText(savedEmail);
            binding.loginPassword.setText(savedPassword);
            rememberMeCheckbox.setChecked(true);
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.equals("admin") && password.equals("admin")) {
                        // Navigate to AdminActivity
                        Toast.makeText(LoginActivity.this, "Login Successfully as admin!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                        if (checkCredentials) {
                            // If Remember Me is checked, save credentials
                            if (rememberMeCheckbox.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.apply();
                            }

                            Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, User.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        // Redirect to signup activity
        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Handle "Forgot Password" action
        binding.forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });



    }
}
