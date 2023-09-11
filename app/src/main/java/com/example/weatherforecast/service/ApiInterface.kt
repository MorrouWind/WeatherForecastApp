package com.example.weatherforecast.service

import com.example.weatherforecast.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather?")
    fun getCurrentWeatherByCoord(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("appid") appId: String = Utils.ACCESS_KEY,
        @Query("units") units : String = "metric"
    ) : Call<WeatherModel>

    @GET("weather")
    fun getCurrentWeatherByCity(
        @Query("q") city: String,
        @Query("appid") appId: String = Utils.ACCESS_KEY,
        @Query("units") units : String = "metric"
    ) : Call<WeatherModel>


}