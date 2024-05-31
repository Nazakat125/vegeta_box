package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminLogin extends AppCompatActivity {
    EditText adminId,password;
    Button login;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = adminId.getText().toString();
                String pass = password.getText().toString();
                if (uName.isEmpty() || pass.isEmpty()){
                    if(uName.isEmpty()){
                        adminId.setError("Enter Email");
                    }
                    if(pass.isEmpty()){
                        password.setError("Enter Password");
                    }
                }else{
                    progressDialog.show();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference("Admin").child("Profile");
                    ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String getPassword = dataSnapshot.child("Password").getValue(String.class);
                                String getId = dataSnapshot.child("User Id").getValue(String.class);
                                if(pass.equals(getPassword) || uName.equals(getId)){
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(AdminLogin.this,AdminDashboard.class);
                                    startActivity(intent);
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(AdminLogin.this,"Wrong Id and Passord",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(AdminLogin.this,"User Does Not exisit",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminLogin.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    void init(){
        adminId = findViewById(R.id.admin_userName);
        password = findViewById(R.id.admin_password);
        login = findViewById(R.id.admin_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}