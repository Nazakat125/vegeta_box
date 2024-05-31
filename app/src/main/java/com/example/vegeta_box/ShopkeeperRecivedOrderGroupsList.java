package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopkeeperRecivedOrderGroupsList extends AppCompatActivity implements ShopkeeperRecivedOrderGroupsAdapter.OnGroupClickListener {
    RecyclerView recyclerView;
    String key;
    ShopkeeperRecivedOrderGroupsAdapter adapter;
    List<ShopkeeperRecivedOrderGroupsData> groupList;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_recived_order_groups_list);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        recyclerView = findViewById(R.id.shopkeeper_all_groups_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the group list and adapter
        groupList = new ArrayList<>();
        adapter = new ShopkeeperRecivedOrderGroupsAdapter(this, groupList, this);
        recyclerView.setAdapter(adapter);

        // Retrieve the key from the intent
        key = getIntent().getStringExtra("key");

        fetchOrderGroupsFromFirebase();
    }

    private void fetchOrderGroupsFromFirebase() {
        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(key).child("Order");
        Log.d("Data",key);
        // Add a listener for single event to fetch the data once
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing groupList
                groupList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Data",snapshot.toString());
                    String groupName = snapshot.getKey();
                    Log.d("Data",groupName);
                    groupList.add(new ShopkeeperRecivedOrderGroupsData(groupName));
                    progressDialog.dismiss();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onGroupClick(int position) {
        Intent intent = new Intent(ShopkeeperRecivedOrderGroupsList.this, ShopkeeperShowOrder.class);
        intent.putExtra("key",key);
        intent.putExtra("gName",groupList.get(position).getName());
        startActivity(intent);
    }
}
