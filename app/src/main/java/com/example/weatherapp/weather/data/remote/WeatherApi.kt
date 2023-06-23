package com.example.weatherapp.weather.data.remote

import com.example.weatherapp.weather.domain.model.Weather
import retrofit2.http.GET
import retrofit2.http.Url

interface WeatherApi {

    @GET
    suspend fun fetchWeather(
        @Url url: String
    ): Weather
}