package com.example.ecommerceclone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceclone.CartImageViewHolder;
import com.example.ecommerceclone.Model.AddedProducts;
import com.example.ecommerceclone.Model.Products;
import com.example.ecommerceclone.Prevalent.Prevalent;
import com.example.ecommerceclone.ProceedToCout;
import com.example.ecommerceclone.R;
import com.example.ecommerceclone.databinding.FragmentCartBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentCartBinding binding;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton floatingActionButton;
    private Button pcot;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        floatingActionButton = getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        recyclerView = root.findViewById(R.id.cartFragmentRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pcot=root.findViewById(R.id.proceed_cout_cart);
        pcot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProceedToCout.class);
                startActivity(intent);
            }
        });

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        Query query=firestore.collection("USERS").document(firebaseAuth.getUid()).collection("CART");

        FirestoreRecyclerOptions<AddedProducts> options = new FirestoreRecyclerOptions.Builder<AddedProducts>()
                .setQuery(query, AddedProducts.class).build();

        FirestoreRecyclerAdapter<AddedProducts, CartImageViewHolder> adapter = new FirestoreRecyclerAdapter<AddedProducts, CartImageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull CartImageViewHolder holder, int position, @NonNull @NotNull AddedProducts model) {

                holder.productName.setText(model.getProductName());
                holder.productDes.setText(model.getProductDes());
                holder.productPrice.setText(model.getProductPrice());
                holder.productSize.setText("Size: " + model.getProductSize());
                String qty = String.valueOf("Qty: " + model.getQuantity());
                holder.productQty.setText(qty);
                holder.closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeProduct(model.getSizepid());
                    }
                });

                Picasso.get()
                        .load(model.getUrl())
                        .placeholder(R.color.light_grey)
                        .fit()
                        .centerCrop()
                        .into(holder.productImage);

            }

            @NonNull
            @NotNull
            @Override
            public CartImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartimageitem, parent, false);
                return new CartImageViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void removeProduct(String pid) {

        firestore.collection("USERS").document(firebaseAuth.getUid()).collection("CART").document(pid).delete()
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "Failed to remove", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        floatingActionButton.setVisibility(View.VISIBLE);
    }
}
