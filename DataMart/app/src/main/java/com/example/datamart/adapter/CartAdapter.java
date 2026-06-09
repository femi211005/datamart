package com.example.datamart.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datamart.R;
import com.example.datamart.db.DatabaseHelper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private Cursor cursor;
    private DatabaseHelper dbHelper;
    private OnCartChangeListener changeListener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, Cursor cursor, OnCartChangeListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.changeListener = listener;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        // Tarik data dari database lokal (SQLite)
        String asin = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ASIN));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));

        holder.tvCartTitle.setText(title);
        holder.tvCartPrice.setText(price);
        holder.tvCartQty.setText(String.valueOf(quantity));

        Glide.with(context)
                .load(image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivCartProduct);

        holder.btnDeleteCart.setOnClickListener(v -> {
            dbHelper.deleteCartItem(asin);
            swapCursor(dbHelper.getCartItems()); // Refresh tampilan daftar
            if (changeListener != null) changeListener.onCartChanged(); // Update total harga
        });
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCartProduct, btnDeleteCart;
        TextView tvCartTitle, tvCartPrice, tvCartQty;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCartProduct = itemView.findViewById(R.id.ivCartProduct);
            btnDeleteCart = itemView.findViewById(R.id.btnDeleteCart);
            tvCartTitle = itemView.findViewById(R.id.tvCartTitle);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQty = itemView.findViewById(R.id.tvCartQty);
        }
    }
}