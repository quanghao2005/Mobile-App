package com.example.hitcapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.productList = products;
    }

    public void setProducts(List<Product> list) {
        this.productList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.textName.setText(p.getName());
        holder.textPrice.setText("Giá: " + p.getPrice() + "đ");

        Glide.with(context).load(p.getImageUrl()).into(holder.imageProduct);

        // 👉 Chi tiết sản phẩm
        holder.buttonDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", p.getName());
            intent.putExtra("description", p.getSize());
            intent.putExtra("price", p.getPrice());
            intent.putExtra("imageUrl", p.getImageUrl());
            context.startActivity(intent);
        });

        // 👉 Mua ngay
        holder.buttonBuy.setOnClickListener(v -> {
            if (p == null || p.getName() == null || p.getName().isEmpty()) {
                Toast.makeText(context, "Sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (productList == null || productList.isEmpty()) {
                Toast.makeText(context, "Không có sản phẩm để mua", Toast.LENGTH_SHORT).show();
                return;
            }

            View formView = LayoutInflater.from(context).inflate(R.layout.dialog_checkout_info, null);
            EditText editName = formView.findViewById(R.id.editName);
            EditText editPhone = formView.findViewById(R.id.editPhone);
            EditText editAddress = formView.findViewById(R.id.editAddress);
            RadioGroup radioGroup = formView.findViewById(R.id.radioGroupPayment);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(formView);
            builder.setTitle("Thông tin đặt hàng");
            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();
                String address = editAddress.getText().toString().trim();
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedId == R.id.radioCash) {
                    saveToCart(p);
                    showSuccessDialog();
                } else if (selectedId == R.id.radioQR) {
                    saveToCart(p);
                    showQRDialog();
                }

            });
            builder.setNegativeButton("Hủy", null);
            builder.show();
        });
    }

    // ✅ Thêm vào giỏ hàng (SharedPreferences)
    private void saveToCart(Product p) {
        SharedPreferences prefs = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String json = prefs.getString("cart_items", "[]");

        try {
            JSONArray cartArray = new JSONArray(json);
            boolean found = false;

            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject obj = cartArray.getJSONObject(i);
                if (obj.getString("name").equals(p.getName())) {
                    int oldQty = obj.getInt("quantity");
                    obj.put("quantity", oldQty + 1);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JSONObject newItem = new JSONObject();
                newItem.put("name", p.getName());
                newItem.put("imageUrl", p.getImageUrl());
                newItem.put("price", p.getPrice());
                newItem.put("quantity", 1);
                newItem.put("size", ""); // để đồng bộ với DetailActivity
                cartArray.put(newItem);
            }

            prefs.edit().putString("cart_items", cartArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showQRDialog() {
        View qrView = LayoutInflater.from(context).inflate(R.layout.dialog_qr_payment, null);
        Button btnConfirm = qrView.findViewById(R.id.buttonConfirmPayment);

        AlertDialog qrDialog = new AlertDialog.Builder(context)
                .setView(qrView)
                .setCancelable(false)
                .create();

        btnConfirm.setOnClickListener(v -> {
            qrDialog.dismiss();
            showSuccessDialog();
        });

        qrDialog.show();
    }

    private void showSuccessDialog() {
        View successView = LayoutInflater.from(context).inflate(R.layout.dialog_checkout_success, null);
        AlertDialog.Builder successBuilder = new AlertDialog.Builder(context);
        successBuilder.setView(successView);
        successBuilder.setCancelable(false);
        successBuilder.setPositiveButton("OK", null);
        successBuilder.show();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textName, textPrice;
        Button buttonDetail, buttonBuy;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            buttonDetail = itemView.findViewById(R.id.buttonDetail);
            buttonBuy = itemView.findViewById(R.id.buttonBuy);
        }
    }
}
