package com.example.datamart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.datamart.R;
import com.example.datamart.model.ReviewItem;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewItem> reviewList;

    public ReviewAdapter(List<ReviewItem> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewItem review = reviewList.get(position);
        holder.tvAuthor.setText(review.getReviewAuthor());
        holder.tvRating.setText("⭐ " + review.getReviewRating());
        holder.tvTitle.setText(review.getReviewTitle());
        holder.tvComment.setText(review.getReviewComment());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvRating, tvTitle, tvComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            tvRating = itemView.findViewById(R.id.tvReviewRating);
            tvTitle = itemView.findViewById(R.id.tvReviewTitle);
            tvComment = itemView.findViewById(R.id.tvReviewComment);
        }
    }
}