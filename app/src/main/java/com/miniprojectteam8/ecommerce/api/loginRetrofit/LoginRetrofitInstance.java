package com.miniprojectteam8.ecommerce.api.loginRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRetrofitInstance {

    private final LoginEndPointInterface API;

    public LoginRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginEndPointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofit.create(LoginEndPointInterface.class);
        API = retrofit.create(LoginEndPointInterface.class);
    }

    public LoginEndPointInterface getAPI() {
        return API;
    }
}
