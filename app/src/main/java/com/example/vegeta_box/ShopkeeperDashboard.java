package com.example.vegeta_box;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShopkeeperDashboard extends AppCompatActivity {
    ViewFlipper viewFlipper;
    CardView addProducts,viewProducts,viewOrder;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_dashboard);
        init();
        key = getIntent().getStringExtra("key");
        viewFlipper.setInAnimation(ShopkeeperDashboard.this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(ShopkeeperDashboard.this, android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.startFlipping();
        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopkeeperDashboard.this, AddProducts.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopkeeperDashboard.this, ShopKeeperShowAllProduct.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopkeeperDashboard.this, ShopkeeperRecivedOrderGroupsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

    }
    void init () {
        viewFlipper = findViewById(R.id.shopKeeper_flipper);
        addProducts = findViewById(R.id.shopKeeper_add_products);
        viewProducts = findViewById(R.id.shopKeeper_manage_products);
        viewOrder = findViewById(R.id.shopKeeper_view_order);

    }
}