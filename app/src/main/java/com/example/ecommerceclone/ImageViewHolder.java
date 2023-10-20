package com.example.ecommerceclone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView productImage;
    public TextView productName,productPrice,productDescription;

    public ImageViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        productImage=itemView.findViewById(R.id.product_image);
        productName=itemView.findViewById(R.id.product_name);
        productPrice=itemView.findViewById(R.id.product_price);
        productDescription=itemView.findViewById(R.id.product_description);


    }
}
