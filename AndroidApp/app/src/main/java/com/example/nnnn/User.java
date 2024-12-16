package com.example.nnnn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class User extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<FoodUser> foodList;
    FoodUserAdapter adapter;
    SQLiteHelper sqLiteHelper;
    Spinner categorySpinner;
    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        categorySpinner = findViewById(R.id.categorySpinner);
        foodList = new ArrayList<>();
        categories = new ArrayList<>();

        // Initialize RecyclerView Adapter
        adapter = new FoodUserAdapter(this, foodList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);

        // Add "All" category to the list of categories
        categories.add("All");

        // Get distinct categories from the database
        Cursor cursorCategories = sqLiteHelper.getData("SELECT DISTINCT category FROM FOOD");
        while (cursorCategories.moveToNext()) {
            String category = cursorCategories.getString(0);
            categories.add(category); // Add the category to the list
        }
        cursorCategories.close();

        // Set up the Spinner adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        // Load all food items initially
        loadFoodItems(null);

        // Set Spinner item selected listener to filter food based on category
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = categories.get(position);

                // تغيير البيانات بناءً على الاختيار
                if (selectedCategory.equals("All")) {
                    loadFoodItems(null); // Load all items if "All" is selected
                } else {
                    loadFoodItems(selectedCategory); // Load items for the selected category
                }

                // تغيير لون النص للعنصر المختار
                if (selectedItemView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedItemView;

                    if (position == 0) {
                        // إذا اختار القيمة الافتراضية
                        selectedTextView.setTextColor(Color.GRAY);
                    } else {
                        // إذا اختار أي قيمة أخرى
                        selectedTextView.setTextColor(Color.BLACK);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle if nothing is selected (optional)
            }
        });
    }

    // Method to load food items based on category
    private void loadFoodItems(String category) {
        foodList.clear();
        String query = "SELECT * FROM FOOD";
        if (category != null && !category.isEmpty()) {
            query += " WHERE category = ?";
        }

        // Use the appropriate query to fetch data from the database
        Cursor cursor = sqLiteHelper.getData(query, category);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(5); // تأكد أن العمود الصحيح للصورة
            foodList.add(new FoodUser(name, price, image)); // Add the food item to the list
        }
        cursor.close();

        // Notify the adapter that the data set has changed
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

            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all saved data
            editor.apply();

                Intent intent = new Intent(User.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close current activity

        }
        if (id == R.id.action_Cart) {
            Intent intent = new Intent(User.this, CartActivity.class);

            startActivity(intent);
            return true;
        }

        if (id == R.id.action_search) {
            // Handle the logout action
            Intent intent = new Intent(User.this, SearchActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        if (id == R.id.action_menu) {
            // Handle the logout action
            Intent intent = new Intent(User.this, User.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
    }

}