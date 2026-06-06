package com.example.datamart;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// WAJIB ADA: Baris ini untuk mengenalkan CartAdapter ke Fragment
import com.example.datamart.adapter.CartAdapter;
import com.example.datamart.db.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

public class CartFragment extends Fragment implements CartAdapter.OnCartChangeListener {

    private RecyclerView rvCartItems;
    private TextView tvSubtotal, tvTotalPayment, tvStickyTotal;
    private DatabaseHelper dbHelper;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menghubungkan ke layout fragment_cart.xml Lumina
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // 1. Inisialisasi Komponen UI berdasarkan ID XML yang benar
        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvSubtotal = view.findViewById(R.id.tvSubtotalPrice);
        tvTotalPayment = view.findViewById(R.id.tvTotalPaymentPrice);
        tvStickyTotal = view.findViewById(R.id.tvStickyTotalPrice);

        // Mengambil tombol Checkout yang baru kita beri ID di XML
        MaterialButton btnCheckout = view.findViewById(R.id.btnCheckout);

        dbHelper = new DatabaseHelper(getContext());

        // 2. Setup RecyclerView dan Pasang Adapter dengan 3 Argumen yang Sesuai
        if (rvCartItems != null) {
            rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
            Cursor cursor = dbHelper.getCartItems(); // Menggunakan nama fungsi getCartItems() dari DatabaseHelper
            adapter = new CartAdapter(getContext(), cursor, this);
            rvCartItems.setAdapter(adapter);
        }

        // 3. Hitung total belanjaan pertama kali saat halaman dibuka
        calculateTotal();

        // 4. LOGIKA TOMBOL CHECKOUT
        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> {
                // Jangan biarkan pindah halaman kalau keranjangnya masih kosong
                if (adapter == null || adapter.getItemCount() == 0) {
                    Toast.makeText(getContext(), "Keranjang belanjamu masih kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    // Beri perintah Intent untuk pindah ke CheckoutActivity
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    // Fungsi otomatis berjalan jika ada item yang dihapus dari keranjang belanja
    @Override
    public void onCartChanged() {
        calculateTotal();
    }

    // Logika Hitung Otomatis Total Belanjaan dari SQLite
    private void calculateTotal() {
        if (dbHelper == null) return;

        Cursor cursor = dbHelper.getCartItems();
        long subtotal = 0;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String priceStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));

                // Membersihkan simbol mata uang agar bisa dihitung secara matematis
                String cleanPrice = priceStr.replaceAll("[^0-9]", "");
                if (!cleanPrice.isEmpty()) {
                    subtotal += (Long.parseLong(cleanPrice) * qty);
                }
            }
            cursor.close();
        }

        // Ditambah Ongkir (25rb) dikurang Diskon (50rb) sesuai rincian ringkasan pesanan Lumina
        long totalPayment = subtotal + 25000 - 50000;
        if (totalPayment < 0 || subtotal == 0) totalPayment = 0;

        // Set nilai teks ke layar handphone
        if (tvSubtotal != null) tvSubtotal.setText("Rp " + String.format("%,d", subtotal).replace(',', '.'));
        if (tvTotalPayment != null) tvTotalPayment.setText("Rp " + String.format("%,d", totalPayment).replace(',', '.'));
        if (tvStickyTotal != null) tvStickyTotal.setText("Rp " + String.format("%,d", totalPayment).replace(',', '.'));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Memastikan isi keranjang langsung ter-refresh otomatis saat user kembali ke halaman ini
        if (adapter != null && dbHelper != null) {
            adapter.swapCursor(dbHelper.getCartItems());
            calculateTotal();
        }
    }
}