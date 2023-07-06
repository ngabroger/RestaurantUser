package com.example.restaurantuser.Domain;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderData {
    private String username;
    private String address;
    private ArrayList<FoodDomain> items;
    private double totalPemesanan;
    private long orderDateTime;
    private int orderTime;
    private int orderDay;
    private int orderMonth;
    private int orderYear;

    public OrderData() {
        // Empty constructor needed for Firebase
    }

    public OrderData(String username, String address, ArrayList<FoodDomain> items) {
        this.username = username;
        this.address = address;
        this.items = items;
        this.totalPemesanan = 0;
        this.orderDateTime = Calendar.getInstance().getTimeInMillis();
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

    public long getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(long orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(int orderDay) {
        this.orderDay = orderDay;
    }

    public int getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(int orderMonth) {
        this.orderMonth = orderMonth;
    }

    public int getOrderYear() {
        return orderYear;
    }

    public void setOrderYear(int orderYear) {
        this.orderYear = orderYear;
    }
}
