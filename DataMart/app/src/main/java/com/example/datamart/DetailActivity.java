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
    private ImageButton btnAddToCartSmall, btnBackDetail;
    private MaterialButton btnOrderNow;

    private DatabaseHelper dbHelper;

    // Variabel penampung data produk
    private String productAsin, productTitle, productPrice, productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Menghubungkan dengan layout XML
        setContentView(R.layout.activity_detail_product);

        // 1. Inisialisasi Database dan View
        dbHelper = new DatabaseHelper(this);

        ivProductMain = findViewById(R.id.ivProductMain);
        tvDetailProductName = findViewById(R.id.tvDetailProductName);
        tvProductPrice = findViewById(R.id.tvDetailProductPrice);
        btnAddToCartSmall = findViewById(R.id.btnAddToCartSmall);
        btnOrderNow = findViewById(R.id.btnOrderNow);

        // Mengaitkan tombol kembali yang sudah ada di XML
        btnBackDetail = findViewById(R.id.btnBackDetail);

        // 2. Tangkap Data Intent dari ProductAdapter
        Intent intent = getIntent();
        if (intent != null) {
            productAsin = intent.getStringExtra("PRODUCT_ASIN");
            productTitle = intent.getStringExtra("PRODUCT_TITLE");
            productPrice = intent.getStringExtra("PRODUCT_PRICE");
            productImage = intent.getStringExtra("PRODUCT_IMAGE");

            // 3. Pasang Data ke Tampilan
            if (productTitle != null) {
                tvDetailProductName.setText(productTitle);
            }
            if (tvProductPrice != null && productPrice != null) {
                tvProductPrice.setText(productPrice);
            }

            // Tampilkan gambar asli produk dari Amazon menggunakan Glide
            if (productImage != null) {
                Glide.with(this)
                        .load(productImage)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(ivProductMain);
            }
        }

        // 4. LOGIKA TOMBOL KEMBALI
        if (btnBackDetail != null) {
            btnBackDetail.setOnClickListener(v -> finish());
        }

        // 5. LOGIKA TOMBOL MASUKKAN KERANJANG (SQLite)
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

        // 6. LOGIKA TOMBOL PESAN SEKARANG (Lanjut ke Checkout)
        if (btnOrderNow != null) {
            btnOrderNow.setOnClickListener(v -> {
                Intent checkoutIntent = new Intent(DetailActivity.this, CheckoutActivity.class);
                // Bawa data produk ini agar CheckoutActivity bisa menampilkannya secara dinamis
                checkoutIntent.putExtra("CHECKOUT_TITLE", productTitle);
                checkoutIntent.putExtra("CHECKOUT_PRICE", productPrice);
                checkoutIntent.putExtra("CHECKOUT_IMAGE", productImage);
                startActivity(checkoutIntent);
            });
        }
    }
}