package com.example.vegeta_box;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CustomerDashboard extends AppCompatActivity {
    ViewFlipper viewFlipper;
    CardView createGroup,joinGroup;
    String key;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        init();
        key = getIntent().getStringExtra("key");
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateGroupDialog();
            }
        });
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference(key).child("Group");
                ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Intent intent = new Intent(CustomerDashboard.this, CustomerAllShopsKeeprsList.class);
                            intent.putExtra("key",key);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(CustomerDashboard.this, CustomerGroups.class);
                            intent.putExtra("key",key);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    void init () {
        viewFlipper = findViewById(R.id.customer_flipper);
        createGroup = findViewById(R.id.customer_create_group);
        joinGroup = findViewById(R.id.customer_join_group);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

    }

    void showCreateGroupDialog() {
        Dialog createGroupDialog = new Dialog(CustomerDashboard.this);
        createGroupDialog.setContentView(R.layout.create_group_dialog);

        EditText groupNameEditText = createGroupDialog.findViewById(R.id.customer_group_name);
        EditText groupArea = createGroupDialog.findViewById(R.id.customer_group_area);
        Button createGroupButton = createGroupDialog.findViewById(R.id.customer_group_btn);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String groupName = groupNameEditText.getText().toString();
                String area = groupArea.getText().toString();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("Groups").child(groupName);
                if(groupName.isEmpty() || area.isEmpty()){
                    if (groupName.isEmpty()){
                        groupNameEditText.setError("Enter Gruop Name");
                    }
                    if (area.isEmpty()){
                        groupArea.setError("Enter Gruop Area");
                    }
                }else{
                    ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                progressDialog.dismiss();
                                Toast.makeText(CustomerDashboard.this,"Already Group Exsist With that Namne",Toast.LENGTH_LONG).show();
                            }else{
                                DatabaseReference ref2 = db.getReference("Groups").child(groupName).child("Group Info");
                                Map<String , Object> data = new HashMap<>();
                                data.put("Name",groupName);
                                data.put("Area",area);
                                ref2.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        createGroupDialog.dismiss();
                                        progressDialog.dismiss();
                                        Toast.makeText(CustomerDashboard.this,"Group Created",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });

        // Show the dialog
        createGroupDialog.show();
    }
}
