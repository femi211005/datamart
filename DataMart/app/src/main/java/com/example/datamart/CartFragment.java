package com.example.datamart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datamart.adapter.ProductAdapter;
import com.example.datamart.db.DatabaseHelper;
import com.example.datamart.model.Product;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private TextView tvEmptyCart;
    private DatabaseHelper dbHelper;
    private ProductAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCart = view.findViewById(R.id.rvCart);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);

        // Kita gunakan bentuk kotak-kotak (Grid) seperti di halaman utama
        rvCart.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Panggil database SQLite
        dbHelper = new DatabaseHelper(getContext());

        // Ambil semua barang dari dalam database
        loadCartData();

        return view;
    }

    private void loadCartData() {
        List<Product> cartItems = dbHelper.getAllCartItems();

        if (cartItems.isEmpty()) {
            // Jika keranjang kosong, tampilkan teks pemberitahuan
            tvEmptyCart.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        } else {
            // Jika ada isinya, masukkan ke adapter untuk ditampilkan
            tvEmptyCart.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);

            cartAdapter = new ProductAdapter(cartItems);
            rvCart.setAdapter(cartAdapter);
        }
    }
}