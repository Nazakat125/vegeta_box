package com.example.vegeta_box;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLogIn extends AppCompatActivity {
    EditText userName, password;
    Button login;
    TextView register;
    ProgressDialog progressDialog;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_log_in);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogIn.this, CustomerRegistration.class);
                startActivity(intent);
            }
        });

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerLogIn.this, ForgotPassword.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = userName.getText().toString();
                String pass = password.getText().toString();
                if (uName.isEmpty() || pass.isEmpty()) {
                    if (uName.isEmpty()) {
                        userName.setError("Enter Email");
                    }
                    if (pass.isEmpty()) {
                        password.setError("Enter Password");
                    }
                } else {
                    progressDialog.show();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference(uName).child("Profile");
                    ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String getPassword = dataSnapshot.child("Password").getValue(String.class);
                                String getUserType = dataSnapshot.child("User Type").getValue(String.class);
                                if (pass.equals(getPassword) || getUserType.equals("Customer")) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(CustomerLogIn.this, CustomerDashboard.class);
                                    intent.putExtra("key", uName);
                                    startActivity(intent);
                                } else {
                                    if (getUserType.equals("Customer")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(CustomerLogIn.this, "Invalid User", Toast.LENGTH_LONG).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CustomerLogIn.this, "Wrong Password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CustomerLogIn.this, "User Does Not exist", Toast.LENGTH_LONG).show();
                            }
                            // Log step after handling login attempt
                            Log.d("CustomerLogIn", "Login attempt completed");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CustomerLogIn.this, e.toString(), Toast.LENGTH_LONG).show();
                            // Log step after dismissing progress dialog due to failure
                            Log.e("CustomerLogIn", "Error occurred, progress dialog dismissed", e);
                        }
                    });
                }
            }
        });
    }

    void init() {
        userName = findViewById(R.id.customer_userName);
        password = findViewById(R.id.customer_password);
        login = findViewById(R.id.customer_login);
        register = findViewById(R.id.customer_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}
