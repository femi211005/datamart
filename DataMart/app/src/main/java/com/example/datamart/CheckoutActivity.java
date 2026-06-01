package com.example.datamart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 1. Menghubungkan ID dari desain XML aslimu
        ImageButton btnBack = findViewById(R.id.btnBackCheckout);
        MaterialButton btnPayNow = findViewById(R.id.btnPayNow);
        TextView tvCheckoutName = findViewById(R.id.tvCheckoutName);

        // 2. Menarik data nama akun dari Brankas (SharedPreferences)
        SharedPreferences userPrefs = getSharedPreferences("AkunApp", Context.MODE_PRIVATE);
        String savedName = userPrefs.getString("nama", "Geby");
        if (tvCheckoutName != null) {
            tvCheckoutName.setText(savedName);
        }

        // 3. Logika Tombol Kembali
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 4. Logika Tombol Bayar Sekarang
        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> {
                // Munculkan notifikasi sukses
                Toast.makeText(this, "Pembayaran Berhasil! Pesanan sedang diproses.", Toast.LENGTH_LONG).show();

                // Kembali ke Beranda dan bersihkan riwayat halaman sebelumnya
                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}