package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAllShops extends AppCompatActivity {
    private RecyclerView adminShopRecy;
    private LinearLayout noData;
    private ProgressDialog progressDialog;
    private AdminAllShopsAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<AdminAllShopsData> dataList = new ArrayList<>(); // Declaration and initialization of dataList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_shops);
        init();
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Data", snapshot.toString());
                    String name = snapshot.child("Name").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String province = snapshot.child("Province").getValue(String.class);
                    String district = snapshot.child("District").getValue(String.class);
                    String address = snapshot.child("Address").getValue(String.class);
                    progressDialog.dismiss();
                    AdminAllShopsData data = new AdminAllShopsData(name,email,province,district,address);
                    dataList.add(data);
                }
                Log.d("DataList", "Size: " + dataList.size()); // Add this log to check the size of dataList
                adapter.notifyDataSetChanged(); // Notify adapter of data changes
                if (dataList.isEmpty()) {
                    noData.setVisibility(View.VISIBLE);
                } else {
                    noData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminAllShops.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        adminShopRecy = findViewById(R.id.admin_shop_list_recy_);
        noData = findViewById(R.id.addmin_shop_list_nodata_);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        adminShopRecy.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminAllShopsAdapter(this, dataList); // Pass dataList here
        adminShopRecy.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child("Shops");
    }



}
