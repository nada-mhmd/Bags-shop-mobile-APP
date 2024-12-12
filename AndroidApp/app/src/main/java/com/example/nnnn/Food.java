package com.example.nnnn;
public class Food {
    private int id;
    private String name;
    private String price;
    private String category;  // إضافة فئة الطعام
    private int quantity;     // إضافة كمية الطعام
    private byte[] image;

    // مُنشئ الكلاس
    public Food(int id, String name, String price, String category, int quantity, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.image = image;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
