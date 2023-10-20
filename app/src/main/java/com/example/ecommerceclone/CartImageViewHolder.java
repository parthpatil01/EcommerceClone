package com.example.ecommerceclone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class CartImageViewHolder extends RecyclerView.ViewHolder {

    public TextView productName,productDes,productSize,productQty,productPrice;
    public ImageView productImage,closeButton;

    public CartImageViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        productImage=itemView.findViewById(R.id.cart_image);
        productName=itemView.findViewById(R.id.cart_product_name);
        productDes=itemView.findViewById(R.id.cart_product_description);
        productSize=itemView.findViewById(R.id.cart_size);
        productQty=itemView.findViewById(R.id.cart_qty);
        productPrice=itemView.findViewById(R.id.cart_price);
        closeButton=itemView.findViewById(R.id.remove_from_cart);

    }
}
