package com.example.vegeta_box;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class ShopkeeperShowOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView groupName, groupArea, subTotal, delivery, total;
    Button deliverOrder;
    String key, groupKey;
    ProgressDialog progressDialog;
    ShopkeeperShowOrderAdapter adapter;
    List<ShopkeeperShowOrderData> orderList;
    String gName,gArea;
    int sub = 0;
    int totalBill = 0;
    int deliveryCharges = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_show_order);
        init();
        key = getIntent().getStringExtra("key");
        groupKey = getIntent().getStringExtra("gName");
        fetchOrderDataFromFirebase();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.successfull_dialog);
        dialog.setCancelable(false);
        deliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference shopkeeperRef = db.getReference(key).child("Order").child(groupKey);
                DatabaseReference groupRef = db.getReference("Groups").child(groupKey).child("OrderPlaced");
                shopkeeperRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        groupRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                               dialog.show();

                                // Delay the dismissal of the dialog by 2 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    dialog.dismiss();
                                    startActivity(new Intent(ShopkeeperShowOrder.this, WellcomeScreen.class));
                                    }
                                }, 2000);
                            }
                        });
                    }
                });

            }
        });

    }

    void init() {
        recyclerView = findViewById(R.id.shopkeeper_cart_recy);
        groupName = findViewById(R.id.shopkeeper_cart_group_name);
        groupArea = findViewById(R.id.shopkeeper_cart_group_area);
        subTotal = findViewById(R.id.shopkeeper_cart_sub_total);
        delivery = findViewById(R.id.shopkeeper_cart_group_delivery_charges);
        total = findViewById(R.id.shopkeeper_cart_group_total_bill);
        deliverOrder = findViewById(R.id.shopkeeper_cart_group_delivery_order);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        // Initialize the order list and adapter
        orderList = new ArrayList<>();
        adapter = new ShopkeeperShowOrderAdapter(this, orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    void fetchOrderDataFromFirebase() {
        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(key).child("Order").child(groupKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear(); // Clear existing data
                // Loop through the dataSnapshot to retrieve each order
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve order details
                    String title = snapshot.child("Prouct Name").getValue(String.class);
                    String price = snapshot.child("Prouct Price").getValue(String.class);
                    Long totalItem = snapshot.child("Prouct item").getValue(Long.class);
                    String image =snapshot.child("Prouct Image").getValue(String.class) ;
                     gArea =snapshot.child("Group Area").getValue(String.class) ;
                     gName =snapshot.child("Group Name").getValue(String.class) ;

                    int spaceIndex = price.indexOf(" ");
                    String priceString = price.substring(0, spaceIndex);
                    int priceValue = Integer.parseInt(priceString);
                     sub += priceValue * totalItem;
                     totalBill = sub + deliveryCharges;
                     delivery.setText(String.valueOf(deliveryCharges));
                     subTotal.setText(String.valueOf(sub));
                     total.setText(String.valueOf(totalBill));
                     groupName.setText(gName);
                     groupArea.setText(gArea);
                     progressDialog.dismiss();
                     ShopkeeperShowOrderData order = new ShopkeeperShowOrderData(image, title, price, String.valueOf(totalItem));
                    orderList.add(order);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }


}
