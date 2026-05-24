package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("asin")
    private String asin;

    @SerializedName("product_title")
    private String title;

    @SerializedName("product_price")
    private String price;

    @SerializedName("product_photo")
    private String photoUrl;

    // Constructor untuk SQLite
    public Product(String asin, String title, String price, String photoUrl) {
        this.asin = asin;
        this.title = title;
        this.price = price;
        this.photoUrl = photoUrl;
    }

    public String getAsin() { return asin; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getPhotoUrl() { return photoUrl; }
}