package com.example.hitcapp;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.*;

import java.util.*;

public class CartActivity extends AppCompatActivity {

    private ListView listViewCart;
    private Button buttonCheckout;
    private ImageButton buttonBack, buttonClearAll;
    private TextView textViewTotal;
    private ArrayList<Product> cartItems;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        listViewCart = findViewById(R.id.listViewCart);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonBack = findViewById(R.id.buttonBack);
        buttonClearAll = findViewById(R.id.buttonClearAll);
        textViewTotal = findViewById(R.id.textViewTotal);

        loadCartData();

        cartAdapter = new CartAdapter();
        listViewCart.setAdapter(cartAdapter);
        updateTotal();

        buttonBack.setOnClickListener(v -> finish());

        buttonCheckout.setOnClickListener(v -> showCheckoutInfoDialog());

        buttonClearAll.setOnClickListener(v -> {
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            updateTotal();
            getSharedPreferences("cart", MODE_PRIVATE).edit().remove("cart_items").apply();
            Toast.makeText(this, "Đã xóa tất cả sản phẩm", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCartData() {
        cartItems = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        String json = prefs.getString("cart_items", "[]");

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Product p = Product.fromJson(obj);
                if (p != null) cartItems.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveCartData() {
        JSONArray array = new JSONArray();
        for (Product p : cartItems) {
            array.put(p.toJson());
        }
        getSharedPreferences("cart", MODE_PRIVATE).edit()
                .putString("cart_items", array.toString()).apply();
    }

    private void updateTotal() {
        int total = 0;
        for (Product p : cartItems) {
            total += p.getPrice() * p.getQuantity();
        }
        textViewTotal.setText("Tổng: " + total + "đ");
    }

    private class CartAdapter extends BaseAdapter {
        @Override public int getCount() { return cartItems.size(); }
        @Override public Product getItem(int i) { return cartItems.get(i); }
        @Override public long getItemId(int i) { return i; }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            if (view == null)
                view = LayoutInflater.from(CartActivity.this).inflate(R.layout.activity_cart_item, parent, false);

            Product p = getItem(pos);

            ImageView imageView = view.findViewById(R.id.imageProduct);
            Glide.with(CartActivity.this).load(p.getImageUrl()).into(imageView); // Load ảnh từ URL

            TextView nameText = view.findViewById(R.id.textProductName);
            TextView priceText = view.findViewById(R.id.textProductPrice);
            TextView qtyText = view.findViewById(R.id.textProductQuantity);
            ImageButton btnIncrease = view.findViewById(R.id.buttonIncrease);
            ImageButton btnDecrease = view.findViewById(R.id.buttonDecrease);
            ImageButton btnRemove = view.findViewById(R.id.buttonRemove);

            nameText.setText(p.getName() + " (Size: " + p.getSize() + ")");
            priceText.setText("Thành tiền: " + (p.getPrice() * p.getQuantity()) + "đ");
            qtyText.setText(String.valueOf(p.getQuantity()));

            btnIncrease.setOnClickListener(v -> {
                p.setQuantity(p.getQuantity() + 1);
                notifyDataSetChanged();
                updateTotal();
                saveCartData();
            });

            btnDecrease.setOnClickListener(v -> {
                if (p.getQuantity() > 1) {
                    p.setQuantity(p.getQuantity() - 1);
                    notifyDataSetChanged();
                    updateTotal();
                    saveCartData();
                }
            });

            btnRemove.setOnClickListener(v -> {
                cartItems.remove(pos);
                notifyDataSetChanged();
                updateTotal();
                saveCartData();
            });

            return view;
        }
    }

    private void showCheckoutInfoDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_checkout_info, null);
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editPhone = dialogView.findViewById(R.id.editPhone);
        EditText editAddress = dialogView.findViewById(R.id.editAddress);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupPayment);

        new AlertDialog.Builder(this)
                .setTitle("Thông tin đặt hàng")
                .setView(dialogView)
                .setPositiveButton("Tiếp tục", (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    String phone = editPhone.getText().toString().trim();
                    String address = editAddress.getText().toString().trim();
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (selectedId == R.id.radioCash) {
                            showSuccessDialog(name, phone, address, "Tiền mặt");
                        } else if (selectedId == R.id.radioQR) {
                            showQRPaymentDialog(name, phone, address);
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showSuccessDialog(String name, String phone, String address, String method) {
        new AlertDialog.Builder(this)
                .setTitle("✅ Đặt hàng thành công")
                .setMessage("Cảm ơn bạn, " + name + "!\n\nPhương thức: " + method +
                        "\nĐịa chỉ: " + address + "\nSĐT: " + phone)
                .setPositiveButton("OK", (dialog, which) -> {
                    cartItems.clear();
                    saveCartData();
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                })
                .show();
    }

    private void showQRPaymentDialog(String name, String phone, String address) {
        ImageView qrImage = new ImageView(this);
        qrImage.setImageResource(R.drawable.qr_code); // Ảnh QR trong drawable
        qrImage.setPadding(40, 40, 40, 40);

        new AlertDialog.Builder(this)
                .setTitle("🔶 Quét mã QR để thanh toán")
                .setView(qrImage)
                .setPositiveButton("Tôi đã thanh toán", (dialog, which) -> {
                    showSuccessDialog(name, phone, address, "Mã QR");
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
