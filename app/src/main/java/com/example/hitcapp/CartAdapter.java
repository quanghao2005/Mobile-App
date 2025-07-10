package com.example.hitcapp.cart_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hitcapp.R;
import com.example.hitcapp.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private OnCartActionListener listener;

    public interface OnCartActionListener {
        void onIncrease(int position);
        void onDecrease(int position);
        void onRemove(int position);
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, OnCartActionListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.textProductName.setText(item.getName());
        holder.textProductQuantity.setText(String.valueOf(item.getQuantity()));

        // Giá dựa trên đơn giá * số lượng
        int totalPrice = item.getPrice() * item.getQuantity();
        holder.textProductPrice.setText(totalPrice + "đ");

        Glide.with(context).load(item.getImageResId()).into(holder.imageProduct);

        holder.buttonIncrease.setOnClickListener(v -> listener.onIncrease(position));
        holder.buttonDecrease.setOnClickListener(v -> listener.onDecrease(position));
        holder.buttonRemove.setOnClickListener(v -> listener.onRemove(position));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName, textProductPrice, textProductQuantity;
        ImageButton buttonIncrease, buttonDecrease, buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            textProductQuantity = itemView.findViewById(R.id.textProductQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
