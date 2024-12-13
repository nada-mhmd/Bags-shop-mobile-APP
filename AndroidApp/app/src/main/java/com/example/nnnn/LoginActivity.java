package com.example.nnnn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.nnnn.databinding.ActivityLoginBinding;

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

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String savedEmail = sharedPreferences.getString("email", "");
            Toast.makeText(this, "Welcome back " + savedEmail, Toast.LENGTH_SHORT).show();
            navigateToNextActivity(sharedPreferences.getString("userType", ""));
            finish(); // Close LoginActivity
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
                        saveLoginState(sharedPreferences, email, "admin");
                        Toast.makeText(LoginActivity.this, "Login Successfully as admin!", Toast.LENGTH_SHORT).show();
                        navigateToNextActivity("admin");
                    } else {
                        Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                        if (checkCredentials) {
                            saveLoginState(sharedPreferences, email, "user");
                            Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            navigateToNextActivity("user");
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        // Redirect to signup activity
        binding.signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Handle "Forgot Password" action
        binding.forgotPasswordText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void saveLoginState(SharedPreferences sharedPreferences, String email, String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putString("userType", userType);
        editor.apply();
    }

    private void navigateToNextActivity(String userType) {
        Intent intent;
        if ("admin".equals(userType)) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, User.class);
        }
        startActivity(intent);
        finish();
    }
}
