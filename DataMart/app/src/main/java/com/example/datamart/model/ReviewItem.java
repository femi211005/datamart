package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class ReviewItem {

    @SerializedName("reviewer_name")
    private String reviewerName;

    @SerializedName("review_description")
    private String reviewDescription;

    @SerializedName("review_rating")
    private String reviewRating;

    @SerializedName("reviewer_image")
    private String reviewerImage;

    // --- GETTER (Disesuaikan agar pas dengan ReviewAdapter baris 39-44) ---

    public String getReviewerName() {
        return reviewerName != null ? reviewerName : "Pengguna Lumina";
    }

    public String getReviewDescription() {
        return reviewDescription != null ? reviewDescription : "Tidak ada deskripsi ulasan.";
    }

    public String getReviewRating() {
        return reviewRating != null ? reviewRating : "5";
    }

    public String getReviewerImage() {
        return reviewerImage;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public void setReviewerImage(String reviewerImage) {
        this.reviewerImage = reviewerImage;
    }
}