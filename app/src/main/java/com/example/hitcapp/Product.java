package com.example.hitcapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    private final String name;
    private final String size;
    private final int price;
    private final String imageUrl; // Ảnh từ URL (dạng chuỗi)
    private int quantity;

    // Constructor đầy đủ (dùng cho giỏ hàng, thêm sản phẩm)
    public Product(String name, String size, int price, String imageUrl, int quantity) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // Constructor đơn giản (dùng khi load từ API, size mặc định)
    public Product(String name, String description, int price, String imageUrl) {
        this.name = name;
        this.size = "M"; // size mặc định nếu không có
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = 1; // số lượng mặc định là 1
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setter
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Tính tổng tiền
    public int getTotalPrice() {
        return price * quantity;
    }

    // Convert sang JSONObject để lưu vào SharedPreferences
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("size", size);
            obj.put("price", price);
            obj.put("imageUrl", imageUrl);
            obj.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    // Tạo Product từ JSONObject (dùng khi load lại từ SharedPreferences)
    public static Product fromJson(JSONObject obj) {
        try {
            return new Product(
                    obj.getString("name"),
                    obj.getString("size"),
                    obj.getInt("price"),
                    obj.getString("imageUrl"),
                    obj.getInt("quantity")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // So sánh sản phẩm (cùng name + size thì được xem là giống nhau)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product p = (Product) o;
        return name.equals(p.name) && size.equals(p.size);
    }

    @Override
    public int hashCode() {
        return (name + size).hashCode();
    }
}
