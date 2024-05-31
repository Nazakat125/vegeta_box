package com.example.vegeta_box;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WellcomeScreen extends AppCompatActivity {
    Button admin,shopkeeper,customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_screen);
        init();
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellcomeScreen.this,CustomerLogIn.class);
                startActivity(intent);
            }
        });
        shopkeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellcomeScreen.this,ShopLogIn.class);
                startActivity(intent);
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellcomeScreen.this,AdminLogin.class);
                startActivity(intent);
            }
        });
    }
    void init(){
        admin = findViewById(R.id.admin);
        shopkeeper = findViewById(R.id.shopkeeper);
        customer = findViewById(R.id.customer);
    }
}