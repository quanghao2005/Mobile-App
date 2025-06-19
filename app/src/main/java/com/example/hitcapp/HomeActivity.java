package com.example.hitcapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xử lý nút "Xem tất cả sản phẩm"
        Button btnViewAll = findViewById(R.id.buttonViewAll);
        btnViewAll.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng 'Xem tất cả sản phẩm' đang phát triển", Toast.LENGTH_SHORT).show();
            // TODO: Mở trang danh sách sản phẩm nếu có (dùng Intent)
        });

        // Xử lý tìm kiếm
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(HomeActivity.this, "Đang tìm: " + query, Toast.LENGTH_SHORT).show();
                // TODO: Thêm logic tìm kiếm sản phẩm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Có thể lọc sản phẩm theo từ khóa nhập dần
                return false;
            }
        });
    }
}
