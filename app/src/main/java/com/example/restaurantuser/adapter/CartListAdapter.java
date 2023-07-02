package com.example.restaurantuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.Helper.ChangeNumberItemsListener;
import com.example.restaurantuser.Helper.ManagementCart;
import com.example.restaurantuser.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    ArrayList<FoodDomain>listFoodSelected;
    private ManagementCart managementCart;
    ChangeNumberItemsListener changeNumberItemsListener;
    private DecimalFormat decimalFormat;

    public CartListAdapter(ArrayList<FoodDomain> listFoodSelected, Context context ,ChangeNumberItemsListener changeNumberItemsListener) {
        this.listFoodSelected = listFoodSelected;
        managementCart= new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
        decimalFormat = new DecimalFormat("#,##0.##");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listFoodSelected.get(position).getNama());
        int price = Integer.parseInt(listFoodSelected.get(position).getHarga());
        String formattedPrice = "Rp." + decimalFormat.format(price);
        holder.feeEachItem.setText(formattedPrice);

        double total = listFoodSelected.get(position).getNumericCart() * price;
        String formattedTotal = "Rp" + decimalFormat.format(total);
        holder.totalEachItem.setText(formattedTotal);

        holder.num.setText(String.valueOf(listFoodSelected.get(position).getNumericCart()));

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(listFoodSelected.get(position).getFoto(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30, 30, 30, 30))
                .into(holder.pic);

    holder.plusItem.setOnClickListener(view -> managementCart.plusNumberFood(listFoodSelected, position, () -> {
        notifyDataSetChanged();
        changeNumberItemsListener.changed();
    }));
        holder.minusItem.setOnClickListener(view -> managementCart.minusNumberFood(listFoodSelected, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }));

    }

    @Override
    public int getItemCount() {
        return listFoodSelected.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView title,feeEachItem,plusItem,minusItem;
        ImageView pic;
        TextView  totalEachItem,num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic =itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            plusItem = itemView.findViewById(R.id.PlusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }
}
