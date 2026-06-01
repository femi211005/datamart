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

        // 1. Set teks nama produk dengan validasi aman
        holder.tvProductTitle.setText(product.getProductTitle() != null ? product.getProductTitle() : "Produk Tanpa Nama");

        // 2. Set teks harga produk
        holder.tvProductPrice.setText(product.getProductPrice() != null ? product.getProductPrice() : "Harga tidak tersedia");

        // 3. Set teks rating dan total ulasan (Cek berdasarkan angka 0 karena tipe datanya int)
        if (product.getProductNumOfReviews() == 0) {
            holder.tvProductRating.setText("Belum ada ulasan");
        } else {
            holder.tvProductRating.setText(product.getProductStarRating() + " (" + product.getProductNumOfReviews() + ")");
        }

        // 4. Tampilkan gambar produk dari internet menggunakan Glide
        Glide.with(context)
                .load(product.getProductPhoto())
                .placeholder(android.R.drawable.ic_menu_gallery) // Gambar sementara saat proses muat
                .error(android.R.drawable.ic_menu_report_image) // Gambar jika URL gagal dimuat/error
                .into(holder.ivProductImage);

        // 5. Alur UX: Lompat ke halaman Detail Produk
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            // Mengirim data ke DetailActivity
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
}