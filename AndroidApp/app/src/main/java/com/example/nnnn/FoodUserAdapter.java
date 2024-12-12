package com.example.nnnn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodUserAdapter extends RecyclerView.Adapter<FoodUserAdapter.FoodUserViewHolder> {

    private Context context;
    private ArrayList<FoodUser> foodList;

    public FoodUserAdapter(Context context, ArrayList<FoodUser> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_item_user, parent, false);
        return new FoodUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodUserViewHolder holder, int position) {
        FoodUser food = foodList.get(position);

        holder.txtName.setText(food.getName());
        holder.txtPrice.setText( "$" + food.getPrice());

        // تحويل byte[] إلى Bitmap
        byte[] foodImage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.addToCart.setOnClickListener(v -> {
            // إضافة العنصر إلى العربة
            CartManager.getInstance(context).addToCart(food);
            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodUserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imageView;
        TextView addToCart;

        public FoodUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imageView = itemView.findViewById(R.id.imageView);
            addToCart = itemView.findViewById(R.id.addToCartPopular);
        }
    }
}