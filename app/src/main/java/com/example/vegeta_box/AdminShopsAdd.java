// AdminShopsAdd.java
package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
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

public class AdminShopsAdd extends AppCompatActivity implements AdminShopsAddAdapter.OnAcceptDeleteClickListener {

    private RecyclerView adminShopRecy;
    private LinearLayout noData;
    private List<AdminShopsAddData> dataList;
    private AdminShopsAddAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shops_add);
        init();
        setupRecyclerView();

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Admin").child("Requests"); // Replace "shops" with your database reference
        progressDialog.show();
        // Fetch data from Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear(); // Clear existi
                progressDialog.dismiss();// ng data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Data",snapshot.toString());
                    String name = snapshot.child("Name").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String province = snapshot.child("Province").getValue(String.class);
                    String district = snapshot.child("District").getValue(String.class);
                    String address = snapshot.child("Address").getValue(String.class);
                    String userName = snapshot.child("User Name").getValue(String.class);
                    String key = snapshot.getKey().toString();
                    progressDialog.dismiss();
                    AdminShopsAddData data = new AdminShopsAddData(key,userName,name,email,province,district,address);
                    dataList.add(data);
                }
                adapter.notifyDataSetChanged(); // Notify adapter of data changes
                if (dataList.isEmpty()) {
                    noData.setVisibility(View.VISIBLE);
                } else {
                    noData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminShopsAdd.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        adminShopRecy = findViewById(R.id.admin_shop_list_recy);
        noData = findViewById(R.id.addmin_shop_list_nodata);
        dataList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adminShopRecy.setLayoutManager(layoutManager);
        adapter = new AdminShopsAddAdapter(dataList, this);
        adminShopRecy.setAdapter(adapter);
    }

    @Override
    public void onAcceptClick(AdminShopsAddData data) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(data.getUserName()).child("Profile");
        DatabaseReference ref2 = db.getReference("Admin").child("Shops").child(data.getKey());
        Map<String , Object> update = new HashMap<>();
        update.put("Permission","true");
        progressDialog.show();
        ref.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Map<String , Object> saveData = new HashMap<>();
                saveData.put("Name",data.getName());
                saveData.put("User Name",data.getUserName());
                saveData.put("Address",data.getAddress());
                saveData.put("Email",data.getEmail());
                saveData.put("District",data.getDistrict());
                saveData.put("Province",data.getProvince());
                ref2.setValue(saveData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference ref3 = db.getReference("Admin").child("Requests").child(data.getKey());
                        ref3.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminShopsAdd.this, "Shop Allowed to Work", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AdminShopsAdd.this, "Failed to delete child node: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminShopsAdd.this,e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AdminShopsAdd.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDeleteClick(AdminShopsAddData data) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(data.getUserName()).child("Profile");
        progressDialog.show();
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference ref3 = db.getReference("Admin").child("Requests").child(data.getKey());
                ref3.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminShopsAdd.this, "Shop Deleted", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminShopsAdd.this, "Failed to delete child node: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AdminShopsAdd.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
