package com.example.restaurantuser.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.restaurantuser.Domain.FoodDomain;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }
    public void insertFood(FoodDomain item){
        ArrayList<FoodDomain> listfood = getListCart();
        boolean existAlready  = false;
        int n= 0 ;
        for ( int y = 0;y< listfood.size();y++){
            if(listfood.get(y).getTitle().equals(item.getTitle())){
                existAlready = true ;
                n = y;
                break;
            }
        }
        if( existAlready){
            listfood.get(n).setNumericCart(item.getNumericCart());
        }else{
            listfood.add(item);
        }
        tinyDB.putListObject("cartList",listfood);
        Toast.makeText(context,"Add To Your Cart",Toast.LENGTH_SHORT).show();
    }
    public ArrayList<FoodDomain>  getListCart(){
        return tinyDB.getListObject("cartList");
    }
}
