package com.miniprojectteam8.ecommerce.api.productRetrofit;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductEndPointInterface {
    // Trailing slash is needed
    String BASE_URL = "https://fakestoreapi.com";

    @GET("/products")
    Call<List<ProductRetrofit>> getAllProducts();
}
