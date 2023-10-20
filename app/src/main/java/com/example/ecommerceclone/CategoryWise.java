package com.example.ecommerceclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerceclone.Model.Products;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class CategoryWise extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        recyclerView=findViewById(R.id.category_wise_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);

        Query query= FirebaseFirestore.getInstance()
                .collection("PRODUCTS").whereEqualTo("category",category);


        FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class)
                .build();


        FirestoreRecyclerAdapter<Products, ImageViewHolder> adapter = new FirestoreRecyclerAdapter<Products, ImageViewHolder>(options) {
            @Override
            public void onBindViewHolder(ImageViewHolder holder, int position, Products model) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(CategoryWise.this, ProductDescription.class);
                        intent.putExtra("pid",model.getPID());
                        intent.putExtra("url",model.getImage());
                        startActivity(intent);
                    }
                });
                holder.productName.setText(model.getName());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("₹"+model.getPrice());

                Picasso.get().load(model.getImage())
                        .centerCrop()
                        .fit()
                        .into(holder.productImage);

            }

            @Override
            public ImageViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Using a custom layout called R.layout.message for each item, we create a new instance of the viewholder
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.homepage_image_item, group, false);

                return new ImageViewHolder(view);
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}