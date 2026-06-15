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

    private TextView tvCourierStatus, tvPaymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);
        ImageButton btnBack = findViewById(R.id.btnBackCheckout);
        MaterialButton btnPayNow = findViewById(R.id.btnPayNow);
        TextView tvCheckoutName = findViewById(R.id.tvCheckoutName);

        ImageView ivCheckoutProduct = findViewById(R.id.ivCheckoutProduct);
        TextView tvCheckoutProductName = findViewById(R.id.tvCheckoutProductName);
        TextView tvCheckoutProductPrice = findViewById(R.id.tvCheckoutProductPrice);
        TextView tvCheckoutTotalPrice = findViewById(R.id.tvCheckoutTotalPrice);

        SharedPreferences userPrefs = getSharedPreferences("AkunApp", Context.MODE_PRIVATE);
        String savedName = userPrefs.getString("nama", "Pengguna");
        if (tvCheckoutName != null) {
            tvCheckoutName.setText(savedName);
        }

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
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> {
                String[] couriers = {"Regular Shipping", "Express Shipping", "Instant Delivery"};
                new AlertDialog.Builder(CheckoutActivity.this)
                        .setTitle("Pilih Metode Pengiriman (Kurir)")
                        .setItems(couriers, (dialog, which) -> {
                            selectedCourier = couriers[which];
                            Toast.makeText(CheckoutActivity.this, "Kurir Terpilih: " + selectedCourier, Toast.LENGTH_SHORT).show();
                            String[] payments = {"Transfer Bank VA", "Kartu Kredit / Debit", "Dompet Digital (E-Wallet)"};
                            new AlertDialog.Builder(CheckoutActivity.this)
                                    .setTitle("Pilih Metode Pembayaran")
                                    .setItems(payments, (dialogPage, whichPayment) -> {
                                        selectedPayment = payments[whichPayment];
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
    private void prosesTransaksiFinal() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_CART, null, null);
        } catch (Exception e) {
            Log.e("SQL_ERROR", "Gagal mengosongkan keranjang: " + e.getMessage());
        }

        Toast.makeText(this, "Transaksi Berhasil Menggunakan " + selectedPayment, Toast.LENGTH_LONG).show();

        Intent successIntent = new Intent(CheckoutActivity.this, SuccessActivity.class);
        startActivity(successIntent);
        finish();
    }
}