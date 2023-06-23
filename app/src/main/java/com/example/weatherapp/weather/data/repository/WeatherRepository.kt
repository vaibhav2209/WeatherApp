package com.example.weatherapp.weather.data.repository

import com.example.weatherapp.utils.network.ApiEndPoints
import com.example.weatherapp.weather.data.remote.WeatherApi
import com.example.weatherapp.weather.domain.model.Weather
import com.example.weatherapp.weather.domain.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) : IWeatherRepository {


    /*
    *Assuming that we are passing lat lang as path parameter
    * for simplicity and demo testing Whole url is passed
    * */
    override fun getWeather() : Flow<Weather> =
        flow {
            emit(weatherApi.fetchWeather(ApiEndPoints.GET_WEATHER))
        }.flowOn(Dispatchers.IO)

}