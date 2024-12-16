package com.example.nnnn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ReportActivity extends AppCompatActivity {

    private TextView emailTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report); // تأكد من وجود layout باسم activity_report

        emailTextView = findViewById(R.id.emailTextView); // تأكد من وجود TextView في الـ layout
        databaseHelper = new DatabaseHelper(this);
        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayEmailsWithAction();
    }

    private void displayEmailsWithAction() {
        Cursor cursor = databaseHelper.getAllEmails();

        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder emailsBuilder = new StringBuilder();
            while (cursor.moveToNext()) {
                String email = cursor.getString(0); // جلب الإيميل من العمود الأول
                emailsBuilder.append(email).append("\n");
                emailsBuilder.append("create account").append("\n\n");
            }
            cursor.close();

            // تطبيق التنسيق باستخدام SpannableString
            applyFormattedText(emailsBuilder.toString());
        } else {
            emailTextView.setText("No emails found.");
            Toast.makeText(this, "No emails in database.", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyFormattedText(String text) {
        SpannableString spannableString = new SpannableString(text);

        int startIndex = 0;
        while (startIndex < text.length()) {
            int index = text.indexOf("create account", startIndex); // ابحث عن "create account" من الموضع الحالي
            if (index == -1) break; // لو مفيش مزيد، اخرج من الحلقة

            int endIndex = index + "create account".length();
            spannableString.setSpan(
                    new ForegroundColorSpan(Color.GREEN), // لون أخضر
                    index,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            startIndex = endIndex; // ابدأ البحث من نهاية الكلمة الحالية
        }

        emailTextView.setText(spannableString);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submenu, menu); // Inflate the menu
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all saved data
            editor.apply();

            Intent intent = new Intent(ReportActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close current activity

        }

        if (id == R.id.action_admin) {
            // Handle the admin panel action
            Intent intent = new Intent(ReportActivity.this, AdminActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
