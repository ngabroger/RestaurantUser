package com.example.restaurantuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.R;
import com.example.restaurantuser.activity.DetailActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class foodListAdapter extends RecyclerView.Adapter<foodListAdapter.ViewHolder> {
    ArrayList<FoodDomain> items;
    Context context;
    DatabaseReference databaseReference;

    public foodListAdapter(Context context, ArrayList<FoodDomain> items) {
        this.items = items;
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        Glide.with(context)
                .load(product.getFoto())
                .transform(new GranularRoundedCorners(40, 40, 0, 0))
                .into(holder.productImage);

        holder.itemView.setOnClickListener(view -> {
            Intent detailIntent = new Intent(context, DetailActivity.class);
            detailIntent.putExtra("productId", product.getNama());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.picFood);
            productName = itemView.findViewById(R.id.textTitleFood);
            productPrice = itemView.findViewById(R.id.textPrice);
        }
    }
}