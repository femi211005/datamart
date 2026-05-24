package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReviewResponse {
    @SerializedName("data")
    private ReviewDataWrapper data;

    public ReviewDataWrapper getData() { return data; }

    public static class ReviewDataWrapper {
        @SerializedName("reviews")
        private List<ReviewItem> reviews;

        public List<ReviewItem> getReviews() { return reviews; }
    }
}