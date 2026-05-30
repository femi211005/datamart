package com.example.datamart;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datamart.adapter.CategoryAdapter;
import com.example.datamart.adapter.ProductAdapter;
import com.example.datamart.api.ApiClient;
import com.example.datamart.api.ApiService;
import com.example.datamart.model.AmazonResponse;
import com.example.datamart.model.CategoryItem;
import com.example.datamart.model.CategoryResponse;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    // Hanya menggunakan ID yang benar-benar ADA di desain aslimu
    private RecyclerView rvCategories, rvProducts, rvReviews;
    private ImageButton btnCart, btnNotification;

    private ApiService apiService;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<CategoryItem> categoryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Hubungkan variabel dengan ID persis seperti di XML-mu
        rvCategories = view.findViewById(R.id.rvCategories);
        rvProducts = view.findViewById(R.id.rvProducts);
        rvReviews = view.findViewById(R.id.rvReviews);
        btnCart = view.findViewById(R.id.btnCart);
        btnNotification = view.findViewById(R.id.btnNotification);

        apiService = ApiClient.getClient().create(ApiService.class);

        // 2. Atur bentuk daftar
        if (rvCategories != null) {
            rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        if (rvProducts != null) {
            rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        // 3. Cek internet lalu muat data API
        if (isNetworkAvailable()) {
            fetchCategories();
        } else {
            Toast.makeText(getContext(), "Tidak ada koneksi internet!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchCategories() {
        apiService.getCategories("US").enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getData();

                    if (rvCategories != null) {
                        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
                        rvCategories.setAdapter(categoryAdapter);
                    }

                    if (!categoryList.isEmpty()) {
                        fetchProducts(categoryList.get(0).getName());
                    }
                } else {
                    Toast.makeText(getContext(), "Gagal terhubung ke Amazon. Cek API Key.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi terputus.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProducts(String query) {
        apiService.searchProducts(query, 1, "US", "RELEVANCE").enqueue(new Callback<AmazonResponse>() {
            @Override
            public void onResponse(Call<AmazonResponse> call, Response<AmazonResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                    if (rvProducts != null) {
                        productAdapter = new ProductAdapter(getContext(), response.body().getData().getProducts());
                        rvProducts.setAdapter(productAdapter);
                    }

                } else {
                    Toast.makeText(getContext(), "Gagal memuat produk. Cek API Key.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AmazonResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi terputus saat memuat produk.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi mengecek koneksi internet HP
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}