package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerGroups extends AppCompatActivity implements CustomerGroupsListAdapter.OnJoinButtonClickListener {

    private RecyclerView recyclerView;
    private LinearLayout noData;
    private ProgressDialog progressDialog;
    private CustomerGroupsListAdapter adapter;
    private List<CustomerGroupsListData> dataList;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_groups);
        initViews();
        key = getIntent().getStringExtra("key");
        initRecyclerView();
        fetchGroupsData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.customer_group_list_recy);
        noData = findViewById(R.id.customer_group_list_no_data);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void initRecyclerView() {
        dataList = new ArrayList<>();
        adapter = new CustomerGroupsListAdapter(this, dataList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchGroupsData() {
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Groups");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("Group Info").child("Name").getValue(String.class);
                    String area = snapshot.child("Group Info").child("Area").getValue(String.class);
                    dataList.add(new CustomerGroupsListData(name, area));
                }
                adapter.notifyDataSetChanged(); // Notify adapter about the new data
                progressDialog.dismiss();

                // Update visibility of noData layout
                if (dataList.isEmpty()) {
                    noData.setVisibility(LinearLayout.VISIBLE);
                } else {
                    noData.setVisibility(LinearLayout.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onJoinButtonClick(int position) {
        CustomerGroupsListData data = dataList.get(position);
        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(key).child("Group");
        Map<String , Object> addGroups = new HashMap<>();
        addGroups.put("Name",data.getName());
        addGroups.put("Area",data.getArea());
        ref.setValue(addGroups).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String randomKey = UUID.randomUUID().toString();
                DatabaseReference ref2 = db.getReference("Groups").child(data.getName()).child("Memebers").child(randomKey);
                Map<String , Object> addMember = new HashMap<>();
                addMember.put("Name",key);
                ref2.setValue(addMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(CustomerGroups.this,CustomerDashboard.class);
                        intent.putExtra("key",key);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
