package com.example.nnnn;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    ArrayList<FoodUser> cartList;
    CartAdapter cartAdapter;
    TextView totalPrice;
    Button btnCheckout, okbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);


        // استرداد العناصر من العربة
        cartList = CartManager.getInstance(this).getCartItems();

        if (cartList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
        }

        // إعداد RecyclerView
        cartAdapter = new CartAdapter(this, cartList, this);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // تحديث السعر الإجمالي
        calculateTotalPrice();

        // زر Checkout
        btnCheckout.setOnClickListener(v -> {
            if (!cartList.isEmpty()) {
                showOrderPlacedDialog(); // عرض الرسالة
            } else {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void calculateTotalPrice() {
        double total = 0;
        for (FoodUser item : cartList) {
            try {
                double price = Double.parseDouble(item.getPrice());
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                Log.e("Price Parsing Error", "Invalid price: " + item.getPrice());
            }
        }
        totalPrice.setText(String.format("Total: $%.2f", total));
    }

    private void showOrderPlacedDialog() {

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Initialize the button in the custom dialog layout
        Button okbtn = dialogView.findViewById(R.id.okButton);

        // Create the dialog and show it
        AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false) // Disallow dialog closing when touching outside
                .create(); // Creating the AlertDialog
        dialog.show(); // Show the dialog

        // Set the action for the "OK" button
        okbtn.setOnClickListener(v -> {
            clearCart(); // Clear the cart
            dialog.dismiss(); // Dismisses the custom dialog
        });
    }





    private void clearCart() {
        CartManager.getInstance(this).getCartItems().clear(); // مسح قائمة العربة
        cartAdapter.notifyDataSetChanged(); // تحديث القائمة
        calculateTotalPrice(); // تحديث السعر الإجمالي
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
            // Handle the logout action
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        if (id == R.id.action_Cart) {
            Intent intent = new Intent(CartActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_search) {
            // Handle the search action
            Intent intent = new Intent(CartActivity.this, SearchActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        if (id == R.id.action_menu) {
            // Handle the menu action
            Intent intent = new Intent(CartActivity.this, User.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}