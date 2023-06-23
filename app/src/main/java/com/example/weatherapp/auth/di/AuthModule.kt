package com.example.weatherapp.auth.di

import com.example.weatherapp.auth.data.local.AuthDao
import com.example.weatherapp.auth.data.repository.AuthRepository
import com.example.weatherapp.auth.domain.repository.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    fun providesAuthRepo(
        authDao: AuthDao
    ) : IAuthRepository =
        AuthRepository(authDao)

}