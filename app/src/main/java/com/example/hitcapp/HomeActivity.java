package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button buttonDetail1, buttonBuy1, buttonDetail2, buttonBuy2, buttonDetail3, buttonBuy3;
    private ImageButton buttonCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home); // Đảm bảo file XML bạn sửa là activity_home.xml

        // Tìm và gán các nút
        buttonDetail1 = findViewById(R.id.buttonDetail1);
        buttonBuy1 = findViewById(R.id.buttonBuy1);
        buttonDetail2 = findViewById(R.id.buttonDetail2);
        buttonBuy2 = findViewById(R.id.buttonBuy2);
        buttonDetail3 = findViewById(R.id.buttonDetail3);
        buttonBuy3 = findViewById(R.id.buttonBuy3);
        buttonCart = findViewById(R.id.buttonCart);

        // Sự kiện tìm kiếm
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(HomeActivity.this, "Đang tìm: " + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Mở giỏ hàng
        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Sự kiện chi tiết sản phẩm
        buttonDetail1.setOnClickListener(v -> openDetail("Bàn gỗ", "Làm từ gỗ tự nhiên, phong cách cổ điển.", R.drawable.images));
        buttonDetail2.setOnClickListener(v -> openDetail("Bàn chữ K", "Thiết kế chân hình chữ K hiện đại.", R.drawable.images__1_));
        buttonDetail3.setOnClickListener(v -> openDetail("Bàn bầu dục", "Bàn họp hình bầu dục sang trọng.", R.drawable.bg08m_2));

        // Sự kiện mua ngay
        buttonBuy1.setOnClickListener(v -> addToCart("Bàn gỗ"));
        buttonBuy2.setOnClickListener(v -> addToCart("Bàn chữ K"));
        buttonBuy3.setOnClickListener(v -> addToCart("Bàn bầu dục"));
    }

    // Mở chi tiết sản phẩm
    private void openDetail(String name, String description, int imageResId) {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("image", imageResId);
        startActivity(intent);
    }

    // Thêm sản phẩm vào giỏ hàng
    private void addToCart(String productName) {
        Toast.makeText(this, productName + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("product", productName);
        startActivity(intent);
    }
}
