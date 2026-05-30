package com.example.datamart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datamart.db.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivProductMain;
    private TextView tvDetailProductName, tvProductPrice;
    private ImageButton btnAddToCartSmall;
    private MaterialButton btnOrderNow;

    private DatabaseHelper dbHelper;

    // Variabel penampung data produk
    private String productAsin, productTitle, productPrice, productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        // 1. Inisialisasi Database dan View
        dbHelper = new DatabaseHelper(this);

        ivProductMain = findViewById(R.id.ivProductMain);
        tvDetailProductName = findViewById(R.id.tvDetailProductName);
        tvProductPrice = findViewById(R.id.tvDetailProductPrice);
        btnAddToCartSmall = findViewById(R.id.btnAddToCartSmall);
        btnOrderNow = findViewById(R.id.btnOrderNow);

        // 2. Tangkap Data Intent dari ProductAdapter
        Intent intent = getIntent();
        if (intent != null) {
            productAsin = intent.getStringExtra("PRODUCT_ASIN");
            productTitle = intent.getStringExtra("PRODUCT_TITLE");
            productPrice = intent.getStringExtra("PRODUCT_PRICE");
            productImage = intent.getStringExtra("PRODUCT_IMAGE");

            // 3. Pasang Data ke Tampilan
            tvDetailProductName.setText(productTitle);
            if (tvProductPrice != null) {
                tvProductPrice.setText(productPrice);
            }

            // Tampilkan gambar asli produk dari Amazon menggunakan Glide
            Glide.with(this)
                    .load(productImage)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(ivProductMain);
        }

        // 4. LOGIKA TOMBOL MASUKKAN KERANJANG (SQLite)
        btnAddToCartSmall.setOnClickListener(v -> {
            if (productAsin != null) {
                boolean isSuccess = dbHelper.addToCart(productAsin, productTitle, productPrice, productImage, 1);
                if (isSuccess) {
                    Toast.makeText(DetailActivity.this, "Berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 5. LOGIKA TOMBOL PESAN SEKARANG
        // (CheckoutActivity dimatikan sementara agar tidak error merah)
        btnOrderNow.setOnClickListener(v -> {
            Toast.makeText(DetailActivity.this, "Halaman Checkout sedang dibangun!", Toast.LENGTH_SHORT).show();
        });
    }
}