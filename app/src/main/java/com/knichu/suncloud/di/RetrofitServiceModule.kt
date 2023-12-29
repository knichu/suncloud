package com.knichu.suncloud.di

import com.knichu.data.service.AirPollutionService
import com.knichu.data.service.CityLocationService
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
    @AirPollutionQualifier
    fun providesAirPollutionService(retrofit: Retrofit): AirPollutionService =
        retrofit.create(AirPollutionService::class.java)

    @Provides
    @Singleton
    @WeatherQualifier
    fun providesWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    @CityLocationQualifier
    fun providesCityLocationService(retrofit: Retrofit): CityLocationService =
        retrofit.create(CityLocationService::class.java)
}