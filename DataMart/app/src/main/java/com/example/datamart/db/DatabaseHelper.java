package com.example.datamart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LuminaCart.db";
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel dan Kolom
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ASIN = "asin";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_QUANTITY = "quantity";

    // Kueri membuat tabel
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ASIN + " TEXT UNIQUE, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_PRICE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // FUNGSI 1: TAMBAH ATAU UPDATE UPDATE JUMLAH BARANG DI KERANJANG
    public boolean addToCart(String asin, String title, String price, String image, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Cek dulu apakah produk ini sudah ada di keranjang
        Cursor cursor = db.query(TABLE_CART, new String[]{COLUMN_QUANTITY}, COLUMN_ASIN + "=?", new String[]{asin}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Jika sudah ada, tinggal tambahkan jumlahnya (quantity)
            int currentQty = cursor.getInt(0);
            values.put(COLUMN_QUANTITY, currentQty + quantity);
            long result = db.update(TABLE_CART, values, COLUMN_ASIN + "=?", new String[]{asin});
            cursor.close();
            return result != -1;
        } else {
            // Jika belum ada, masukkan data baru
            values.put(COLUMN_ASIN, asin);
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_PRICE, price);
            values.put(COLUMN_IMAGE, image);
            values.put(COLUMN_QUANTITY, quantity);
            long result = db.insert(TABLE_CART, null, values);
            if (cursor != null) cursor.close();
            return result != -1;
        }
    }

    // FUNGSI 2: AMBIL SEMUA DATA KERANJANG (Akan dipakai di CartFragment)
    public Cursor getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CART, null);
    }

    // FUNGSI 3: HAPUS BARANG DARI KERANJANG
    public void deleteCartItem(String asin) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ASIN + "=?", new String[]{asin});
    }
}