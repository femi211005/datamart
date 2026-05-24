package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;

public class CategoryItem {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() { return id; }
    public String getName() { return name; }
}