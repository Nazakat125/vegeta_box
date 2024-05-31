package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerAllShopsKeeprsList extends AppCompatActivity implements CustomerShowAllShopsAdapter.OnItemClickListener {

    TextView groupName, groupArea;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String key;
    CustomerShowAllShopsAdapter adapter;
    List<CustomerShowAllShopsData> dataList;
    String gName,gArea;
    ImageView cartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_all_shops_keeprs_list);
        init();

        key = getIntent().getStringExtra("key");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new CustomerShowAllShopsAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        fetchGroupDetails();
        fetchShopkeepersList();
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerAllShopsKeeprsList.this, customerCart.class);
                intent.putExtra("key",gName);
                intent.putExtra("gArea",gArea);
                startActivity(intent);
            }
        });
    }

    private void init() {
        groupName = findViewById(R.id.custmer_group_name);
        groupArea = findViewById(R.id.custmer_group_area);
        recyclerView = findViewById(R.id.custmer_all_shops_recy);
        cartBtn = findViewById(R.id.custmer_cart_btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void fetchGroupDetails() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference groupRef = db.getReference(key).child("Group");
        groupRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                 gName = dataSnapshot.child("Name").getValue(String.class);
                 gArea = dataSnapshot.child("Area").getValue(String.class);
                groupName.setText(gName);
                groupArea.setText(gArea);
            }
        });
    }

    private void fetchShopkeepersList() {
        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference shopsRef = db.getReference("Admin").child("Shops");
        shopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String shopName = snapshot.child("Name").getValue(String.class);
                    String userName = snapshot.child("User Name").getValue(String.class);
                    dataList.add(new CustomerShowAllShopsData(shopName, userName));
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        CustomerShowAllShopsData data = dataList.get(position);
        Intent intent = new Intent(this, CustomerAllProductsList.class);
        intent.putExtra("key", data.getUserName());
        intent.putExtra("Group Name", gName);
        intent.putExtra("userName", key);
        startActivity(intent);
    }
}
