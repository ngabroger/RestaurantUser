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

import java.util.ArrayList;

public class foodListAdapter extends RecyclerView.Adapter<foodListAdapter.ViewHolder> {
    ArrayList<FoodDomain> items;
    Context context;

    public foodListAdapter(ArrayList<FoodDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from((parent.getContext())).inflate(R.layout.viewholder_food_list,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textTitleFood.setText(items.get(position).getTitle());
        holder.textPrice.setText("Rp."+items.get(position).getPrice());
        holder.textScore.setText(""+items.get(position).getScore());
        int drawableResourceID = holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl(),"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).
                load(drawableResourceID)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", items.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
TextView textTitleFood,textPrice,textScore;
ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitleFood = itemView.findViewById(R.id.textTitleFood);
            textPrice = itemView.findViewById(R.id.textPrice);
            textScore= itemView.findViewById(R.id.textScore);
            pic = itemView.findViewById(R.id.picFood);
        }
    }
}
