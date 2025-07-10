package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RequestQueue mRequestQueue;
    private String url = "https://fakestoreapi.com/users";

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

        mRequestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            String email = ((EditText) findViewById(R.id.etEmail)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                login(email, password);
            }
        });

        findViewById(R.id.tvRegisterLink).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String email, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray users = new JSONArray(response);
                        boolean isValid = false;

                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);
                            String userEmail = user.getString("email");
                            String userPassword = user.getString("password");

                            if (userEmail.equals(email) && userPassword.equals(password)) {
                                isValid = true;
                                break;
                            }
                        }

                        if (isValid) {
                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: " + error.toString());
                    Toast.makeText(this, "Lỗi mạng! Không thể đăng nhập", Toast.LENGTH_SHORT).show();
                });

        mRequestQueue.add(stringRequest);
    }
}
