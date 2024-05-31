package com.example.vegeta_box;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {
    private Context context;
    private List<CustomerCartData> cartList;
    private CartButtonClickListener listener;

    public CustomerCartAdapter(Context context, List<CustomerCartData> cartList, CartButtonClickListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_in_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerCartData cartItem = cartList.get(position);
        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(cartItem.getProductPrice());
        Glide.with(context).load(cartItem.getProductImage()).into(holder.productImage);
        holder.totalItem.setText(String.valueOf(cartItem.getTotalItem()));

        // Handle add button click
        holder.addBtn.setOnClickListener(view -> {
            if (listener != null) {
                listener.onAddButtonClick(position);
            }
        });

        // Handle subtract button click
        holder.subtractBtn.setOnClickListener(view -> {
            if (listener != null) {
                listener.onSubtractButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, addBtn, subtractBtn;
        TextView productName, productPrice, totalItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_image);
            addBtn = itemView.findViewById(R.id.cart_add_button);
            subtractBtn = itemView.findViewById(R.id.cart_subtract_button);
            productName = itemView.findViewById(R.id.cart_title);
            productPrice = itemView.findViewById(R.id.cart_price);
            totalItem = itemView.findViewById(R.id.cart_total_item);
        }
    }

    // Interface to communicate button clicks back to the CustomerCart class
    public interface CartButtonClickListener {
        void onAddButtonClick(int position);
        void onSubtractButtonClick(int position);
    }
}
