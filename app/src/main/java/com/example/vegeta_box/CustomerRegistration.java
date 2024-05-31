package com.example.vegeta_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegistration extends AppCompatActivity {
    EditText email, name, address, password, confirmPassword,userName;
    Button register;
    TextView login;
    AutoCompleteTextView province, district;
    ProgressDialog progressDialog;
    String selectedProvince;
    String selectedDistrict;
    String token;
    String[] provincesPakistan = {"Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan", "Gilgit-Baltistan", "Azad Kashmir", "Islamabad Capital Territory"};
    String[] punjabDistricts = {"Lahore", "Faisalabad", "Rawalpindi", "Multan", "Gujranwala", "Sialkot", "Bahawalpur", "Gujrat", "Sargodha", "Sheikhupura"};
    String[] sindhDistricts = {"Karachi", "Hyderabad", "Sukkur", "Larkana", "Mirpur Khas", "Nawabshah", "Shikarpur", "Jacobabad", "Dadu", "Khairpur"};
    String[] kpDistricts = {"Peshawar", "Abbottabad", "Mardan", "Swat", "Dera Ismail Khan", "Kohat", "Mansehra", "Nowshera", "Charsadda", "Mingora"};
    String[] balochistanDistricts = {"Quetta", "Gwadar", "Khuzdar", "Turbat", "Sibi", "Zhob", "Loralai", "Chaman", "Mastung", "Kalat"};
    String[] gilgitBaltistanDistricts = {"Gilgit", "Skardu", "Chilas", "Gahkuch", "Danyore", "Ghizer", "Hunza", "Nagar", "Shigar", "Astore"};
    String[] azadKashmirDistricts = {"Muzaffarabad", "Mirpur", "Kotli", "Bhimber", "Rawalakot", "Bagh", "Hattian", "Sudhanoti", "Neelam Valley", "Poonch"};
    String[] islamabadDistricts = {"Islamabad"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        init();
        getAndStoreFCMToken();
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, provincesPakistan);
        province.setAdapter(provinceAdapter);
        province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = (String) parent.getItemAtPosition(position);
                if(selectedProvince.equals("Islamabad Capital Territory")){
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, islamabadDistricts);
                    district.setAdapter(districtAdapter);
                } else if (selectedProvince.equals("Azad Kashmir")) {
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, azadKashmirDistricts);
                    district.setAdapter(districtAdapter);
                } else if (selectedProvince.equals("Gilgit-Baltistan")) {
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, gilgitBaltistanDistricts);
                    district.setAdapter(districtAdapter);
                } else if (selectedProvince.equals("Balochistan")) {
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, balochistanDistricts);
                    district.setAdapter(districtAdapter);
                } else if (selectedProvince.equals("Khyber Pakhtunkhwa")) {
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, kpDistricts);
                    district.setAdapter(districtAdapter);
                } else if(selectedProvince.equals("Sindh")){
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, sindhDistricts);
                    district.setAdapter(districtAdapter);
                } else if (selectedProvince.equals("Punjab")) {
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(CustomerRegistration.this, android.R.layout.simple_dropdown_item_1line, punjabDistricts);
                    district.setAdapter(districtAdapter);
                }

            }
        });
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = (String) parent.getItemAtPosition(position);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegistration.this,CustomerLogIn.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String na = name.getText().toString();
                String add = address.getText().toString();
                String pass = password.getText().toString();
                String passAgain = confirmPassword.getText().toString();
                String UName = userName.getText().toString();

                boolean isValid = true; // Flag to track if all fields are valid

                // Check for empty fields
                if (TextUtils.isEmpty(em)) {
                    email.setError("Enter Email");
                    isValid = false;
                }
                if (TextUtils.isEmpty(UName)) {
                    userName.setError("Enter User Name");
                    isValid = false;
                }
                if (TextUtils.isEmpty(na)) {
                    name.setError("Enter Name");
                    isValid = false;
                }
                if (TextUtils.isEmpty(add)) {
                    address.setError("Enter Address");
                    isValid = false;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Enter Password");
                    isValid = false;
                }
                if (TextUtils.isEmpty(passAgain)) {
                    confirmPassword.setError("Enter Password Again");
                    isValid = false;
                }
                if (selectedProvince == null) {
                    Toast.makeText(CustomerRegistration.this, "Please Select Province", Toast.LENGTH_LONG).show();
                    isValid = false;
                }
                if (selectedDistrict == null) {
                    Toast.makeText(CustomerRegistration.this, "Please Select District", Toast.LENGTH_LONG).show();
                    isValid = false;
                }

                if (isValid) { // Proceed only if all fields are valid
                    if (!pass.equals(passAgain)) {
                        Toast.makeText(CustomerRegistration.this, "Password and Confirm Password have the same value", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.show();
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference ref = db.getReference(UName).child("Profile");
                        Map<String, Object> data = new HashMap<>();
                        data.put("Name", na);
                        data.put("Email", em);
                        data.put("Address", add);
                        data.put("User Name", UName);
                        data.put("Password", pass);
                        data.put("Confirm Password", passAgain);
                        data.put("District", selectedDistrict);
                        data.put("Province", selectedProvince);
                        data.put("User Type", "Customer");
                        data.put("Permission", "false");
                        data.put("FCM Token", token);

                        ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CustomerRegistration.this, "User Already exists with this User Name", Toast.LENGTH_LONG).show();
                                } else {
                                    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(CustomerRegistration.this, ShopLogIn.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(CustomerRegistration.this, e.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(CustomerRegistration.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });



    }




    void init() {
        email = findViewById(R.id.customer_email_r);
        name = findViewById(R.id.customer_name_r);
        address = findViewById(R.id.customer_address_r);
        password = findViewById(R.id.customer_password_r);
        confirmPassword = findViewById(R.id.customer_password_again_r);
        register = findViewById(R.id.customer_register_r);
        login = findViewById(R.id.customer_login_r);
        userName = findViewById(R.id.customer_userName_r);
        province = findViewById(R.id.customer_province);
        district = findViewById(R.id.customer_district);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
    private void getAndStoreFCMToken() {
        // Get the FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        token = task.getResult();
                    } else {
                        // Handle the error
                    }
                });
    }
}
