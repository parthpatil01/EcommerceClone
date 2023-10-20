package com.example.ecommerceclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceclone.Model.AddedProducts;
import com.example.ecommerceclone.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {

    private String ProductId, uri;
    private ImageView productImage, cartIcon;
    private TextView productName, productDes, productPrice, addToCart, cartCounter;
    private TextView sizeS, sizeM, sizeL, sizeXL;
    private String flag, selectedSize;
    private int allowToAdd = 1; //allow 1, not allow 0, go to cart -1
    private FirebaseFirestore firestore ;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        init();

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();

        sizeS.setOnClickListener(this);
        sizeM.setOnClickListener(this);
        sizeL.setOnClickListener(this);
        sizeXL.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        cartIcon.setOnClickListener(this);

        checkCart();

        ProductId = getIntent().getStringExtra("pid");
        uri = getIntent().getStringExtra("url");

        getProductInfo();


    }

    void init(){
        productImage = findViewById(R.id.pro_des_image);
        productName = findViewById(R.id.pro_des_pro_name);
        productPrice = findViewById(R.id.pro_des_pro_price);
        productDes = findViewById(R.id.pro_des_pro_des);
        addToCart = findViewById(R.id.pro_des_addtocart);
        cartCounter = findViewById(R.id.cartCounter);
        cartIcon = findViewById(R.id.pro_des_cart);


        sizeS = findViewById(R.id.s);
        sizeM = findViewById(R.id.m);
        sizeL = findViewById(R.id.l);
        sizeXL = findViewById(R.id.xl);
    }

    private void getProductInfo() {

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("PRODUCTS").document(ProductId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Products products = documentSnapshot.toObject(Products.class);
                Picasso.get().load(products.getImage()).fit().centerCrop().into(productImage);
                productName.setText(products.getName());
                productDes.setText(products.getDescription());
                productPrice.setText("₹" + products.getPrice());
            }
        });

    }


    public void backButton(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.s:
                setColor("s");
                selectedSize = "s";
                break;

            case R.id.m:
                setColor("m");
                selectedSize = "m";
                break;

            case R.id.l:
                setColor("l");
                selectedSize = "l";
                break;

            case R.id.xl:
                setColor("xl");
                selectedSize = "xl";
                break;

            case R.id.pro_des_addtocart:


                if (selectedSize != null && allowToAdd == 1) {

                    allowToAdd = 0;
                    addToCart(selectedSize);

                }
                else if (allowToAdd == -1) opencart();

                else Toast.makeText(this, "Select a size", Toast.LENGTH_SHORT).show();

                break;

            case R.id.pro_des_cart:
                opencart();
                break;
        }
    }

    private void opencart() {
        Intent intent = new Intent(ProductDescription.this, Homepage.class);
        intent.putExtra("opencart", "opencart");
        startActivity(intent);
        finish();
    }

    public void addToCart(String selectedSize) {

        String newProductId = ProductId + selectedSize;

        firestore.collection("USERS").document(firebaseAuth.getUid()).collection("CART").document(newProductId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    long quantity = (long)documentSnapshot.get("quantity")+ 1;

                    final HashMap<String, Object> product = new HashMap<>();
                    product.put("quantity", quantity);

                    firestore.collection("USERS").document(firebaseAuth.getUid()).collection("CART").document(newProductId).set(product, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            cartCounter.setText(String.valueOf(Integer.parseInt(cartCounter.getText().toString())+1));
                            addToCart.setText("Go to cart!");
                            allowToAdd = -1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(ProductDescription.this, "Unable to add", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{

                    String currentTime, currentDate;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
                    currentDate = simpleDateFormat.format(calendar.getTime());

                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss a");
                    currentTime = simpleDateFormat1.format(calendar.getTime());

                    final HashMap<String, Object> product = new HashMap<>();
                    product.put("pid", ProductId);
                    product.put("productName", productName.getText().toString());
                    product.put("productDes", productDes.getText().toString());
                    product.put("productPrice", productPrice.getText().toString());
                    product.put("productSize", selectedSize);
                    product.put("time", currentTime);
                    product.put("date", currentDate);
                    product.put("discount", "");
                    product.put("quantity", 1);
                    product.put("url", uri);
                    product.put("sizepid", newProductId);

                    firestore.collection("USERS").document(firebaseAuth.getUid()).collection("CART").document(newProductId).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            addToCart.setText("Go to cart!");
                            cartCounter.setText(String.valueOf(Integer.parseInt(cartCounter.getText().toString())+1));
                            allowToAdd = -1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(ProductDescription.this, "Unable to add", Toast.LENGTH_SHORT).show();
                        }
                    });

                }



            }
        });

    }


    public void checkCart() {

        firestore.collection("USERS").document(firebaseAuth.getUid()).collection("CART").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String str=String.valueOf(queryDocumentSnapshots.size());
                cartCounter.setText(str);
            }
        });

    }


    private void setColor(String value) {

        switch (value) {
            case "s":
                defaultColor();
                sizeS.setTextColor(getResources().getColor(R.color.green));
                sizeS.setBackgroundResource(R.drawable.green_border);
                flag = "s";
                break;
            case "m":
                defaultColor();
                sizeM.setTextColor(getResources().getColor(R.color.green));
                sizeM.setBackgroundResource(R.drawable.green_border);
                flag = "m";
                break;

            case "l":
                defaultColor();
                sizeL.setTextColor(getResources().getColor(R.color.green));
                sizeL.setBackgroundResource(R.drawable.green_border);
                flag = "l";
                break;

            case "xl":
                defaultColor();
                sizeXL.setTextColor(getResources().getColor(R.color.green));
                sizeXL.setBackgroundResource(R.drawable.green_border);
                flag = "xl";
                break;

        }


    }

    private void defaultColor() {
        if (flag != null) {
            switch (flag) {
                case "s":
                    sizeS.setTextColor(getResources().getColor(R.color.black));
                    sizeS.setBackgroundResource(R.drawable.round_border);

                    break;
                case "m":
                    sizeM.setTextColor(getResources().getColor(R.color.black));
                    sizeM.setBackgroundResource(R.drawable.round_border);

                    break;

                case "l":
                    sizeL.setTextColor(getResources().getColor(R.color.black));
                    sizeL.setBackgroundResource(R.drawable.round_border);

                    break;

                case "xl":
                    sizeXL.setTextColor(getResources().getColor(R.color.black));
                    sizeXL.setBackgroundResource(R.drawable.round_border);

                    break;

            }
        }
    }
}