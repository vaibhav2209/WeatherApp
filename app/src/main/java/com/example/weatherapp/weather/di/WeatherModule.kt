package com.example.weatherapp.weather.di

import com.example.weatherapp.weather.data.remote.WeatherApi
import com.example.weatherapp.weather.data.repository.WeatherRepository
import com.example.weatherapp.weather.domain.repository.IWeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object WeatherModule {

    @Provides
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApi = run {
        retrofit.create(WeatherApi::class.java)
    }

    @Provides
    fun providesWeatherRepository(
        api: WeatherApi
    ) : IWeatherRepository =
        WeatherRepository(api)
}