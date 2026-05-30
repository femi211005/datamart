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
import com.example.datamart.model.ReviewItem; // Menggunakan file asli milikmu

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context context;
    private final List<ReviewItem> reviewList; // Menggunakan ReviewItem

    public ReviewAdapter(Context context, List<ReviewItem> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem review = reviewList.get(position);

        // Menampilkan data ulasan dari Amazon menggunakan file asli milikmu
        holder.tvReviewerName.setText(review.getReviewerName());
        holder.tvReviewComment.setText(review.getReviewDescription());
        holder.tvReviewRating.setText("Rating: " + review.getReviewRating());

        Glide.with(context)
                .load(review.getReviewerImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivReviewerPhoto);
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReviewerPhoto;
        TextView tvReviewerName, tvReviewComment, tvReviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReviewerPhoto = itemView.findViewById(R.id.ivReviewerPhoto);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewComment = itemView.findViewById(R.id.tvReviewComment);
            tvReviewRating = itemView.findViewById(R.id.tvReviewRating);
        }
    }
}