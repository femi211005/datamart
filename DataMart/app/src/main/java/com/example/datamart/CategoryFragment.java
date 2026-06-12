package com.example.datamart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datamart.adapter.CategoryAdapter;
import com.example.datamart.adapter.ProductAdapter;
import com.example.datamart.api.ApiClient;
import com.example.datamart.api.ApiService;
import com.example.datamart.model.AmazonResponse;
import com.example.datamart.model.CategoryResponse;
import com.example.datamart.model.CategoryItem;
import com.example.datamart.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private RecyclerView rvCategoryList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private final List<CategoryItem> categoryList = new ArrayList<>();
    private ApiService apiService;

    private boolean isShowingProducts = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        rvCategoryList = view.findViewById(R.id.rvCategoryList);
        apiService = ApiClient.getClient().create(ApiService.class);

        if (rvCategoryList != null) {
            rvCategoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        categoryAdapter = new CategoryAdapter(getContext(), categoryList, categoryName -> {
            if (categoryName != null && !categoryName.isEmpty()) {
                Toast.makeText(getContext(), "Membuka Kategori: " + categoryName, Toast.LENGTH_SHORT).show();
                fetchProductsDirectly(categoryName);
            }
        });

        if (rvCategoryList != null) {
            rvCategoryList.setAdapter(categoryAdapter);
        }

        fetchCategoryFromAPI();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isShowingProducts) {
                    if (rvCategoryList != null) {
                        rvCategoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        rvCategoryList.setAdapter(categoryAdapter);
                    }
                    isShowingProducts = false;
                } else {
                    setEnabled(false);
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        return view;
    }

    private void fetchCategoryFromAPI() {
        apiService.getCategories("US").enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CategoryResponse categoryResponse = response.body();
                    categoryList.clear();
                    if (categoryResponse.getData() != null) {
                        categoryList.addAll(categoryResponse.getData());
                    }
                    if (categoryAdapter != null) {
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Log.e("API_KATEGORI", "Gagal memuat", t);
            }
        });
    }

    private void fetchProductsDirectly(String categoryName) {
        String cleanedCategoryQuery = categoryName;
        if (categoryName.contains("&")) {
            cleanedCategoryQuery = categoryName.split("&")[0].trim();
        }

        final String finalQuery = cleanedCategoryQuery;

        apiService.searchProducts(finalQuery, 1, "US", "RELEVANCE", null).enqueue(new Callback<AmazonResponse>() {
            @Override
            public void onResponse(@NonNull Call<AmazonResponse> call, @NonNull Response<AmazonResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Product> products = response.body().getData().getProducts();

                    if (products != null && !products.isEmpty()) {
                        productAdapter = new ProductAdapter(getContext(), products);
                        if (rvCategoryList != null) {
                            rvCategoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            rvCategoryList.setAdapter(productAdapter);
                        }
                        isShowingProducts = true;
                    } else {
                        Toast.makeText(getContext(), "Produk kosong.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("API_ERROR", "Gagal memuat produk: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AmazonResponse> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "Koneksi bermasalah", t);
            }
        });
    }
}