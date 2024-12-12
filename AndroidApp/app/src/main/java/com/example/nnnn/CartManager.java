package com.example.nnnn;

import android.content.Context;

import java.util.ArrayList;
public class CartManager {
    private static CartManager instance;
    private ArrayList<FoodUser> cartItems;
    private Context context;  // إضافة مرجع للسياق

    private CartManager(Context context) {
        cartItems = new ArrayList<>();
        this.context = context;  // حفظ السياق
    }

    public static CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context);
        }
        return instance;
    }
    public void addToCart(FoodUser newItem) {
        boolean itemExists = false;

        for (FoodUser item : cartItems) {
            if (item.getName().equals(newItem.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            cartItems.add(newItem);
        }

        if (context instanceof CartActivity) {
            ((CartActivity) context).calculateTotalPrice();
        }
    }



    public ArrayList<FoodUser> getCartItems() {
        return cartItems;
    }
}

