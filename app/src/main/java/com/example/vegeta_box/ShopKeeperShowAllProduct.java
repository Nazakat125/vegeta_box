package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
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
import java.util.List;

public class ShopKeeperShowAllProduct extends AppCompatActivity implements ShopKeeperAllProductsAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    LinearLayout noData;
    ProgressDialog progressDialog;
    ShopKeeperAllProductsAdapter adapter;
    List<ShopKeeperAllProductsData> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_keeper_show_all_product);
        init();
        String key = getIntent().getStringExtra("key");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(key).child("Products");

        progressDialog.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("data",postSnapshot.toString());
                    String detail = postSnapshot.child("Product Detail").getValue(String.class);
                    String name = postSnapshot.child("Product Name").getValue(String.class);
                    String image = postSnapshot.child("Product image").getValue(String.class);
                    String price = postSnapshot.child("Product Prict").getValue(String.class);
                    ShopKeeperAllProductsData product = new ShopKeeperAllProductsData(image,name,price + " RS(per kg)",detail);
                    productList.add(product);
                }
                if (productList.isEmpty()) {
                    noData.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                } else {
                    noData.setVisibility(View.GONE);
                    adapter = new ShopKeeperAllProductsAdapter(ShopKeeperShowAllProduct.this, productList);
                    adapter.setOnItemClickListener(ShopKeeperShowAllProduct.this);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShopKeeperShowAllProduct.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    void init(){
        recyclerView = findViewById(R.id.shopkeeper_view_all_product_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noData = findViewById(R.id.shopkeeper_shop_list_nodata_);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        productList = new ArrayList<>();
    }

    @Override
    public void onItemClick(int position) {
        // Handle the item click here
        ShopKeeperAllProductsData product = productList.get(position);
        Intent intent = new Intent(ShopKeeperShowAllProduct.this, ShopKeeperProductsDetail.class);
        intent.putExtra("image", product.getImage());
        intent.putExtra("name", product.getName());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("detail", product.getDetail());
        startActivity(intent);
    }

}
