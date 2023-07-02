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
            if(listfood.get(y).getNama().equals(item.getNama())){
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
        tinyDB.putListObject("CartList",listfood);
        Toast.makeText(context,"Add To Your Cart",Toast.LENGTH_SHORT).show();
    }


    public ArrayList<FoodDomain>  getListCart(){
        return tinyDB.getListObject("CartList");
    }
    public void minusNumberFood(ArrayList <FoodDomain> listfood,int position, ChangeNumberItemsListener changeNumberItemsListener){
        if (listfood.get(position).getNumericCart()==1){
            listfood.remove(position);
        }else {
            listfood.get(position).setNumericCart(listfood.get(position).getNumericCart()-1);
        }
        tinyDB.putListObject("CartList",listfood);
        changeNumberItemsListener.changed();
    }
    public void plusNumberFood(ArrayList<FoodDomain> listfood,int position ,ChangeNumberItemsListener changeNumberItemsListener){
        listfood.get(position).setNumericCart(listfood.get(position).getNumericCart()+1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }
    public double getTotalFee() {
        ArrayList<FoodDomain> listfood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            int harga = Integer.parseInt(listfood2.get(i).getHarga());
            fee = fee + (harga * listfood2.get(i).getNumericCart());
        }
        return fee;
    }
}


