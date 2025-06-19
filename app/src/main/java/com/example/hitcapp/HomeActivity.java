package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout product1, product2, product3;
    private TextView textViewProduct1, textViewProduct2, textViewProduct3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Thiết lập tìm kiếm
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

        // Ánh xạ các thành phần giao diện
        product1 = findViewById(R.id.product1);
        product2 = findViewById(R.id.product2);
        product3 = findViewById(R.id.product3);

        textViewProduct1 = findViewById(R.id.textViewProduct1);
        textViewProduct2 = findViewById(R.id.textViewProduct2);
        textViewProduct3 = findViewById(R.id.textViewProduct3);

        // Gán tên sản phẩm
        textViewProduct1.setText("Bàn gỗ");
        textViewProduct2.setText("Bàn chữ K");
        textViewProduct3.setText("Bàn bầu dục");

        // Xử lý click vào sản phẩm
        product1.setOnClickListener(v -> openDetail(
                "Bàn gỗ",
                "Là một món đồ nội thất phổ biến, được làm từ các loại gỗ khác nhau, có nhiều kiểu dáng, kích thước và mục đích sử dụng.",
                R.drawable.images
        ));

        product2.setOnClickListener(v -> openDetail(
                "Bàn chữ K",
                "Là loại bàn có thiết kế chân bàn hình chữ K, thường được sử dụng làm bàn làm việc, bàn gaming hoặc bàn học.",
                R.drawable.images__1_
        ));

        product3.setOnClickListener(v -> openDetail(
                "Bàn bầu dục",
                "Bàn họp hình bầu dục với phong cách hiện đại, sang trọng, thiết kế đường cong mềm mại tạo sự uyển chuyển và thẩm mỹ.",
                R.drawable.bg08m_2
        ));
    }

    private void openDetail(String name, String description, int imageResId) {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("image", imageResId);
        startActivity(intent);
    }
}
