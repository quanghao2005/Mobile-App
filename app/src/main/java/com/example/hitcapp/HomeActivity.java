package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
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
    ArrayList<Product> filteredList = new ArrayList<>();
    ImageButton buttonCart;
    SearchView searchView;

    String apiUrl = "https://fakestoreapi.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        buttonCart = findViewById(R.id.buttonCart);
        searchView = findViewById(R.id.searchView);

        // Thiết lập adapter ban đầu với filteredList
        productAdapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sự kiện click nút giỏ hàng
        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Xử lý tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Không xử lý khi người dùng nhấn tìm
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });

        loadProducts();
    }

    private void loadProducts() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        productList.clear();
                        filteredList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String name = obj.getString("title");
                            String description = obj.getString("description");
                            double priceDouble = obj.getDouble("price");
                            int price = (int) priceDouble;
                            String imageUrl = obj.getString("image");

                            Product product = new Product(name, description, price, imageUrl);
                            productList.add(product);
                        }
                        filteredList.addAll(productList);
                        productAdapter.setProducts(filteredList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Lỗi gọi API", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void filterProducts(String keyword) {
        filteredList.clear();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.setProducts(filteredList);
    }
}
