package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("asin")
    private String asin;

    @SerializedName("product_title")
    private String productTitle;

    @SerializedName("product_price")
    private String productPrice;

    @SerializedName("product_star_rating")
    private String productStarRating;

    @SerializedName("product_num_of_reviews")
    private int productNumOfReviews;

    @SerializedName("product_photo")
    private String productPhoto;

    // --- GETTER (Wajib sama persis dengan yang dipanggil di ProductAdapter) ---

    public String getAsin() {
        return asin;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductStarRating() {
        return productStarRating;
    }

    public int getProductNumOfReviews() {
        return productNumOfReviews;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    // --- SETTER ---

    public void setAsin(String asin) {
        // Mengantisipasi jika ada data kosong dari API Amazon
        this.asin = asin != null ? asin : "";
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle != null ? productTitle : "Produk Tanpa Nama";
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice != null ? productPrice : "Rp 0";
    }

    public void setProductStarRating(String productStarRating) {
        this.productStarRating = productStarRating;
    }

    public void setProductNumOfReviews(int productNumOfReviews) {
        this.productNumOfReviews = productNumOfReviews;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }
}