package com.example.vegeta_box;// ShopkeeperShowOrderAdapter.java
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

public class ShopkeeperShowOrderAdapter extends RecyclerView.Adapter<ShopkeeperShowOrderAdapter.ViewHolder> {
    private Context context;
    private List<ShopkeeperShowOrderData> orderList;

    public ShopkeeperShowOrderAdapter(Context context, List<ShopkeeperShowOrderData> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopkeeper_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopkeeperShowOrderData order = orderList.get(position);
        holder.titleTextView.setText(order.getTitle());
        holder.priceTextView.setText(order.getPrice());
        holder.totalItemTextView.setText(order.getTotalItem());
        Glide.with(context).load(order.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, priceTextView, totalItemTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shopkeeper_order_image);
            titleTextView = itemView.findViewById(R.id.shopkeeper_order_title);
            priceTextView = itemView.findViewById(R.id.shopkeeper_order_price);
            totalItemTextView = itemView.findViewById(R.id.shopkeeper_order_total_item);
        }
    }
}
