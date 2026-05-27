package com.example.datamart.api;

import com.example.datamart.model.AmazonResponse;
import com.example.datamart.model.CategoryResponse;
import com.example.datamart.model.ReviewResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    // 1. Endpoint untuk memunculkan menu Kategori
    @Headers({
            "x-rapidapi-host: real-time-amazon-data.p.rapidapi.com",
            "x-rapidapi-key: 3601ed8feemsh5939c37f03eabbab174aeejsne097138f9e88"
    })
    @GET("product-category-list")
    Call<CategoryResponse> getCategories(
            @Query("country") String country
    );

    // 2. Endpoint Pencarian Produk (Sekarang sudah membawa Kunci!)
    @Headers({
            "x-rapidapi-host: real-time-amazon-data.p.rapidapi.com",
            "x-rapidapi-key: 3601ed8feemsh5939c37f03eabbab174aeejsne097138f9e88"
    })
    @GET("search")
    Call<AmazonResponse> searchProducts(
            @Query("query") String query,
            @Query("page") int page,
            @Query("country") String country,
            @Query("sort_by") String sortBy
    );

    // 3. Endpoint Ulasan Produk Terbaik (Sekarang sudah membawa Kunci!)
    @Headers({
            "x-rapidapi-host: real-time-amazon-data.p.rapidapi.com",
            "x-rapidapi-key: 3601ed8feemsh5939c37f03eabbab174aeejsne097138f9e88"
    })
    @GET("top-product-reviews")
    Call<ReviewResponse> getProductReviews(
            @Query("asin") String productId,
            @Query("country") String country,
            @Query("sort_by") String sortBy
    );
}