package com.example.datamart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datamart.api.ApiClient;
import com.example.datamart.api.ApiService;
import com.example.datamart.model.CategoryResponse;
import com.example.datamart.model.CategoryItem;
import com.example.datamart.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private RecyclerView rvCategoryList;
    private CategoryAdapter adapter;
    private List<CategoryItem> categoryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menyambungkan file Java ini dengan desain fragment_category.xml
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        rvCategoryList = view.findViewById(R.id.rvCategoryList);

        // Mengatur tampilan daftar menjadi kotak-kotak (Grid) 2 kolom
        if (rvCategoryList != null) {
            rvCategoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));

            // Inisialisasi Adapter dan pasangkan ke RecyclerView
            adapter = new CategoryAdapter(getContext(), categoryList);
            rvCategoryList.setAdapter(adapter);
        }

        // Panggil fungsi untuk mengambil data API langsung dari internet
        fetchCategoryFromAPI();

        return view;
    }

    private void fetchCategoryFromAPI() {
        Toast.makeText(getContext(), "Memuat Kategori dari API...", Toast.LENGTH_SHORT).show();

        // 1. Memanggil ApiService menggunakan ApiClient
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 2. Mengeksekusi permintaan data kategori (untuk negara "US")
        Call<CategoryResponse> call = apiService.getCategories("US");

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    CategoryResponse categoryResponse = response.body();
                    categoryList.clear();
                    if (categoryResponse.getData() != null) {
                        categoryList.addAll(categoryResponse.getData());
                    }

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    Log.d("API_KATEGORI", "Berhasil menarik data dari Amazon API!");

                } else {
                    Toast.makeText(getContext(), "Gagal memuat kategori: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API_KATEGORI", "Gagal respons: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi API gagal! Periksa internet Anda.", Toast.LENGTH_SHORT).show();
                Log.e("API_KATEGORI", "Error koneksi: " + t.getMessage());
            }
        });
    }
}