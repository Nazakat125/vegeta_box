package com.example.vegeta_box;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ShopKeeperProductsDetail extends AppCompatActivity {
ImageView image;
TextView name,price,detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_keeper_products_detail);
        init();
        String getImage = getIntent().getStringExtra("image");
        String getName = getIntent().getStringExtra("name");
        String getPrice = getIntent().getStringExtra("price");
        String getDetail = getIntent().getStringExtra("detail");
        name.setText(getName);
        price.setText(getPrice);
        detail.setText(getDetail);
        Glide.with(this).load(getImage).into(image);
    }
    void  init(){
    image = findViewById(R.id.shopkeeper_view_products_details_image);
    name = findViewById(R.id.shopkeeper_view_products_details_name);
    price = findViewById(R.id.shopkeeper_view_products_details_price);
    detail = findViewById(R.id.shopkeeper_view_products_details_discription);
    }
}