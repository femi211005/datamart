package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class ReviewItem {
    @SerializedName("review_title")
    private String reviewTitle;

    @SerializedName("review_comment")
    private String reviewComment;

    @SerializedName("review_star_rating")
    private String reviewRating;

    @SerializedName("review_author")
    private String reviewAuthor;

    public String getReviewTitle() { return reviewTitle; }
    public String getReviewComment() { return reviewComment; }
    public String getReviewRating() { return reviewRating; }
    public String getReviewAuthor() { return reviewAuthor; }
}