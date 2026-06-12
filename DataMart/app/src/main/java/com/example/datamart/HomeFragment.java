package com.example.datamart;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.datamart.model.Product;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvHomeCategories, rvHomeProducts, rvBestReviews;
    private EditText etSearch;
    private ImageView ivSearchIcon;

    private ApiService apiService;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<CategoryItem> categoryList = new ArrayList<>();

    private boolean isInitialLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvHomeCategories = view.findViewById(R.id.rvHomeCategories);
        rvHomeProducts = view.findViewById(R.id.rvHomeProducts);
        rvBestReviews = view.findViewById(R.id.rvBestReviews);

        etSearch = view.findViewById(R.id.etSearch);
        ivSearchIcon = view.findViewById(R.id.ivSearchIcon);

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

        if (etSearch != null && ivSearchIcon != null) {
            ivSearchIcon.setOnClickListener(v -> executeSearch());

            etSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    executeSearch();
                    return true;
                }
                return false;
            });
        }

        if (getArguments() != null && getArguments().containsKey("SELECTED_CATEGORY")) {
            String titipanKategori = getArguments().getString("SELECTED_CATEGORY");
            isInitialLoad = false;
            if (isNetworkAvailable()) {
                fetchCategoriesOnly();
                fetchProducts(titipanKategori, titipanKategori);
            } else {
                Toast.makeText(getContext(), "Tidak ada koneksi internet!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (isNetworkAvailable()) {
                fetchCategories();
            } else {
                Toast.makeText(getContext(), "Tidak ada koneksi internet!", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void executeSearch() {
        String keyword = etSearch.getText().toString().trim();
        if (!keyword.isEmpty()) {
            Toast.makeText(getContext(), "Mencari: " + keyword, Toast.LENGTH_SHORT).show();
            fetchProducts(keyword, null);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            }
            etSearch.setText("");
            etSearch.clearFocus();
        } else {
            Toast.makeText(getContext(), "Kolom pencarian tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCategories() {
        apiService.getCategories("US").enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getData();

                    if (rvHomeCategories != null) {
                        // FIX PERBAIKAN: categoryName langsung berupa data String murni dari adapter, jangan panggil .getName() lagi
                        categoryAdapter = new CategoryAdapter(getContext(), categoryList, categoryName -> {
                            if (categoryName != null && !categoryName.isEmpty()) {
                                Toast.makeText(getContext(), "Memuat Kategori: " + categoryName, Toast.LENGTH_SHORT).show();
                                fetchProducts(categoryName, categoryName);
                            }
                        });
                        rvHomeCategories.setAdapter(categoryAdapter);
                    }

                    if (isInitialLoad && !categoryList.isEmpty()) {
                        fetchProducts(categoryList.get(0).getName(), null);
                        isInitialLoad = false;
                    }
                } else {
                    Log.e("API_ERROR", "Error Kategori: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Koneksi terputus.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCategoriesOnly() {
        apiService.getCategories("US").enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getData();

                    if (rvHomeCategories != null) {
                        categoryAdapter = new CategoryAdapter(getContext(), categoryList, categoryName -> {
                            if (categoryName != null && !categoryName.isEmpty()) {
                                Toast.makeText(getContext(), "Memuat Kategori: " + categoryName, Toast.LENGTH_SHORT).show();
                                fetchProducts(categoryName, categoryName);
                            }
                        });
                        rvHomeCategories.setAdapter(categoryAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Gagal memuat kategori horizontal.");
            }
        });
    }

    private void fetchProducts(String query, String categoryId) {
        apiService.searchProducts(query, 1, "US", "RELEVANCE", categoryId).enqueue(new Callback<AmazonResponse>() {
            @Override
            public void onResponse(@NonNull Call<AmazonResponse> call, @NonNull Response<AmazonResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Product> products = response.body().getData().getProducts();

                    if (products != null && !products.isEmpty()) {
                        if (rvHomeProducts != null) {
                            productAdapter = new ProductAdapter(getContext(), products);
                            rvHomeProducts.setAdapter(productAdapter);
                        }
                    } else {
                        Toast.makeText(getContext(), "Produk tidak ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Error Produk: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AmazonResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Koneksi pencarian terputus.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.net.Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false;
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }
}