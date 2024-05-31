package com.example.vegeta_box;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class customerCart extends AppCompatActivity implements CustomerCartAdapter.CartButtonClickListener {
    RecyclerView recyclerView;
    TextView subTotal, deliveryCharges, total;
    Button placeOrder;
    ProgressDialog progressDialog;
    String key,gArea;
    int totalBill = 0;
    int sub = 0;
    int delivery = 100;
    int totalItem = 1;

    // Adapter and list to hold cart data
    CustomerCartAdapter adapter;
    List<CustomerCartData> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cart);
        init();
        key = getIntent().getStringExtra("key");
        gArea = getIntent().getStringExtra("gArea");
        fetchDataFromFirebase();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.successfull_dialog);
        dialog.setCancelable(false);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                progressDialog.show();
                for (int i = 0; i < cartList.size(); i++) {
                    CustomerCartData cartItem = cartList.get(i);
                    String randomKey = UUID.randomUUID().toString();

                    DatabaseReference ref = db.getReference(cartItem.getShopName()).child("Order").child(key).child(randomKey);
                    Map<String , Object> data = new HashMap<>();
                    data.put("Prouct Name",cartItem.getProductName());
                    data.put("Prouct Image",cartItem.getProductImage());
                    data.put("Prouct Price",cartItem.getProductPrice());
                    data.put("Prouct item",cartItem.getTotalItem());
                    data.put("Group Name",key);
                    data.put("Group Area",gArea);
                    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(customerCart.this,"Order Placed",Toast.LENGTH_LONG).show();
                            DatabaseReference deleteNode = db.getReference("Groups").child(key).child("Cart");
                            DatabaseReference orderPlaced = db.getReference("Groups").child(key).child("OrderPlaced");
                            Map<String , Object> orderPlace = new HashMap<>();
                            orderPlace.put("Prouct Name",cartItem.getProductName());
                            orderPlace.put("Prouct Image",cartItem.getProductImage());
                            orderPlace.put("Prouct Price",cartItem.getProductPrice());
                            orderPlace.put("Prouct item",cartItem.getTotalItem());
                            orderPlace.put("Group Name",cartItem.getShopName());
                            orderPlace.put("Sub Total",sub);
                            orderPlace.put("Total Bill",totalBill);
                            orderPlaced.setValue(orderPlace).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.show();


                                    // Delay the dismissal of the dialog by 2 seconds
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Intent intent = new Intent(customerCart.this, CustomerAllShopsKeeprsList.class);
                                            intent.putExtra("key",key);
                                            deleteNode.removeValue();
                                            startActivity(intent);
                                        }
                                    }, 2000);

                                }
                            });
                        }
                    });
                }
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }

    void init() {
        recyclerView = findViewById(R.id.cart_recy);
        subTotal = findViewById(R.id.cart_sub_total);
        deliveryCharges = findViewById(R.id.cart_delivery_charges);
        total = findViewById(R.id.cart_total_bill);
        placeOrder = findViewById(R.id.cart_place_order);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        deliveryCharges.setText(String.valueOf(delivery));
        cartList = new ArrayList<>();
        adapter = new CustomerCartAdapter(this, cartList, this); // Pass the activity as listener

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    void fetchDataFromFirebase() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Groups").child(key).child("Cart");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartList.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productName = snapshot.child("Product Name").getValue(String.class);
                    String productPrice = snapshot.child("Product Price").getValue(String.class);
                    String productImage = snapshot.child("Product Image").getValue(String.class);
                    String shopName = snapshot.child("Shop Name").getValue(String.class);
                    int spaceIndex = productPrice.indexOf(" ");
                    String priceString = productPrice.substring(0, spaceIndex);
                    int priceValue = Integer.parseInt(priceString);
                    sub += priceValue * totalItem;
                    totalBill = sub + delivery;
                    subTotal.setText(String.valueOf(sub));
                    total.setText(String.valueOf(totalBill));
                    CustomerCartData cartItem = new CustomerCartData(productImage, productName, productPrice, shopName,totalItem,sub,totalBill);
                    cartList.add(cartItem);
                }
                adapter.notifyDataSetChanged(); // Notify the adapter about the new data
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    @Override
    public void onAddButtonClick(int position) {

        cartList.get(position).setTotalItem(cartList.get(position).getTotalItem() + 1);
        adapter.notifyDataSetChanged();
        CustomerCartData data = cartList.get(position);
        String getPrice = data.getProductPrice();
        int spaceIndex = getPrice.indexOf(" ");
        String priceString = getPrice.substring(0, spaceIndex);
        int priceValue = Integer.parseInt(priceString);
        sub += priceValue;
        totalBill = sub + delivery;
        subTotal.setText(String.valueOf(sub));
        total.setText(String.valueOf(totalBill));

    }

    @Override
    public void onSubtractButtonClick(int position) {
        if(cartList.get(position).getTotalItem() > 1){
            cartList.get(position).setTotalItem(cartList.get(position).getTotalItem() - 1);
            adapter.notifyDataSetChanged();
            CustomerCartData data = cartList.get(position);
            String getPrice = data.getProductPrice();
            int spaceIndex = getPrice.indexOf(" ");
            String priceString = getPrice.substring(0, spaceIndex);
            int priceValue = Integer.parseInt(priceString);
            sub -= priceValue;
            totalBill = sub + delivery;
            subTotal.setText(String.valueOf(sub));
            total.setText(String.valueOf(totalBill));

        }

    }
}
