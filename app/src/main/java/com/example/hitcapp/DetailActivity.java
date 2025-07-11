package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewDetail;
    private TextView textViewTitle, textViewDescription, textViewQuantity, textViewPrice;
    private Button buttonIncrease, buttonDecrease, buttonAddToCart;
    private ImageButton buttonBack, buttonGoToCart;
    private int quantity = 1;
    private int price = 0;
    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        imageViewDetail = findViewById(R.id.imageViewDetail);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        textViewPrice = findViewById(R.id.textViewPrice);
        buttonIncrease = findViewById(R.id.buttonIncrease);
        buttonDecrease = findViewById(R.id.buttonDecrease);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        buttonBack = findViewById(R.id.buttonBack);
        buttonGoToCart = findViewById(R.id.buttonGoToCart);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        price = intent.getIntExtra("price", 0);

        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageViewDetail);
        }

        textViewTitle.setText(title != null ? title : "Không có tên");
        textViewDescription.setText(description != null ? description : "Không có mô tả");
        textViewPrice.setText("Giá: " + price + "đ");

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
            startActivity(new Intent(DetailActivity.this, CartActivity.class));
        });

        buttonAddToCart.setOnClickListener(v -> {
            String name = textViewTitle.getText().toString();
            Product product = new Product(name, "", price, imageUrl, quantity); // size để rỗng
            saveToCart(product);
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveToCart(Product newItem) {
        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        String json = prefs.getString("cart_items", "[]");

        try {
            JSONArray array = new JSONArray(json);
            boolean found = false;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("name").equals(newItem.getName())) {
                    int oldQty = obj.getInt("quantity");
                    obj.put("quantity", oldQty + newItem.getQuantity());
                    found = true;
                    break;
                }
            }

            if (!found) {
                JSONObject obj = newItem.toJson();
                array.put(obj);
            }

            prefs.edit().putString("cart_items", array.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
