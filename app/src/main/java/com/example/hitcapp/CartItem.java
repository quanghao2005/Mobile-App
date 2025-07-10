package com.example.hitcapp.model;

public class CartItem {
    private String name;
    private int price;
    private int quantity;
    private int imageResId;
    private String size;

    public CartItem(String name, int price, int quantity, int imageResId, String size) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
        this.size = size;
    }

    // Getters
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getImageResId() { return imageResId; }
    public String getSize() { return size; }

    // Setters
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
