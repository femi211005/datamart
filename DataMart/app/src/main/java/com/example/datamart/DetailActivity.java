package com.example.datamart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.datamart.R;
import com.example.datamart.adapter.ReviewAdapter;
import com.example.datamart.api.ApiClient;
import com.example.datamart.api.ApiService;
import com.example.datamart.model.ReviewResponse;
import com.example.datamart.model.Product; // Import model produk
import com.example.datamart.db.DatabaseHelper; // Import database SQLite kita

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage;
    private TextView tvDetailTitle, tvDetailPrice;
    private Button btnAddToCart;
    private RecyclerView rvReviews;

    private ReviewAdapter reviewAdapter;
    private ApiService apiService;
    private DatabaseHelper dbHelper; // Variabel Gudang SQLite

    private String productId, title, price, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        rvReviews = findViewById(R.id.rvReviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient().create(ApiService.class);
        // Membuka koneksi ke Database SQLite
        dbHelper = new DatabaseHelper(this);

        productId = getIntent().getStringExtra("EXTRA_ID");
        title = getIntent().getStringExtra("EXTRA_TITLE");
        price = getIntent().getStringExtra("EXTRA_PRICE");
        imageUrl = getIntent().getStringExtra("EXTRA_IMAGE");

        tvDetailTitle.setText(title);
        tvDetailPrice.setText(price);

        Glide.with(this).load(imageUrl).into(ivDetailImage);

        if (productId != null) {
            fetchReviews(productId);
        }

        // --- PENERAPAN BACKGROUND THREAD (Syarat Aslab No. 5) ---
        btnAddToCart.setOnClickListener(v -> {
            // Matikan tombol sementara agar tidak diklik berkali-kali (spam)
            btnAddToCart.setEnabled(false);
            btnAddToCart.setText("Menyimpan...");

            // 1. Buat pekerja untuk Background Thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // 2. Buat penghubung untuk kembali ke Main Thread
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                // Proses penyimpanan data berat ini berjalan di latar belakang
                Product productToSave = new Product(productId, title, price, imageUrl);
                boolean isSuccess = dbHelper.addToCart(productToSave);

                // Kembali ke Main Thread untuk mengupdate layar (UI)
                handler.post(() -> {
                    // Nyalakan tombolnya kembali
                    btnAddToCart.setEnabled(true);
                    btnAddToCart.setText("Tambah ke Keranjang");

                    if (isSuccess) {
                        Toast.makeText(DetailActivity.this, "Berhasil masuk keranjang! 🛒", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Barang ini sudah ada di keranjangmu!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    private void fetchReviews(String asin) {
        apiService.getProductReviews(asin, "US", "TOP_REVIEWS").enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    reviewAdapter = new ReviewAdapter(response.body().getData().getReviews());
                    rvReviews.setAdapter(reviewAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal memuat ulasan produk.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}