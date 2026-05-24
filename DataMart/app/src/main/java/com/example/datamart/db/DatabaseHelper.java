package com.example.datamart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.datamart.model.Product;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Informasi Database
    private static final String DATABASE_NAME = "datamart_db";
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel dan Kolom
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_ID = "asin"; // ID unik produk dari Amazon
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Perintah SQL untuk membuat tabel keranjang
        String createTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_IMAGE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Fungsi untuk memasukkan barang ke keranjang
    public boolean addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, product.getAsin());
        values.put(COLUMN_TITLE, product.getTitle());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getPhotoUrl());

        // Insert data, jika ID sudah ada maka akan gagal (mencegah barang duplikat)
        long result = db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return result != -1; // Mengembalikan true jika berhasil disimpan
    }

    // Fungsi untuk mengambil semua barang di keranjang
    public List<Product> getAllCartItems() {
        List<Product> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                // Membongkar data dari SQLite kembali menjadi objek Product
                Product product = new Product(
                        cursor.getString(0), // asin
                        cursor.getString(1), // title
                        cursor.getString(2), // price
                        cursor.getString(3)  // image
                );
                cartList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartList;
    }
}