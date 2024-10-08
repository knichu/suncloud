package com.knichu.gateway.di

import com.knichu.data.repository.AirPollutionRepositoryImpl
import com.knichu.data.repository.CityLocationRepositoryImpl
import com.knichu.data.repository.OpenWeatherRepositoryImpl
import com.knichu.data.repository.WeatherRepositoryImpl
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.repository.OpenWeatherRepository
import com.knichu.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsAirPollutionRepository(
        airPollutionRepositoryImpl: AirPollutionRepositoryImpl
    ): AirPollutionRepository

    @Binds
    fun bindsWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    fun bindsCityLocationRepository(
        cityLocationRepositoryImpl: CityLocationRepositoryImpl
    ): CityLocationRepository

    @Binds
    fun bindsOpenWeatherRepository(
        openWeatherRepositoryImpl: OpenWeatherRepositoryImpl
    ): OpenWeatherRepository
}