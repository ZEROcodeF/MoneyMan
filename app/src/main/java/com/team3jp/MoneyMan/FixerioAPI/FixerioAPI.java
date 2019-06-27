package com.team3jp.MoneyMan.FixerioAPI;

import com.team3jp.MoneyMan.CurrencyRates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FixerioAPI {
    @GET("latest")
    public Call<CurrencyRates> getFixerio(@Query("access_key") String access_key);
}
