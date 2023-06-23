package com.example.weatherapp.weather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.utils.network.Resource
import com.example.weatherapp.weather.domain.model.Weather
import com.example.weatherapp.weather.domain.repository.IWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo : IWeatherRepository
) : ViewModel() {

    /*Get Weather*/
    private val weather = MutableLiveData<Resource<Weather>>()

    fun getWeather() =
        weatherRepo.getWeather()
            .onStart { weather.postValue(Resource.Loading) }
            .onEach { u -> weather.postValue(Resource.Success(u)) }
            .flowOn(Dispatchers.IO)
            .catch { e -> weather.postValue(Resource.Failure(e as Exception)) }
            .launchIn(viewModelScope)

    fun doObserverGetWeather(): LiveData<Resource<Weather>> =
        weather

}