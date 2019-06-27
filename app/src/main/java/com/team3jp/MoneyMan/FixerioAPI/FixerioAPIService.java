package com.team3jp.MoneyMan.FixerioAPI;

import com.team3jp.MoneyMan.CurrencyRates;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FixerioAPIService {
    public static final String FIXERIO_API_KEY = "bbc1baf70ccde6b4524186ae3b746f41";


    public FixerioAPIService() {
    }

    public CurrencyRates getFixerioRespone() {
        CurrencyRates fixerio = null;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://data.fixer.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        FixerioAPI fixerioAPI = retrofit.create(FixerioAPI.class);
        Call<CurrencyRates> callSync = fixerioAPI.getFixerio(FIXERIO_API_KEY);

        try {
            Response<CurrencyRates> response = callSync.execute();
            fixerio = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fixerio;
    }
}
