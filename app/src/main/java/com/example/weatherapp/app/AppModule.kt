package com.example.weatherapp.app

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.auth.data.local.AuthDao
import com.example.weatherapp.utils.network.ApiEndPoints
import com.example.weatherapp.weather.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        httpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .baseUrl(ApiEndPoints.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun providesAuthDao(
        database: LocalDatabase
    ): AuthDao =
        database.authDao()

    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ): LocalDatabase =
        Room.databaseBuilder(
            context = context,
            klass = LocalDatabase::class.java,
            name = LocalDatabase.LOCAL_DATABASE
        ).build()

    @Provides
    @Singleton
    fun providesSessionManager(
        @ApplicationContext context: Context
    ): SharedPreferenceManager =
        SharedPreferenceManager(context)
}