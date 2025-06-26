package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewDetail;
    private TextView textViewTitle, textViewDescription, textViewQuantity;
    private Spinner spinnerSize;
    private Button buttonIncrease, buttonDecrease, buttonAddToCart;
    private ImageButton buttonBack, buttonGoToCart;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Ánh xạ
        imageViewDetail = findViewById(R.id.imageViewDetail);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        spinnerSize = findViewById(R.id.spinnerSize);
        buttonIncrease = findViewById(R.id.buttonIncrease);
        buttonDecrease = findViewById(R.id.buttonDecrease);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        buttonBack = findViewById(R.id.buttonBack);
        buttonGoToCart = findViewById(R.id.buttonGoToCart);

        // Dữ liệu từ intent
        Intent intent = getIntent();
        int imageRes = intent.getIntExtra("image", R.drawable.ic_launcher_foreground);
        String title = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");

        imageViewDetail.setImageResource(imageRes);
        textViewTitle.setText(title != null ? title : "Không có tên");
        textViewDescription.setText(description != null ? description : "Không có mô tả");

        // Spinner Size
        String[] sizeList = {"Nhỏ", "Vừa", "Lớn"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);

        textViewQuantity.setText(String.valueOf(quantity));

        buttonIncrease.setOnClickListener(v -> {
            quantity++;
            textViewQuantity.setText(String.valueOf(quantity));
        });

        buttonDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                textViewQuantity.setText(String.valueOf(quantity));
            }
        });

        buttonBack.setOnClickListener(v -> finish());

        buttonGoToCart.setOnClickListener(v -> {
            Intent goToCart = new Intent(DetailActivity.this, CartActivity.class);
            startActivity(goToCart);
        });

        buttonAddToCart.setOnClickListener(v -> {
            String selectedSize = spinnerSize.getSelectedItem().toString();
            String name = textViewTitle.getText().toString();
            String cartItem = name + " - Size: " + selectedSize + " - SL: " + quantity;

            SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
            Set<String> set = prefs.getStringSet("items", new HashSet<>());
            set.add(cartItem);
            prefs.edit().putStringSet("items", set).apply();

            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();

            // Chuyển đến giỏ hàng luôn (nếu bạn muốn)
            Intent goToCart = new Intent(DetailActivity.this, CartActivity.class);
            startActivity(goToCart);
        });
    }
}
