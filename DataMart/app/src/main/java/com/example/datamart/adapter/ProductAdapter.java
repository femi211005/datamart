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
import com.example.datamart.model.Product; // Sesuaikan jika nama kelas modelmu berbeda

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

        // Set teks nama produk
        holder.tvProductTitle.setText(product.getProductTitle());

        // Set teks harga produk (menggunakan data mentah dari API Amazon)
        holder.tvProductPrice.setText(product.getProductPrice());

        // Set teks rating dan total ulasan jika ada
        if (product.getProductStarRating() != null) {
            holder.tvProductRating.setText(product.getProductStarRating() + " (" + product.getProductNumOfReviews() + ")");
        }

        // Tampilkan gambar produk dari internet menggunakan Glide
        Glide.with(context)
                .load(product.getProductPhoto())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivProductImage);

        // Alur UX: Jika kotak produk diklik, lompat ke halaman Detail Produk
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            // Mengirim data produk terpilih ke DetailActivity agar tampilannya dinamis
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