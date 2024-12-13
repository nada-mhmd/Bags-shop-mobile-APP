package com.example.nnnn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgotEmail, forgotAnswer;
    private Spinner forgotSecurityQuestion;
    private Button resetButton;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        forgotEmail = findViewById(R.id.signup_email);
        forgotSecurityQuestion = findViewById(R.id.signup_security_question);
        forgotAnswer = findViewById(R.id.signup_security_answer);
        resetButton = findViewById(R.id.reset_button);
        backToLogin = findViewById(R.id.back_to_login);

        // Handle reset button click
        resetButton.setOnClickListener(v -> {
            String email = forgotEmail.getText().toString().trim();
            String securityQuestion = forgotSecurityQuestion.getSelectedItem().toString();
            String securityAnswer = forgotAnswer.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(securityAnswer) || securityQuestion.equals("Select a security question")) {
                Toast.makeText(ForgotPasswordActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseHelper databaseHelper = new DatabaseHelper(ForgotPasswordActivity.this);
                boolean isValid = databaseHelper.validateUser(email, securityQuestion, securityAnswer);

                if (isValid) {
                    showPasswordDialog(databaseHelper, email);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle back to login click
        backToLogin.setOnClickListener(v -> finish());
    }

    private void showPasswordDialog(DatabaseHelper databaseHelper, String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        // Input field for the new password
        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setHint("Enter new password");
        newPasswordInput.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(newPasswordInput);

        // Set buttons
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            if (!TextUtils.isEmpty(newPassword)) {
                boolean isUpdated = databaseHelper.updatePassword(email, newPassword);
                if (isUpdated) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }
}
