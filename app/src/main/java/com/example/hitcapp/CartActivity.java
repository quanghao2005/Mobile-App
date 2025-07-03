package com.example.hitcapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class CartActivity extends AppCompatActivity {

    private ListView listViewCart;
    private Button buttonCheckout;
    private ImageButton buttonBack, buttonClearAll;
    private TextView textViewTotal;
    private ArrayList<Product> cartItems;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        listViewCart = findViewById(R.id.listViewCart);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonBack = findViewById(R.id.buttonBack);
        buttonClearAll = findViewById(R.id.buttonClearAll);
        textViewTotal = findViewById(R.id.textViewTotal);

        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        Set<String> itemSet = prefs.getStringSet("items", new HashSet<>());

        cartItems = new ArrayList<>();
        for (String item : itemSet) {
            String[] parts = item.split("\\|\\|");
            if (parts.length == 5) {
                cartItems.add(new Product(
                        parts[0], parts[1],
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[2])
                ));
            }
        }

        cartAdapter = new CartAdapter();
        listViewCart.setAdapter(cartAdapter);
        updateTotal();

        buttonBack.setOnClickListener(v -> finish());

        buttonCheckout.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng thanh toán đang phát triển!", Toast.LENGTH_SHORT).show());

        buttonClearAll.setOnClickListener(v -> {
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            updateTotal();
            prefs.edit().remove("items").apply();
            Toast.makeText(this, "Đã xóa tất cả sản phẩm", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateTotal() {
        int total = 0;
        for (Product p : cartItems) {
            total += p.getPrice() * p.getQuantity();
        }
        textViewTotal.setText("Tổng: " + total + "đ");
    }

    private static class Product {
        private final String name, size;
        private final int price, imageResId;
        private int quantity;

        public Product(String name, String size, int price, int imageResId, int quantity) {
            this.name = name;
            this.size = size;
            this.price = price;
            this.imageResId = imageResId;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public String getSize() { return size; }
        public int getPrice() { return price; }
        public int getImageResId() { return imageResId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    private class CartAdapter extends BaseAdapter {
        @Override public int getCount() { return cartItems.size(); }
        @Override public Product getItem(int i) { return cartItems.get(i); }
        @Override public long getItemId(int i) { return i; }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            if (view == null)
                view = LayoutInflater.from(CartActivity.this).inflate(R.layout.item_cart, parent, false);

            Product p = getItem(pos);
            ((ImageView) view.findViewById(R.id.imageProduct)).setImageResource(p.getImageResId());
            ((TextView) view.findViewById(R.id.textProductName)).setText(p.getName() + " (Size: " + p.getSize() + ")");
            ((TextView) view.findViewById(R.id.textProductPrice)).setText("Giá: " + p.getPrice() + "đ");
            TextView qtyText = view.findViewById(R.id.textProductQuantity);
            qtyText.setText("Số lượng: " + p.getQuantity());

            view.findViewById(R.id.buttonIncrease).setOnClickListener(v -> {
                p.setQuantity(p.getQuantity() + 1);
                notifyDataSetChanged();
                updateTotal();
            });

            view.findViewById(R.id.buttonDecrease).setOnClickListener(v -> {
                if (p.getQuantity() > 1) {
                    p.setQuantity(p.getQuantity() - 1);
                    notifyDataSetChanged();
                    updateTotal();
                }
            });

            view.findViewById(R.id.buttonRemove).setOnClickListener(v -> {
                cartItems.remove(pos);
                notifyDataSetChanged();
                updateTotal();
                removeFromSharedPreferences(p);
            });

            return view;
        }

        private void removeFromSharedPreferences(Product product) {
            SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
            Set<String> set = prefs.getStringSet("items", new HashSet<>());
            Set<String> newSet = new HashSet<>();
            String target = product.getName() + "||" + product.getSize() + "||" + product.getQuantity()
                    + "||" + product.getPrice() + "||" + product.getImageResId();
            for (String item : set) {
                if (!item.equals(target)) newSet.add(item);
            }
            prefs.edit().putStringSet("items", newSet).apply();
        }
    }
}
