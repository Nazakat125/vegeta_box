package com.example.vegeta_box;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ShopKeeperAllProductsAdapter extends RecyclerView.Adapter<ShopKeeperAllProductsAdapter.ViewHolder> {
    private List<ShopKeeperAllProductsData> productList;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ShopKeeperAllProductsAdapter(Context context, List<ShopKeeperAllProductsData> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopkeeper_all_product_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopKeeperAllProductsData product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        holder.detail.setText(product.getDetail());

        Glide.with(context)
                .load(product.getImage())
                .into(holder.image);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView detail;
        ImageView image;
        CardView btn;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shopkeeper_view_products_name);
            price = itemView.findViewById(R.id.shopkeeper_view_products_prices);
            detail = itemView.findViewById(R.id.shopkeeper_view_products_details);
            image = itemView.findViewById(R.id.shopkeeper_view_products_images);
            btn = itemView.findViewById(R.id.ShooperKeeperProductDetailBtn);
        }
    }
}
