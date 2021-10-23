package com.miniprojectteam8.ecommerce.api.productRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductRetrofitInstance {
    private final ProductEndPointInterface API;

    public ProductRetrofitInstance(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProductEndPointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofit.create(ProductEndPointInterface.class);
        API = retrofit.create(ProductEndPointInterface.class);
    }

    public ProductEndPointInterface getAPI(){
        return API;
    }


}
