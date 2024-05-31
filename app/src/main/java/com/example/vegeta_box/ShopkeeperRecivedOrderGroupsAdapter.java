package com.example.vegeta_box;// ShopkeeperRecivedOrderGroupsAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShopkeeperRecivedOrderGroupsAdapter extends RecyclerView.Adapter<ShopkeeperRecivedOrderGroupsAdapter.ViewHolder> {
    private Context context;
    private List<ShopkeeperRecivedOrderGroupsData> groupList;
    private OnGroupClickListener listener;

    public ShopkeeperRecivedOrderGroupsAdapter(Context context, List<ShopkeeperRecivedOrderGroupsData> groupList, OnGroupClickListener listener) {
        this.context = context;
        this.groupList = groupList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_all_groups, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopkeeperRecivedOrderGroupsData group = groupList.get(position);
        holder.groupName.setText(group.getName());

        // Set OnClickListener for the whole card view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onGroupClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.shopkeeper_all_groups_name);
        }
    }

    // Interface for handling group click events
    public interface OnGroupClickListener {
        void onGroupClick(int position);
    }
}
