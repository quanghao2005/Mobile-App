package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            String email = ((android.widget.EditText) findViewById(R.id.etEmail)).getText().toString().trim();
            String password = ((android.widget.EditText) findViewById(R.id.etPassword)).getText().toString();

            if(email.equals("hao@gmail.com") && password.equals("1")) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                android.widget.Toast.makeText(this, "Email hoặc mật khẩu không đúng!", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvRegisterLink).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

}
