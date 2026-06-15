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

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvSubtotal = view.findViewById(R.id.tvSubtotalPrice);
        tvTotalPayment = view.findViewById(R.id.tvTotalPaymentPrice);
        tvStickyTotal = view.findViewById(R.id.tvStickyTotalPrice);

        MaterialButton btnCheckout = view.findViewById(R.id.btnCheckout);

        dbHelper = new DatabaseHelper(getContext());

        if (rvCartItems != null) {
            rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
            Cursor cursor = dbHelper.getCartItems();
            adapter = new CartAdapter(getContext(), cursor, this);
            rvCartItems.setAdapter(adapter);
        }

        calculateTotal();

        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> {
                if (adapter == null || adapter.getItemCount() == 0) {
                    Toast.makeText(getContext(), "Keranjang belanjamu masih kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
    }
    @Override
    public void onCartChanged() {
        calculateTotal();
    }
    private void calculateTotal() {
        if (dbHelper == null) return;

        Cursor cursor = dbHelper.getCartItems();
        long subtotal = 0;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String priceStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));

                String cleanPrice = priceStr.replaceAll("[^0-9]", "");
                if (!cleanPrice.isEmpty()) {
                      subtotal += (Long.parseLong(cleanPrice) * qty);
                }
            }
            cursor.close();
        }
        long totalPayment = subtotal + 25000;
        if (totalPayment < 0 || subtotal == 0) totalPayment = 0;

        if (tvSubtotal != null) tvSubtotal.setText("Rp " + String.format("%,d", subtotal).replace(',', '.'));
        if (tvTotalPayment != null) tvTotalPayment.setText("Rp " + String.format("%,d", totalPayment).replace(',', '.'));
        if (tvStickyTotal != null) tvStickyTotal.setText("Rp " + String.format("%,d", totalPayment).replace(',', '.'));
    }
    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && dbHelper != null) {
            adapter.swapCursor(dbHelper.getCartItems());
            calculateTotal();
        }
    }
}