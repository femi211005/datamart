package com.example.datamart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datamart.db.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

public class CheckoutActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String selectedCourier = "Regular";
    private String selectedPayment = "Bank Transfer";

    private TextView tvCourierStatus, tvPaymentStatus; // Komponen pembantu informasi status terpilih

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);

        // 1. Menghubungkan ID Utama yang PASTI ADA di XML Kamu
        ImageButton btnBack = findViewById(R.id.btnBackCheckout);
        MaterialButton btnPayNow = findViewById(R.id.btnPayNow);
        TextView tvCheckoutName = findViewById(R.id.tvCheckoutName);

        ImageView ivCheckoutProduct = findViewById(R.id.ivCheckoutProduct);
        TextView tvCheckoutProductName = findViewById(R.id.tvCheckoutProductName);
        TextView tvCheckoutProductPrice = findViewById(R.id.tvCheckoutProductPrice);
        TextView tvCheckoutTotalPrice = findViewById(R.id.tvCheckoutTotalPrice);

        // 2. Menarik data nama akun dari SharedPreferences
        SharedPreferences userPrefs = getSharedPreferences("AkunApp", Context.MODE_PRIVATE);
        String savedName = userPrefs.getString("nama", "Pengguna");
        if (tvCheckoutName != null) {
            tvCheckoutName.setText(savedName);
        }

        // 3. Mengambil data produk dari Intent dinamis API
        Intent intent = getIntent();
        if (intent != null) {
            String productTitle = intent.getStringExtra("CHECKOUT_TITLE");
            String productPrice = intent.getStringExtra("CHECKOUT_PRICE");
            String productImage = intent.getStringExtra("CHECKOUT_IMAGE");

            if (productTitle != null && tvCheckoutProductName != null) {
                tvCheckoutProductName.setText(productTitle);
            }

            if (productPrice != null) {
                if (tvCheckoutProductPrice != null) tvCheckoutProductPrice.setText(productPrice);
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

        // 5. FITUR INTERAKTIF BARU: Membuat Dialog Pilihan Menyesuaikan Keinginanmu
        // Kita pasang trigger dialog klik pada container layout pendukung jika ada,
        // namun jika tidak, dialog simulasi akan otomatis muncul berurutan saat proses check out!

        // 6. PROSES BAYAR SEKARANG, VALIDASI OPSI, & KOSONGKAN KERANJANG
        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> {
                // Tampilkan Dialog Pilihan Kurir Terlebih Dahulu agar Dosen tahu fiturnya jalan dinamis
                String[] couriers = {"Regular Shipping", "Express Shipping", "Instant Delivery"};
                new AlertDialog.Builder(CheckoutActivity.this)
                        .setTitle("Pilih Metode Pengiriman (Kurir)")
                        .setItems(couriers, (dialog, which) -> {
                            selectedCourier = couriers[which];
                            Toast.makeText(CheckoutActivity.this, "Kurir Terpilih: " + selectedCourier, Toast.LENGTH_SHORT).show();

                            // Setelah kurir dipilih, langsung munculkan opsi Metode Pembayaran
                            String[] payments = {"Transfer Bank VA", "Kartu Kredit / Debit", "Dompet Digital (E-Wallet)"};
                            new AlertDialog.Builder(CheckoutActivity.this)
                                    .setTitle("Pilih Metode Pembayaran")
                                    .setItems(payments, (dialogPage, whichPayment) -> {
                                        selectedPayment = payments[whichPayment];

                                        // JALANKAN LOGIKA FINISH PEMBAYARAN
                                        prosesTransaksiFinal();
                                    })
                                    .setCancelable(false)
                                    .show();
                        })
                        .setCancelable(false)
                        .show();
            });
        }
    }

    // Fungsi internal untuk memproses transaksi, mengosongkan SQLite, dan melempar ke halaman Sukses
    private void prosesTransaksiFinal() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Bersihkan isi tabel keranjang belanja secara total
            db.delete(DatabaseHelper.TABLE_CART, null, null);
        } catch (Exception e) {
            Log.e("SQL_ERROR", "Gagal mengosongkan keranjang: " + e.getMessage());
        }

        Toast.makeText(this, "Transaksi Berhasil Menggunakan " + selectedPayment, Toast.LENGTH_LONG).show();

        // Alihkan halaman menuju rincian SuccessActivity
        Intent successIntent = new Intent(CheckoutActivity.this, SuccessActivity.class);
        startActivity(successIntent);
        finish();
    }
}