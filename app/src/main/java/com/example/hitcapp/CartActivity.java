package com.example.hitcapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    private ListView listViewCart;
    private Button buttonCheckout;
    private ImageButton buttonBack;
    private TextView textViewTotal;

    private ArrayList<String> cartItems;
    private ArrayAdapter<String> cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        listViewCart = findViewById(R.id.listViewCart);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonBack = findViewById(R.id.buttonBack);
        textViewTotal = findViewById(R.id.textViewTotal);

        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("items", new HashSet<>());

        cartItems = new ArrayList<>(set);
        cartAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        listViewCart.setAdapter(cartAdapter);

        // Tính tổng tiền từ danh sách sản phẩm có định dạng "Tên - Giá"
        int total = 0;
        for (String item : cartItems) {
            String[] parts = item.split(" - ");
            if (parts.length == 2) {
                try {
                    int price = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
                    total += price;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        textViewTotal.setText("Tổng: " + total + "đ");

        buttonBack.setOnClickListener(v -> finish());

        buttonCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng thanh toán đang phát triển!", Toast.LENGTH_SHORT).show();
        });
    }
}
