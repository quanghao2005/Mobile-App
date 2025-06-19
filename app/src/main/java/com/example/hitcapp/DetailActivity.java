package com.example.hitcapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewDetail;
    private TextView textViewTitle, textViewDescription;
    private ImageButton buttonBack;
    private Button buttonAddToCart;

    private Spinner spinnerSize;
    private TextView textViewQuantity;
    private Button buttonIncrease, buttonDecrease;

    private int quantity = 1; // Số lượng mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ẩn ActionBar nếu có
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Ánh xạ View
        imageViewDetail = findViewById(R.id.imageViewDetail);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

        spinnerSize = findViewById(R.id.spinnerSize);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        buttonIncrease = findViewById(R.id.buttonIncrease);
        buttonDecrease = findViewById(R.id.buttonDecrease);

        // Nhận dữ liệu từ Intent
        int imageRes = getIntent().getIntExtra("image", R.drawable.ic_launcher_foreground);
        String title = getIntent().getStringExtra("name");
        String desc = getIntent().getStringExtra("description");

        // Gán dữ liệu
        imageViewDetail.setImageResource(imageRes);
        textViewTitle.setText(title);
        textViewDescription.setText(desc);

        // Gán size trực tiếp không dùng arrays.xml
        String[] sizeList = {"Nhỏ", "Vừa", "Lớn"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);

        // Số lượng ban đầu
        textViewQuantity.setText(String.valueOf(quantity));

        // Xử lý nút tăng
        buttonIncrease.setOnClickListener(v -> {
            quantity++;
            textViewQuantity.setText(String.valueOf(quantity));
        });

        // Xử lý nút giảm
        buttonDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                textViewQuantity.setText(String.valueOf(quantity));
            }
        });

        // Nút quay lại
        buttonBack.setOnClickListener(v -> finish());

        // Nút thêm vào giỏ hàng
        buttonAddToCart.setOnClickListener(v -> {
            String selectedSize = spinnerSize.getSelectedItem().toString();
            String message = "Đã thêm " + quantity + " sản phẩm (Size: " + selectedSize + ") vào giỏ hàng!";
            Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}
