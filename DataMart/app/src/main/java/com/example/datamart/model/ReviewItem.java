package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class ReviewItem {

    @SerializedName("review_author")
    private String reviewerName;

    @SerializedName("review_comment")
    private String reviewComment;

    @SerializedName("review_title")
    private String reviewTitle;

    @SerializedName("review_star_rating")
    private String reviewRating;

    @SerializedName("review_author_avatar")
    private String reviewerImage;


    public String getReviewerName() {
        return (reviewerName != null && !reviewerName.isEmpty()) ? reviewerName : "Pengguna Datamart";
    }

    public String getReviewDescription() {
        if (reviewComment != null && !reviewComment.isEmpty()) {
            return reviewComment;
        } else if (reviewTitle != null && !reviewTitle.isEmpty()) {
            return reviewTitle;
        }
        return "Tidak ada deskripsi ulasan.";
    }

    public String getReviewRating() {
        return (reviewRating != null && !reviewRating.isEmpty()) ? reviewRating : "5";
    }

    public String getReviewerImage() {
        return reviewerImage;
    }
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public void setReviewerImage(String reviewerImage) {
        this.reviewerImage = reviewerImage;
    }
}