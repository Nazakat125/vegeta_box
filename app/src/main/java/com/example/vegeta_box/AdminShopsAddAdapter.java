// AdminShopsAddAdapter.java
package com.example.vegeta_box;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminShopsAddAdapter extends RecyclerView.Adapter<AdminShopsAddAdapter.AdminShopsAddViewHolder> {

    private List<AdminShopsAddData> dataList;
    private OnAcceptDeleteClickListener listener;

    public AdminShopsAddAdapter(List<AdminShopsAddData> dataList, OnAcceptDeleteClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminShopsAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_shops_list, parent, false);
        return new AdminShopsAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminShopsAddViewHolder holder, int position) {
        AdminShopsAddData data = dataList.get(position);
        holder.bindData(data);
        holder.accept.setOnClickListener(v -> listener.onAcceptClick(data));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(data));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class AdminShopsAddViewHolder extends RecyclerView.ViewHolder {

        TextView shopName, shopEmail, shopProvince, shopDistrict, shopAddress;
        Button accept, delete;

        public AdminShopsAddViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.admin_shop_name);
            shopEmail = itemView.findViewById(R.id.admin_shop_email);
            shopProvince = itemView.findViewById(R.id.admin_shop_province);
            shopDistrict = itemView.findViewById(R.id.admin_shop_district);
            shopAddress = itemView.findViewById(R.id.admin_shop_address);
            accept = itemView.findViewById(R.id.admin_shop_accept);
            delete = itemView.findViewById(R.id.admin_shop_delete);
        }

        void bindData(AdminShopsAddData data) {
            shopName.setText(data.getName());
            shopEmail.setText(data.getEmail());
            shopProvince.setText(data.getProvince());
            shopDistrict.setText(data.getDistrict());
            shopAddress.setText(data.getAddress());
        }
    }

    interface OnAcceptDeleteClickListener {
        void onAcceptClick(AdminShopsAddData data);
        void onDeleteClick(AdminShopsAddData data);
    }
}
