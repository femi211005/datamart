package com.example.datamart;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
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

    private RecyclerView rvCategories, rvProducts;
    private ProgressBar progressBar;
    private LinearLayout layoutMain, layoutError;
    private Button btnRefresh;
    private Switch switchTheme;

    private ApiService apiService;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<CategoryItem> categoryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Hubungkan variabel dengan ID di XML
        rvCategories = view.findViewById(R.id.rvCategories);
        rvProducts = view.findViewById(R.id.rvProducts);
        progressBar = view.findViewById(R.id.progressBar);
        layoutMain = view.findViewById(R.id.layoutMain);
        layoutError = view.findViewById(R.id.layoutError);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        switchTheme = view.findViewById(R.id.switchTheme);

        apiService = ApiClient.getClient().create(ApiService.class);

        // 2. Atur bentuk RecyclerView
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // --- FITUR DARK MODE DENGAN SHAREDPREFERENCES ---
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("TemaApp", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        switchTheme.setChecked(isDarkMode);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // 3. Logika Tombol Refresh
        btnRefresh.setOnClickListener(v -> loadData());

        // 4. Mulai memuat data
        loadData();

        return view;
    }

    private void loadData() {
        if (isNetworkAvailable()) {
            layoutError.setVisibility(View.GONE);
            layoutMain.setVisibility(View.VISIBLE);
            fetchCategories();
        } else {
            layoutMain.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Tidak ada koneksi internet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCategories() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getCategories("US").enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getData();

                    // Menggunakan 2 argumen: Context dan List sesuai dengan CategoryAdapter milikmu
                    categoryAdapter = new CategoryAdapter(getContext(), categoryList);
                    rvCategories.setAdapter(categoryAdapter);

                    if (!categoryList.isEmpty()) {
                        // Memanggil getName() dari model CategoryItem-mu
                        fetchProducts(categoryList.get(0).getName());
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    layoutError.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Gagal terhubung ke Amazon. Cek API Key.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Koneksi terputus.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProducts(String query) {
        progressBar.setVisibility(View.VISIBLE);

        apiService.searchProducts(query, 1, "US", "RELEVANCE").enqueue(new Callback<AmazonResponse>() {
            @Override
            public void onResponse(Call<AmazonResponse> call, Response<AmazonResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                    // Menggunakan 2 argumen: Context dan List sesuai ProductAdapter milikmu
                    productAdapter = new ProductAdapter(getContext(), response.body().getData().getProducts());
                    rvProducts.setAdapter(productAdapter);

                } else {
                    layoutError.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Gagal memuat produk. Cek API Key.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AmazonResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Koneksi terputus saat memuat produk.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}