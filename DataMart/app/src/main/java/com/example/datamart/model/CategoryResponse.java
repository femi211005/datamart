package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoryResponse {
    @SerializedName("data")
    private List<CategoryItem> data;

    public List<CategoryItem> getData() { return data; }
}