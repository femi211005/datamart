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
import com.example.datamart.model.ReviewItem;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context context;
    private final List<ReviewItem> reviewList;

    public ReviewAdapter(Context context, List<ReviewItem> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menyambungkan langsung dengan file layout item_review.xml milikmu
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem review = reviewList.get(position);

        if (review != null) {
            // Mengambil nama dan komentar asli dari model (dengan fallback dinamis)
            holder.tvReviewerName.setText(review.getReviewerName());
            holder.tvReviewComment.setText(review.getReviewDescription());

            // Set Rating Bintang Secara Dinamis berdasarkan String dari Amazon
            if (review.getReviewRating() != null) {
                String ratingStr = review.getReviewRating();
                if (ratingStr.contains("5")) {
                    holder.tvReviewRating.setText("⭐⭐⭐⭐⭐");
                } else if (ratingStr.contains("4")) {
                    holder.tvReviewRating.setText("⭐⭐⭐⭐");
                } else if (ratingStr.contains("3")) {
                    holder.tvReviewRating.setText("⭐⭐⭐");
                } else if (ratingStr.contains("2")) {
                    holder.tvReviewRating.setText("⭐⭐");
                } else if (ratingStr.contains("1")) {
                    holder.tvReviewRating.setText("⭐");
                } else {
                    // Default fallback: ⭐⭐⭐⭐⭐ (jika API mengembalikan teks tak terduga)
                    holder.tvReviewRating.setText("⭐⭐⭐⭐⭐");
                }
            }

            // Muat Foto Profil Pengulas Menggunakan Glide secara Real-time
            if (review.getReviewerImage() != null && !review.getReviewerImage().isEmpty()) {
                Glide.with(context)
                        .load(review.getReviewerImage())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(holder.ivReviewerPhoto);
            } else {
                holder.ivReviewerPhoto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReviewerPhoto;
        TextView tvReviewerName, tvReviewRating, tvReviewComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inisialisasi ID yang sesuai dengan XML item_review.xml
            ivReviewerPhoto = itemView.findViewById(R.id.ivReviewerPhoto);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewRating = itemView.findViewById(R.id.tvReviewRating);
            tvReviewComment = itemView.findViewById(R.id.tvReviewComment);
        }
    }
}