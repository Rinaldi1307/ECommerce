package com.miniprojectteam8.ecommerce.api.productRetrofit;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ProductRetrofit {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("price")
    @Expose
    private float price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("image")
    @Expose
    private String imageUrl;

    @SerializedName("rating")
    @Expose
    private ProductRating productRating;

    @Generated("jsonschema2pojo")
    public class ProductRating {
        @SerializedName("rate")
        @Expose
        private float rate;

        @SerializedName("count")
        @Expose
        private int count;

        public float getRate() {
            return rate;
        }

        public int getCount() {
            return count;
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductRating getProductRating() {
        return productRating;
    }
}
