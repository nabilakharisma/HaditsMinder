package com.example.finalproject.Api;

import com.example.finalproject.Response.HadistResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY = "93181b98d6mshf93653b16bf5cbcp1da356jsnf7b6c9615a0b";
    String RAPID_API_HOST = "hadiths-api.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("hadiths")
    Call<HadistResponse> getHadist();
}
