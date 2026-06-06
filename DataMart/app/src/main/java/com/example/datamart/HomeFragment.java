package com.example.datamart;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log; // Tambahan untuk sistem pelacak
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvHomeCategories, rvHomeProducts, rvBestReviews;
    private FrameLayout flNotification;
    private MaterialCardView cvSearchBar;

    private ApiService apiService;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<CategoryItem> categoryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvHomeCategories = view.findViewById(R.id.rvHomeCategories);
        rvHomeProducts = view.findViewById(R.id.rvHomeProducts);
        rvBestReviews = view.findViewById(R.id.rvBestReviews);
        flNotification = view.findViewById(R.id.flNotification);
        cvSearchBar = view.findViewById(R.id.cvSearchBar);

        apiService = ApiClient.getClient().create(ApiService.class);

        if (rvHomeCategories != null) {
            rvHomeCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        if (rvHomeProducts != null) {
            rvHomeProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        if (rvBestReviews != null) {
            rvBestReviews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

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

                    if (rvHomeCategories != null) {
                        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
                        rvHomeCategories.setAdapter(categoryAdapter);
                    }

                    if (!categoryList.isEmpty()) {
                        fetchProducts(categoryList.get(0).getName());
                    }
                } else {
                    // PERUBAHAN: Memunculkan KODE ERROR langsung ke layar HP
                    Toast.makeText(getContext(), "Error Kategori: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("LUMINA_ERROR", "Kode Error Kategori dari Amazon: " + response.code());
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

                    if (rvHomeProducts != null) {
                        productAdapter = new ProductAdapter(getContext(), response.body().getData().getProducts());
                        rvHomeProducts.setAdapter(productAdapter);
                    }

                } else {
                    // PERUBAHAN: Memunculkan KODE ERROR langsung ke layar HP
                    Toast.makeText(getContext(), "Error Produk: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("LUMINA_ERROR", "Kode Error Produk dari Amazon: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AmazonResponse> call, Throwable t) {
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