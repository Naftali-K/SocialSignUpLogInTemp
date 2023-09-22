package com.example.socialsignuplogintemp.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: naftalikomarovski
 * @Date: 2023/07/27
 */
public class ApiClient {

    private static final String BASE_URL = "https://people.googleapis.com/v1/";

    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
