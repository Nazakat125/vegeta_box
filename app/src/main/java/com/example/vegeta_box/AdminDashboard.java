package com.example.vegeta_box;

import android.app.ProgressDialog;
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

public class AdminDashboard extends AppCompatActivity {
    ViewFlipper viewFlipper;
    CardView addShop,manageShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        init();
        viewFlipper.setInAnimation(AdminDashboard.this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(AdminDashboard.this, android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.startFlipping();
        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, AdminShopsAdd.class);
                startActivity(intent);
            }
        });
        manageShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, AdminAllShops.class);
                startActivity(intent);
            }
        });
    }
    void init () {
        viewFlipper = findViewById(R.id.admin_flipper);
        addShop = findViewById(R.id.add_shop);
        manageShop = findViewById(R.id.shop_list);

    }
}