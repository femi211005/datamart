package com.example.datamart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datamart.R;
import com.example.datamart.model.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

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
        CategoryItem item = categoryList.get(position);
        if (item != null) {
            String categoryName = item.getName() != null ? item.getName() : "";
            holder.tvCategoryName.setText(categoryName);

            String iconUrl = getCategoryIconUrl(categoryName);

            Glide.with(context)
                    .load(iconUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivCategoryIcon);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(item.getName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    private String getCategoryIconUrl(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return "https://img.icons8.com/color/96/box.png";
        }

        String nameLower = categoryName.toLowerCase();

        if (nameLower.contains("electronic") || nameLower.contains("computer") || nameLower.contains("pc") || nameLower.contains("tech")) {
            return "https://img.icons8.com/color/96/smartphone.png";
        } else if (nameLower.contains("book") || nameLower.contains("kindle")) {
            return "https://img.icons8.com/color/96/book.png";
        } else if (nameLower.contains("fashion") || nameLower.contains("clothing") || nameLower.contains("apparel") || nameLower.contains("shoe")) {
            return "https://img.icons8.com/color/96/clothes.png";
        } else if (nameLower.contains("home") || nameLower.contains("kitchen") || nameLower.contains("garden")) {
            return "https://img.icons8.com/color/96/home.png";
        } else if (nameLower.contains("toy") || nameLower.contains("baby") || nameLower.contains("kids")) {
            return "https://img.icons8.com/color/96/teddy-bear.png";
        } else if (nameLower.contains("music") || nameLower.contains("movie") || nameLower.contains("art") || nameLower.contains("entertainment")) {
            return "https://img.icons8.com/color/96/clapperboard.png";
        } else if (nameLower.contains("automotive") || nameLower.contains("car") || nameLower.contains("vehicle")) {
            return "https://img.icons8.com/color/96/car.png";
        } else if (nameLower.contains("health") || nameLower.contains("beauty") || nameLower.contains("personal care")) {
            return "https://img.icons8.com/color/96/lipstick.png";
        } else if (nameLower.contains("sport") || nameLower.contains("outdoor")) {
            return "https://img.icons8.com/color/96/dumbbell.png";
        } else if (nameLower.contains("office") || nameLower.contains("school")) {
            return "https://img.icons8.com/color/96/briefcase.png";
        } else if (nameLower.contains("pet") || nameLower.contains("dog") || nameLower.contains("cat")) {
            return "https://img.icons8.com/color/96/dog.png";
        }

        return "https://img.icons8.com/color/96/box.png";
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