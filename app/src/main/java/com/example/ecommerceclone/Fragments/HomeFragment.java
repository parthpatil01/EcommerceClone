package com.example.ecommerceclone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceclone.ImageViewHolder;
import com.example.ecommerceclone.Model.Products;
import com.example.ecommerceclone.ProductDescription;
import com.example.ecommerceclone.R;
import com.example.ecommerceclone.databinding.FragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView=root.findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));


//        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        Query query= FirebaseFirestore.getInstance()
                .collection("PRODUCTS");


        FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class)
                .build();


        FirestoreRecyclerAdapter<Products, ImageViewHolder> adapter = new FirestoreRecyclerAdapter<Products, ImageViewHolder>(options) {
            @Override
            public void onBindViewHolder(ImageViewHolder holder, int position, Products model) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getContext(), ProductDescription.class);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
