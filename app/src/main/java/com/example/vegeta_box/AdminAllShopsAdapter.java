package com.example.vegeta_box;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminAllShopsAdapter extends RecyclerView.Adapter<AdminAllShopsAdapter.ViewHolder> {
    private Context context;
    private List<AdminAllShopsData> shopList;

    public AdminAllShopsAdapter(Context context, List<AdminAllShopsData> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shop_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminAllShopsData shop = shopList.get(position);
        holder.adminShopName.setText(shop.getName());
        holder.adminShopEmail.setText(shop.getEmail());
        holder.adminShopProvince.setText(shop.getProvince());
        holder.adminShopDistrict.setText(shop.getDistrict());
        holder.adminShopAddress.setText(shop.getAddress());
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView adminShopName, adminShopEmail, adminShopProvince, adminShopDistrict, adminShopAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adminShopName = itemView.findViewById(R.id.admin_shop_name_);
            adminShopEmail = itemView.findViewById(R.id.admin_shop_email_);
            adminShopProvince = itemView.findViewById(R.id.admin_shop_province_);
            adminShopDistrict = itemView.findViewById(R.id.admin_shop_district_);
            adminShopAddress = itemView.findViewById(R.id.admin_shop_address_);
        }
    }
}
