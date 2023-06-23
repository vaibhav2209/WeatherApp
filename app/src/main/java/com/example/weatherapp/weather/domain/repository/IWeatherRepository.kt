package com.example.weatherapp.weather.domain.repository

import com.example.weatherapp.weather.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {

    fun getWeather(): Flow<Weather>
}