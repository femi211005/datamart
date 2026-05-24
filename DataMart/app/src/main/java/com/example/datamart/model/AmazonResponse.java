package com.example.datamart.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AmazonResponse {
    @SerializedName("data")
    private DataWrapper data;

    public DataWrapper getData() { return data; }

    public static class DataWrapper {
        @SerializedName("products")
        private List<Product> products;

        public List<Product> getProducts() { return products; }
    }
}