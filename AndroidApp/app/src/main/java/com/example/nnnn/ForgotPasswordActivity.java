package com.example.nnnn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText forgotEmail;
    Button resetButton;
    TextView backToLogin;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // ربط العناصر
        forgotEmail = findViewById(R.id.forgot_email);
        resetButton = findViewById(R.id.reset_button);
        backToLogin = findViewById(R.id.back_to_login);
        databaseHelper = new DatabaseHelper(this);

        // عند الضغط على زر الإرسال
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotEmail.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // التحقق من الإيميل
                    Cursor cursor = databaseHelper.getPasswordByEmail(email);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                        Toast.makeText(ForgotPasswordActivity.this, "Your password is: " + password, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // العودة للشاشة السابقة
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
