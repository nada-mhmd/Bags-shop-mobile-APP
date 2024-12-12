package com.example.nnnn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<FoodUser> foodList;
    FoodUserAdapter adapter;
    SQLiteHelper sqLiteHelper;
    EditText searchEditText;
    ImageButton voiceSearchButton, barcodeSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        voiceSearchButton = findViewById(R.id.voiceSearchButton);
        barcodeSearchButton = findViewById(R.id.barcodeSearchButton);

        foodList = new ArrayList<>();
        adapter = new FoodUserAdapter(this, foodList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);

        // Load all food items initially
        loadFoodItems(null);

        // Add text change listener for searchEditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Perform search as user types
                String query = s.toString().trim();
                loadFoodItems(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });

        // Voice search button click listener
        voiceSearchButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            startActivityForResult(intent, 1);
        });

        // Barcode search button click listener
        barcodeSearchButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, BarcodeScannerActivity.class);
            startActivityForResult(intent, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voiceQuery = result.get(0);
            searchEditText.setText(voiceQuery);
            loadFoodItems(voiceQuery);
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            String barcodeResult = data.getStringExtra("barcode");
            searchEditText.setText(barcodeResult);
            loadFoodItems(barcodeResult);
        }
    }

    // Method to load food items based on search query
    private void loadFoodItems(String query) {
        foodList.clear();
        String sql = "SELECT * FROM FOOD";

        // Add condition for search query
        if (query != null && !query.trim().isEmpty()) {
            sql += " WHERE name LIKE ?";
        }

        Cursor cursor = null;

        try {
            // Execute query safely
            if (query != null && !query.trim().isEmpty()) {
                cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, new String[]{"%" + query + "%"});
            } else {
                cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, null);
            }

            // Parse results
            while (cursor.moveToNext()) {
                String name = cursor.getString(1);
                String price = cursor.getString(2);
                byte[] image = cursor.getBlob(5);
                foodList.add(new FoodUser(name, price, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Handle the logout action
            Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        if (id == R.id.action_Cart) {
            Intent intent = new Intent(SearchActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_search) {
            // Handle the logout action
            Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        if (id == R.id.action_menu) {
            // Handle the logout action
            Intent intent = new Intent(SearchActivity.this, User.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
