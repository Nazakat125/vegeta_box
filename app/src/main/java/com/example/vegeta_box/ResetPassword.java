package com.example.vegeta_box;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    EditText password,confirmPassword;
    Button changePassword;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        password = findViewById(R.id.f_password);
        confirmPassword = findViewById(R.id.f_confirmPassword);
        changePassword = findViewById(R.id.f_resetPassword);
        String number  = getIntent().getStringExtra("number");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String coPass = confirmPassword.getText().toString();
                if(pass.isEmpty() && coPass.isEmpty()){
                    if(pass.isEmpty()){
                        password.setError("Enter Password");
                    }
                    if(coPass.isEmpty()){
                        confirmPassword.setError("Enter Confirm Password");
                    }
                }else{
                    if(pass.length() >= 8 && coPass.length() >= 8){
                        if(pass.equals(coPass)){
                            progressDialog.show();
                            Map<String , Object> data = new HashMap<>();
                            data.put("Password",pass);
                            data.put("Confirm Password",coPass);
                            databaseRef.child(number).child("Profile").updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ResetPassword.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ResetPassword.this,CustomerLogIn.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ResetPassword.this, "Request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                        }else{
                            Toast.makeText(ResetPassword.this, "Password & Confirm Password must be equal", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(ResetPassword.this, "Password & Confirm must be 8 charcter long", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }
}