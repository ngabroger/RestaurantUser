package com.example.restaurantuser.Domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderData {
    private String username;
    private String address;
    private ArrayList<FoodDomain> items;
    private double totalPemesanan;
    private String orderDateTime;


    public OrderData() {
        // Empty constructor needed for Firebase
    }

    public OrderData(String username, String address, ArrayList<FoodDomain> items) {
        this.username = username;
        this.address = address;
        this.items = items;
        this.totalPemesanan = 0;

        // Set orderDateTime in desired format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.orderDateTime = dateFormat.format(new Date());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<FoodDomain> getItems() {
        return items;
    }

    public void setItems(ArrayList<FoodDomain> items) {
        this.items = items;
    }

    public double getTotalPemesanan() {
        return totalPemesanan;
    }

    public void setTotalPemesanan(double totalPemesanan) {
        this.totalPemesanan = totalPemesanan;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }


}
