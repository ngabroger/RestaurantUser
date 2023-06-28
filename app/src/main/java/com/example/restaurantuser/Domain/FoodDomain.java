package com.example.restaurantuser.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {
    private String title;
    private String description;
    private String picUrl;
    private int price;
    private int times;
    private String Category;
    private double score;
    private int numericCart;


    public FoodDomain(String title, String description, String picUrl, int price, int times, String category, double score) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.times = times;
        Category = category;
        this.score = score;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumericCart() {
        return numericCart;
    }

    public void setNumericCart(int numericCart) {
        this.numericCart = numericCart;
    }
}
