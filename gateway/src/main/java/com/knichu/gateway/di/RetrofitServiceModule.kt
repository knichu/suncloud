package com.knichu.gateway.di

import android.app.Application
import android.content.Context
import com.knichu.data.service.AirPollutionService
import com.knichu.data.service.OpenWeatherService
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
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesAirPollutionService(@AirPollutionQualifier retrofit: Retrofit): AirPollutionService =
        retrofit.create(AirPollutionService::class.java)

    @Provides
    @Singleton
    fun providesWeatherService(@WeatherQualifier retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun providesOpenWeatherService(@OpenWeatherQualifier retrofit: Retrofit): OpenWeatherService =
        retrofit.create(OpenWeatherService::class.java)
}