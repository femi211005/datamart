package com.example.datamart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datamart.api.ApiClient;
import com.example.datamart.api.ApiService;
import com.example.datamart.db.DatabaseHelper;
import com.example.datamart.model.ReviewResponse;
import com.example.datamart.model.ReviewItem;
import com.example.datamart.adapter.ReviewAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivProductMain;
    private TextView tvDetailProductName, tvProductPrice;
    private ImageButton btnAddToCartSmall, btnBackDetail;
    private MaterialButton btnOrderNow;

    private DatabaseHelper dbHelper;
    private ApiService apiService;
    private RecyclerView rvProductReviews;
    private ReviewAdapter reviewAdapter;

    private String productAsin, productTitle, productPrice, productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        dbHelper = new DatabaseHelper(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        ivProductMain = findViewById(R.id.ivProductMain);
        tvDetailProductName = findViewById(R.id.tvDetailProductName);
        tvProductPrice = findViewById(R.id.tvDetailProductPrice);
        btnAddToCartSmall = findViewById(R.id.btnAddToCartSmall);
        btnOrderNow = findViewById(R.id.btnOrderNow);
        btnBackDetail = findViewById(R.id.btnBackDetail);

        rvProductReviews = findViewById(R.id.rvProductReviews);
        if (rvProductReviews != null) {
            rvProductReviews.setLayoutManager(new LinearLayoutManager(this));
        }

        Intent intent = getIntent();
        if (intent != null) {
            productAsin = intent.getStringExtra("PRODUCT_ASIN");
            productTitle = intent.getStringExtra("PRODUCT_TITLE");
            productPrice = intent.getStringExtra("PRODUCT_PRICE");
            productImage = intent.getStringExtra("PRODUCT_IMAGE");

            if (productTitle != null) {
                tvDetailProductName.setText(productTitle);
            }
            if (tvProductPrice != null && productPrice != null) {
                tvProductPrice.setText(productPrice);
            }

            if (productImage != null) {
                Glide.with(this)
                        .load(productImage)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(ivProductMain);
            }

            // PANGGIL API ULASAN
            if (productAsin != null && !productAsin.isEmpty()) {
                fetchProductReviews(productAsin);
            }
        }

        if (btnBackDetail != null) {
            btnBackDetail.setOnClickListener(v -> finish());
        }

        if (btnAddToCartSmall != null) {
            btnAddToCartSmall.setOnClickListener(v -> {
                if (productAsin != null) {
                    boolean isSuccess = dbHelper.addToCart(productAsin, productTitle, productPrice, productImage, 1);
                    if (isSuccess) {
                        Toast.makeText(DetailActivity.this, "Berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Data produk belum siap", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnOrderNow != null) {
            btnOrderNow.setOnClickListener(v -> {
                Intent checkoutIntent = new Intent(DetailActivity.this, CheckoutActivity.class);
                checkoutIntent.putExtra("CHECKOUT_TITLE", productTitle);
                checkoutIntent.putExtra("CHECKOUT_PRICE", productPrice);
                checkoutIntent.putExtra("CHECKOUT_IMAGE", productImage);
                startActivity(checkoutIntent);
            });
        }
    }

    // Fungsi mengambil ulasan asli dari endpoint Amazon
    private void fetchProductReviews(String asin) {
        // PERBAIKAN: Gunakan "TOP_REVIEWS" agar server Amazon mengerti permintaannya
        apiService.getProductReviews(asin, "US", "TOP_REVIEWS").enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ReviewItem> reviews = response.body().getData().getReviews();

                    if (reviews != null && !reviews.isEmpty()) {
                        // Ulasan ada isinya, munculkan ke layar
                        if (rvProductReviews != null) {
                            reviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
                            rvProductReviews.setAdapter(reviewAdapter);
                        }
                    } else {
                        // PERBAIKAN: Jika ulasan kosong, beritahu lewat Toast agar layar tidak terkesan nge-bug!
                        Toast.makeText(DetailActivity.this, "Belum ada teks ulasan untuk produk ini dari Amazon.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Gagal memuat ulasan. Status: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this, "Koneksi terputus: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}