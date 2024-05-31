package com.example.vegeta_box;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProducts extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView productImage;
    EditText productName, productPrice, productDetail;
    Button btn;
    LinearLayout addImageButton;
    ProgressDialog progressDialog;
    Uri uri;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        init();
        key = getIntent().getStringExtra("key");

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                String price = productPrice.getText().toString();
                String detail = productDetail.getText().toString();
                if(uri == null || name.isEmpty() || price.isEmpty() || detail.isEmpty()){
                    if(uri == null){
                        Toast.makeText(AddProducts.this,"Add Images",Toast.LENGTH_LONG).show();
                    }
                    if(name.isEmpty()){
                        Toast.makeText(AddProducts.this,"Add Product Name",Toast.LENGTH_LONG).show();
                    }
                    if(price.isEmpty()){
                        Toast.makeText(AddProducts.this,"Add Product Price",Toast.LENGTH_LONG).show();
                    }
                    if(detail.isEmpty()){
                        Toast.makeText(AddProducts.this,"Add Product Detail",Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.show();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    String imageName = UUID.randomUUID().toString();
                    StorageReference imageRef = storageRef.child("images/" + imageName);
                    imageRef.putFile(uri)
                            .addOnSuccessListener(taskSnapshot -> {

                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    Map<String , Object> data = new HashMap<>();
                                    data.put("Product image",imageUrl);
                                    data.put("Product Name",name);
                                    data.put("Product Prict",price);
                                    data.put("Product Detail",detail);
                                    String randomKey = UUID.randomUUID().toString();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(key).child("Products").child(randomKey);
                                    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddProducts.this, "Product Added", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddProducts.this,ShopkeeperDashboard.class);
                                            intent.putExtra("key",key);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddProducts.this, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddProducts.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            });
                }
                // Perform further actions here
            }
        });
    }


    private void init() {
        productImage = findViewById(R.id.shopkeeper_add_products_image);
        productName = findViewById(R.id.shopkeeper_add_products_name);
        productPrice = findViewById(R.id.shopkeeper_add_products_price);
        productDetail = findViewById(R.id.shopkeeper_add_products_detial);
        btn = findViewById(R.id.shopkeeper_add_products_btn);
        addImageButton = findViewById(R.id.shopkeeper_add_products_add_image);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                productImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
