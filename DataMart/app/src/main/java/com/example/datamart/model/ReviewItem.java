package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class ReviewItem {

    // 1. Kunci asli untuk nama pengulas dari API Amazon
    @SerializedName("review_author")
    private String reviewerName;

    // 2. Kunci asli untuk isi komentar panjang
    @SerializedName("review_comment")
    private String reviewComment;

    // 3. Kunci asli untuk judul ulasan (dipakai jika komentar kosong)
    @SerializedName("review_title")
    private String reviewTitle;

    // 4. Kunci asli untuk rating bintang
    @SerializedName("review_star_rating")
    private String reviewRating;

    // 5. Kunci asli untuk foto profil bule Amazon
    @SerializedName("review_author_avatar")
    private String reviewerImage;

    // ==========================================
    // --- BLOK GETTER (PENGAMBIL DATA NYATA) ---
    // ==========================================

    public String getReviewerName() {
        return (reviewerName != null && !reviewerName.isEmpty()) ? reviewerName : "Pengguna Lumina";
    }

    public String getReviewDescription() {
        // Prioritaskan isi komentar panjang. Jika kosong, tarik judul ulasannya.
        if (reviewComment != null && !reviewComment.isEmpty()) {
            return reviewComment;
        } else if (reviewTitle != null && !reviewTitle.isEmpty()) {
            return reviewTitle;
        }
        // Jika dari Amazon memang kosong melompong (hanya kasih bintang)
        return "Tidak ada deskripsi ulasan.";
    }

    public String getReviewRating() {
        return (reviewRating != null && !reviewRating.isEmpty()) ? reviewRating : "5";
    }

    public String getReviewerImage() {
        return reviewerImage;
    }

    // ==========================================
    // --- BLOK SETTER (WAJIB ADA AGAR TIDAK CRASH) ---
    // ==========================================

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public void setReviewerImage(String reviewerImage) {
        this.reviewerImage = reviewerImage;
    }
}