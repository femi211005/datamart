package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    // Getter untuk mengambil ID Kategori jika nanti dibutuhkan
    public String getId() {
        return id;
    }

    // Getter Utama yang dipanggil di CategoryAdapter (Baris 38)
    public String getName() {
        return name;
    }

    // Setter jika kamu ingin mengubah data secara manual di kode Java
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}