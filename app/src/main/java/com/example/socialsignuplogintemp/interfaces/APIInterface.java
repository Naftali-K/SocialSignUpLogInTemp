package com.example.socialsignuplogintemp.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * @Author: naftalikomarovski
 * @Date: 2023/07/27
 */
public interface APIInterface {
    @GET("people/me?personFields=genders%2Cbirthdays")
    Call<String> getUserProfile(@Header("Authorization") String accessToken);
}
