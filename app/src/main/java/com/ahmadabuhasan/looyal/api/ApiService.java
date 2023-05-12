package com.ahmadabuhasan.looyal.api;

import com.ahmadabuhasan.looyal.model.City;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("city")
    Call<City> listCity();
}
