package com.example.nnnn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<FoodUser> cartList;
    private CartActivity cartActivity; // مرجع لـ CartActivity

    // تم تعديل المُنشئ لاستقبال مرجع للنشاط
    public CartAdapter(Context context, ArrayList<FoodUser> cartList, CartActivity cartActivity) {
        this.context = context;
        this.cartList = cartList;
        this.cartActivity = cartActivity; // تخزين المرجع للنشاط
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        FoodUser food = cartList.get(position);

        holder.txtName.setText(food.getName());
        holder.txtQuantity.setText(String.valueOf(food.getQuantity()));

        double price = Double.parseDouble(food.getPrice());
        double totalPrice = price * food.getQuantity();
        holder.txtPrice.setText(String.valueOf(totalPrice));

        // تحويل byte[] إلى Bitmap لعرض الصورة
        byte[] foodImage = food.getImage();
        if (foodImage != null && foodImage.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
            holder.imageView.setImageBitmap(bitmap);  // تعيين الصورة في الـ ImageView
        }

        // زر زيادة الكمية
        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = food.getQuantity();
            food.setQuantity(quantity + 1);  // زيادة الكمية
            holder.txtQuantity.setText(String.valueOf(food.getQuantity()));

            // تحديث السعر الإجمالي
            double updatedTotalPrice = price * food.getQuantity();
            holder.txtPrice.setText(String.valueOf(updatedTotalPrice));

            notifyDataSetChanged();  // تحديث كافة العناصر
            cartActivity.calculateTotalPrice();
        });

        // زر تقليل الكمية
        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = food.getQuantity();
            if (quantity > 1) {  // التأكد من أن الكمية لا تقل عن 1
                food.setQuantity(quantity - 1);  // تقليل الكمية
                holder.txtQuantity.setText(String.valueOf(food.getQuantity()));

                // تحديث السعر الإجمالي
                double updatedTotalPrice = price * food.getQuantity();
                holder.txtPrice.setText(String.valueOf(updatedTotalPrice));

                notifyDataSetChanged();  // تحديث كافة العناصر
                cartActivity.calculateTotalPrice();
            }
        });

        // زر إزالة العنصر
        holder.btnRemove.setOnClickListener(v -> {
            cartList.remove(position);  // إزالة العنصر من القائمة
            notifyDataSetChanged();  // تحديث كافة العناصر

            cartActivity.calculateTotalPrice();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity;
        ImageView imageView;
        View btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.cartTxtName);
            txtPrice = itemView.findViewById(R.id.cartTxtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imageView = itemView.findViewById(R.id.cartImageView);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}