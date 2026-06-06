package com.example.datamart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 1. Menghubungkan ID dari XML
        ImageButton btnBack = findViewById(R.id.btnBackCheckout);
        MaterialButton btnPayNow = findViewById(R.id.btnPayNow);
        TextView tvCheckoutName = findViewById(R.id.tvCheckoutName);

        // ID Produk yang baru ditambahkan
        ImageView ivCheckoutProduct = findViewById(R.id.ivCheckoutProduct);
        TextView tvCheckoutProductName = findViewById(R.id.tvCheckoutProductName);
        TextView tvCheckoutProductPrice = findViewById(R.id.tvCheckoutProductPrice);
        TextView tvCheckoutTotalPrice = findViewById(R.id.tvCheckoutTotalPrice); // Total pembayaran di bawah

        // 2. Menarik data nama akun dari Brankas (SharedPreferences)
        SharedPreferences userPrefs = getSharedPreferences("AkunApp", Context.MODE_PRIVATE);
        String savedName = userPrefs.getString("nama", "Pengguna");
        if (tvCheckoutName != null) {
            tvCheckoutName.setText(savedName);
        }

        // 3. MENGAMBIL DATA PRODUK DARI API (Dikirim dari DetailActivity)
        Intent intent = getIntent();
        if (intent != null) {
            String productTitle = intent.getStringExtra("CHECKOUT_TITLE");
            String productPrice = intent.getStringExtra("CHECKOUT_PRICE");
            String productImage = intent.getStringExtra("CHECKOUT_IMAGE");

            // Memasang data asli ke layar Checkout
            if (productTitle != null && tvCheckoutProductName != null) {
                tvCheckoutProductName.setText(productTitle);
            }

            if (productPrice != null) {
                if (tvCheckoutProductPrice != null) tvCheckoutProductPrice.setText(productPrice);
                // Kita samakan total harganya dengan harga produk karena asumsikan ongkir gratis
                if (tvCheckoutTotalPrice != null) tvCheckoutTotalPrice.setText(productPrice);
            }

            if (productImage != null && ivCheckoutProduct != null) {
                Glide.with(this)
                        .load(productImage)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(ivCheckoutProduct);
            }
        }

        // 4. Logika Tombol Kembali
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 5. Logika Tombol Bayar Sekarang
        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> {
                Toast.makeText(this, "Pembayaran Berhasil! Pesanan sedang diproses.", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(CheckoutActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            });
        }
    }
}