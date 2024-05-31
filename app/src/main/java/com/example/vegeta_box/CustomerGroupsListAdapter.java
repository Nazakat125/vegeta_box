package com.example.vegeta_box;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerGroupsListAdapter extends RecyclerView.Adapter<CustomerGroupsListAdapter.ViewHolder> {
    private Context context;
    private List<CustomerGroupsListData> dataList;
    private OnJoinButtonClickListener joinButtonClickListener;

    public CustomerGroupsListAdapter(Context context, List<CustomerGroupsListData> dataList, OnJoinButtonClickListener joinButtonClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.joinButtonClickListener = joinButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerGroupsListData data = dataList.get(position);
        holder.nameTextView.setText(data.getName());
        holder.areaTextView.setText(data.getArea());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView areaTextView;
        Button joinBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.customer_group_name_j);
            areaTextView = itemView.findViewById(R.id.admin_shop_province_);
            joinBtn = itemView.findViewById(R.id.customer_group_join_btn_j);

            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && joinButtonClickListener != null) {
                        joinButtonClickListener.onJoinButtonClick(position);
                    }
                }
            });
        }
    }

    public interface OnJoinButtonClickListener {
        void onJoinButtonClick(int position);
    }
}
