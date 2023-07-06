package com.example.restaurantuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.R;
import com.example.restaurantuser.activity.DetailActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;



public class foodListAdapter extends RecyclerView.Adapter<foodListAdapter.ViewHolder> {
    ArrayList<FoodDomain> items;
    Context context;


    public foodListAdapter(Context context, ArrayList<FoodDomain> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.viewholder_food_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDomain product = items.get(position);
        holder.productName.setText(product.getNama());
        holder.productPrice.setText(String.format("Rp %s", product.getHarga()));
        holder.productRating.setText(product.getRating());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ImageRef = storageRef.child("images/"+product.getFoto());
        ImageRef.getDownloadUrl().addOnSuccessListener(url -> {
            Transformation<Bitmap> transformation = new MultiTransformation<>(
                    new CenterCrop(),
                    new GranularRoundedCorners(35 ,35 , 0 , 0)
            );
            RequestOptions requestOptions = new RequestOptions()
                    .transform(transformation);

            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(holder.productImage);
        }).addOnFailureListener(exception -> {
            // ...
        });
        holder.itemView.setOnClickListener(view -> {
            Intent detailIntent = new Intent(context, DetailActivity.class);
            detailIntent.putExtra("object", product); // Add the object as an extra
            context.startActivity(detailIntent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.picFood);
            productName = itemView.findViewById(R.id.textTitleFood);
            productPrice = itemView.findViewById(R.id.textPrice);
            productRating = itemView.findViewById(R.id.textScore);

        }
    }
}