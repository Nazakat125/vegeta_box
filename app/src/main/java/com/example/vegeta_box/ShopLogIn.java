package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ShopLogIn extends AppCompatActivity {
    EditText userName,password;
    Button login;
    TextView register;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_log_in);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopLogIn.this,ShopRegister.class);
                startActivity(intent);            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = userName.getText().toString();
                String pass = password.getText().toString();
                if (uName.isEmpty() || pass.isEmpty()){
                    if(uName.isEmpty()){
                        userName.setError("Enter Email");
                    }
                    if(pass.isEmpty()){
                        password.setError("Enter Password");
                    }
                }else{
                    progressDialog.show();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference(uName).child("Profile");
                    ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String getPassword = dataSnapshot.child("Password").getValue(String.class);
                                String getPermission = dataSnapshot.child("Permission").getValue(String.class);
                                String userType = dataSnapshot.child("User Type").getValue(String.class);
                                String userName = dataSnapshot.child("User Name").getValue(String.class);
                                if(userType.equals("ShopKeeper")){
                                    if(pass.equals(getPassword)){
                                        if(getPermission.equals("true")){
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(ShopLogIn.this, ShopkeeperDashboard.class);
                                            intent.putExtra("key",userName);
                                            startActivity(intent);
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(ShopLogIn.this,"Admin not Allow you set",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(ShopLogIn.this,"Wrong Password",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(ShopLogIn.this,"InValid Information",Toast.LENGTH_LONG).show();
                                }

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(ShopLogIn.this,"User Does Not exisit",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ShopLogIn.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }




    void init(){
        userName = findViewById(R.id.shop_userName);
        password = findViewById(R.id.shop_password);
        login = findViewById(R.id.shop_login);
        register = findViewById(R.id.shop_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}