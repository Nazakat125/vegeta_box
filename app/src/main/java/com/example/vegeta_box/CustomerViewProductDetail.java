package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomerViewProductDetail extends AppCompatActivity {
    ImageView image;
    TextView name,price,detail;
    Button addToCart;
    String groupName,getImage,getName,getPrice,getDetail,shopName,userName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_product_detail);
        init();
         getImage = getIntent().getStringExtra("image");
         getName = getIntent().getStringExtra("name");
         getPrice = getIntent().getStringExtra("price");
         getDetail = getIntent().getStringExtra("detail");
         shopName = getIntent().getStringExtra("shopName");
         groupName = getIntent().getStringExtra("Group Name");
        userName = getIntent().getStringExtra("userName");
        name.setText(getName);
        price.setText(getPrice);
        detail.setText(getDetail);
        Glide.with(this).load(getImage).into(image);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                String randomKey = UUID.randomUUID().toString();
                DatabaseReference ref = db.getReference("Groups").child(groupName).child("Cart").child(randomKey);
                Map<String , Object> data = new HashMap<>();
                data.put("Product Name",getName);
                data.put("Product Price",getPrice);
                data.put("Product Image",getImage);
                data.put("Shop Name",shopName);
                Log.d("data",shopName);
                ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CustomerViewProductDetail.this,"Product Added to Cart",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CustomerViewProductDetail.this,CustomerAllShopsKeeprsList.class);
                        intent.putExtra("key",userName);
                        startActivity(intent);

                    }
                });
            }
        });

    }
    void  init(){
        image = findViewById(R.id.customer_view_products_details_image);
        name = findViewById(R.id.customer_view_products_details_name);
        price = findViewById(R.id.customer_view_products_details_price);
        detail = findViewById(R.id.customer_view_products_details_discription);
        addToCart = findViewById(R.id.customer_add_to_cart);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}