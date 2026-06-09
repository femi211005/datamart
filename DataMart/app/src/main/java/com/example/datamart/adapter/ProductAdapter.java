package com.example.datamart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datamart.DetailActivity;
import com.example.datamart.R;
import com.example.datamart.model.CategoryItem;
import com.example.datamart.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductTitle.setText(product.getProductTitle() != null ? product.getProductTitle() : "Produk Tanpa Nama");

        holder.tvProductPrice.setText(product.getProductPrice() != null ? product.getProductPrice() : "Harga tidak tersedia");

        if (product.getProductNumOfReviews() == 0) {
            holder.tvProductRating.setText("Belum ada ulasan");
        } else {
            holder.tvProductRating.setText(product.getProductStarRating() + " (" + product.getProductNumOfReviews() + ")");
        }

        Glide.with(context)
                .load(product.getProductPhoto())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.ivProductImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("PRODUCT_ASIN", product.getAsin());
            intent.putExtra("PRODUCT_TITLE", product.getProductTitle());
            intent.putExtra("PRODUCT_PRICE", product.getProductPrice());
            intent.putExtra("PRODUCT_IMAGE", product.getProductPhoto());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductTitle, tvProductPrice, tvProductRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductRating = itemView.findViewById(R.id.tvProductRating);
        }
    }

    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        private final Context context;
        private final List<CategoryItem> categoryList;
        private final OnCategoryClickListener listener;

        public interface OnCategoryClickListener {
            void onCategoryClick(String categoryName);
        }

        public CategoryAdapter(Context context, List<CategoryItem> categoryList, OnCategoryClickListener listener) {
            this.context = context;
            this.categoryList = categoryList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            CategoryItem category = categoryList.get(position);

            if (category.getName() != null) {
                holder.tvCategoryName.setText(category.getName());
            } else {
                holder.tvCategoryName.setText("Kategori");
            }

            if (category.getName() != null) {
                String namaKategori = category.getName().toLowerCase();

                if (namaKategori.contains("book") || namaKategori.contains("audible") || namaKategori.contains("magazine") || namaKategori.contains("kindle") || namaKategori.contains("educational")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_buku);

                } else if (namaKategori.contains("clothing") || namaKategori.contains("fashion") || namaKategori.contains("men") || namaKategori.contains("women") || namaKategori.contains("girls") || namaKategori.contains("boys") || namaKategori.contains("luggage") || namaKategori.contains("luxury")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_baju);

                } else if (namaKategori.contains("computer") || namaKategori.contains("electronic") || namaKategori.contains("cell phone") || namaKategori.contains("amazon devices") || namaKategori.contains("smart home")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_elektronik);

                } else if (namaKategori.contains("software") || namaKategori.contains("video game") || namaKategori.contains("apps") || namaKategori.contains("alexa") || namaKategori.contains("aws")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_mainan);

                } else if (namaKategori.contains("sport") || namaKategori.contains("outdoor")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_olahraga);

                } else if (namaKategori.contains("home") || namaKategori.contains("kitchen") || namaKategori.contains("garden") || namaKategori.contains("appliance")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_rumah);

                } else if (namaKategori.contains("movie") || namaKategori.contains("tv") || namaKategori.contains("music") || namaKategori.contains("cd") || namaKategori.contains("vinyl") || namaKategori.contains("prime video")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_hiburan);

                } else if (namaKategori.contains("grocery") || namaKategori.contains("food") || namaKategori.contains("fresh")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_makanan);

                } else if (namaKategori.contains("health") || namaKategori.contains("beauty") || namaKategori.contains("pharmacy") || namaKategori.contains("baby") || namaKategori.contains("personal care")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_kesehatan);

                } else if (namaKategori.contains("automotive") || namaKategori.contains("industrial") || namaKategori.contains("tool")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_alat);

                } else if (namaKategori.contains("art") || namaKategori.contains("craft") || namaKategori.contains("sewing") || namaKategori.contains("handmade") || namaKategori.contains("collectible") || namaKategori.contains("instrument")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_seni);

                } else if (namaKategori.contains("card") || namaKategori.contains("payment") || namaKategori.contains("subscribe")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_kartu);

                } else if (namaKategori.contains("pet")) {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_kategori_hiburan);

                } else {
                    holder.ivCategoryIcon.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } else {
                holder.ivCategoryIcon.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null && category.getName() != null) {
                    listener.onCategoryClick(category.getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryList != null ? categoryList.size() : 0;
        }

        public static class CategoryViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCategoryIcon;
            TextView tvCategoryName;

            public CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
                tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            }
        }
    }
}