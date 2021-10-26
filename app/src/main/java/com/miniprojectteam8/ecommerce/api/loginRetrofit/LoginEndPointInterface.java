package com.miniprojectteam8.ecommerce.api.loginRetrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginEndPointInterface {
    String BASE_URL = "https://talentpool.oneindonesia.id";

    @FormUrlEncoded
    @Headers({"X-API-KEY: 454041184B0240FBA3AACD15A1F7A8BB"})
    @POST("/api/user/login")
    Call<LoginResponse> getLoginResponse(
            @Field("username") String username,
            @Field("password") String password
    );
}
