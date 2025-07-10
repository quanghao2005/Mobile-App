package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Product> productList = new ArrayList<>();
    ImageButton buttonCart;  // Dùng đúng ID đã khai báo trong XML

    String apiUrl = "https://fakestoreapi.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        buttonCart = findViewById(R.id.buttonCart); // <-- đúng ID trong XML

        // Thiết lập RecyclerView
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sự kiện click nút giỏ hàng
        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Gọi API
        loadProducts();
    }

    private void loadProducts() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        productList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String name = obj.getString("title");
                            String description = obj.getString("description");
                            double priceDouble = obj.getDouble("price");
                            int price = (int) priceDouble;
                            String imageUrl = obj.getString("image");

                            productList.add(new Product(name, description, price, imageUrl));
                        }
                        productAdapter.setProducts(productList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Lỗi gọi API", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
