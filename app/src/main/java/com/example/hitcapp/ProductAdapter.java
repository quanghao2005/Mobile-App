package com.example.hitcapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

        // 👉 Mua ngay → thêm vào SharedPreferences (kiểu JSON array)
        holder.buttonBuy.setOnClickListener(v -> {
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
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
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
