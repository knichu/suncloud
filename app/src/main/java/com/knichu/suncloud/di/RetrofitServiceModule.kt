package com.knichu.suncloud.di

import com.knichu.data.service.AirPollutionService
import com.knichu.data.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitServiceModule {

    @Provides
    @Singleton
    fun providesAirPollutionService(retrofit: Retrofit): AirPollutionService =
        retrofit.create(AirPollutionService::class.java)

    @Provides
    @Singleton
    fun providesWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)
}