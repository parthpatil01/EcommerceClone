package com.example.ecommerceclone.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.ecommerceclone.CategoryWise;
import com.example.ecommerceclone.R;
import com.example.ecommerceclone.databinding.FragmentCategoriesFragmentBinding;

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private LinearLayout shirts, trousers, sweaters, jackets, dresses, western, sandals, shoes, watches, luggage;

    private FragmentCategoriesFragmentBinding binding;

    public CategoriesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCategoriesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init(root);
        shirts.setOnClickListener(this);
        trousers.setOnClickListener(this);
        sweaters.setOnClickListener(this);
        jackets.setOnClickListener(this);
        dresses.setOnClickListener(this);
        western.setOnClickListener(this);
        sandals.setOnClickListener(this);
        shoes.setOnClickListener(this);
        watches.setOnClickListener(this);
        luggage.setOnClickListener(this);

        return root;
    }

    private void init(View view) {

        shirts = view.findViewById(R.id.shirt_ll);
        trousers = view.findViewById(R.id.trouser_ll);
        sweaters = view.findViewById(R.id.sweater_ll);
        jackets = view.findViewById(R.id.jackets_ll);
        dresses = view.findViewById(R.id.dresses_ll);
        western=view.findViewById(R.id.western_ll);
        sandals = view.findViewById(R.id.sandals_ll);
        shoes = view.findViewById(R.id.shoes_ll);
        watches = view.findViewById(R.id.watches_ll);
        luggage = view.findViewById(R.id.luggages_ll);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.shirt_ll:
                openCategory("shirts");
                break;

            case R.id.trouser_ll:
                openCategory("trousers");

                break;

            case R.id.sweater_ll:
                openCategory("sweaters");
                break;

            case R.id.jackets_ll:
                openCategory("jackets");
                break;

            case R.id.dresses_ll:
                openCategory("dresses");
                break;

            case R.id.western_ll:
                openCategory("western");
                break;

            case R.id.sandals_ll:
                openCategory("sandals");
                break;

            case R.id.shoes_ll:
                openCategory("shoes");
                break;

            case R.id.watches_ll:
                openCategory("watches");
                break;

            case R.id.luggages_ll:
                openCategory("luggages");
                break;

        }
    }

    void openCategory(String category) {
        Intent intent = new Intent(getContext(), CategoryWise.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}