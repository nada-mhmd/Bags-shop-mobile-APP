package com.example.nnnn;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper sqLiteHelper;
    ListView listView;
    ArrayList<Food> foodList;
    FoodAdapter adapter;
    ImageView imageViewFood; // Declare ImageView at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(this, R.layout.food_item, foodList);
        listView.setAdapter(adapter);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);

        // Request permissions if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        loadData();
    }

    public static byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void loadData() {
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM FOOD");
        foodList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            String category = cursor.getString(3); // Retrieve the category
            int quantity = cursor.getInt(4); // Retrieve the quantity
            byte[] image = cursor.getBlob(5);

            foodList.add(new Food(id, name, price, category, quantity, image)); // Pass correct parameters
        }

        adapter.notifyDataSetChanged();
        if (cursor != null) {
            cursor.close();
        }
    }

    public void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_activity);
        dialog.setTitle("Update");

        imageViewFood = dialog.findViewById(R.id.imageViewFood); // Initialize ImageView
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        final EditText edtCategory = dialog.findViewById(R.id.edtCategory);
        final EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        // Load existing data into the dialog
        final Food food = foodList.get(position);
        edtName.setText(food.getName());
        edtPrice.setText(food.getPrice());
        edtCategory.setText(food.getCategory());
        edtQuantity.setText(String.valueOf(food.getQuantity()));
        if (food.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(food.getImage(), 0, food.getImage().length);
            imageViewFood.setImageBitmap(bitmap);
        }

        // Handle image selection and update
        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Update the food item in the database
                    sqLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            edtCategory.getText().toString().trim(),
                            Integer.parseInt(edtQuantity.getText().toString().trim()),
                            imageViewToByte(imageViewFood),
                            food.getId()
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    error.printStackTrace();
                }
                loadData(); // Refresh the list with updated data
            }
        });

        // Set dialog size
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 200 && data != null) {
            try {
                // Get the selected image from data
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Set the image to the ImageView in the dialog
                imageViewFood.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            // Handle the logout action
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }

        if (id == R.id.action_admin) {
            // Handle the admin panel action
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
