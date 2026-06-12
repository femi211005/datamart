package com.example.datamart.api;

import com.example.datamart.model.AmazonResponse;
import com.example.datamart.model.CategoryResponse;
import com.example.datamart.model.ReviewResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    @Headers({
            "x-rapidapi-host: real-time-amazon-data.p.rapidapi.com",
            "x-rapidapi-key: 9b9540a8d9msh263042dbd29cb17p183777jsn946cd7be086f" // API Key Baru Segar dari Curl Kamu
    })
    @GET("product-category-list")
    Call<CategoryResponse> getCategories(
            @Query("country") String country
    );

    @Headers({
            "x-rapidapi-host: real-time-amazon-data.p.rapidapi.com",
            "x-rapidapi-key: 9b9540a8d9msh263042dbd29cb17p183777jsn946cd7be086f" // API Key Baru Segar dari Curl Kamu
    })
    @GET("search")
    Call<AmazonResponse> searchProducts(
            @Query("query") String query,
            @Query("page") int page,
            @Query("country") String country,
            @Query("sort_by") String sortBy,
            @Query("category_id") String categoryId
    );

    @Headers({
            "x-rapidapi-host: real-time-amazon-data.p.rapidapi.com",
            "x-rapidapi-key: 9b9540a8d9msh263042dbd29cb17p183777jsn946cd7be086f" // API Key Baru Segar dari Curl Kamu
    })
    @GET("top-product-reviews")
    Call<ReviewResponse> getProductReviews(
            @Query("asin") String productId,
            @Query("country") String country,
            @Query("sort_by") String sortBy
    );
}