package com.example.android.weatherapp.interfaces;

import com.example.android.weatherapp.model.ForecastRequest;
import com.example.android.weatherapp.model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCountry,
                                     @Query("appid") String keyApi,
                                     @Query("units") String unit);

    @GET("data/2.5/forecast")
    Call<ForecastRequest> loadForecast(@Query("q") String cityCountry,
                                       @Query("appid") String keyApi,
                                       @Query("units") String unit);

}
