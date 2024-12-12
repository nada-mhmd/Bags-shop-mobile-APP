package com.example.nnnn;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    TextView reportTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportTextView = findViewById(R.id.reportTextView);

        // Get the report data from the intent
        String report = getIntent().getStringExtra("report");
        reportTextView.setText(report); // Set the report content
    }
}

