package com.knichu.gateway.di

import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.WeatherUseCase
import com.knichu.domain.useCase.useCaseImpl.AirPollutionUseCaseImpl
import com.knichu.domain.useCase.useCaseImpl.DataStoreUseCaseImpl
import com.knichu.domain.useCase.useCaseImpl.WeatherUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindsAirPollutionUseCase(
        airPollutionUseCaseImpl: AirPollutionUseCaseImpl
    ): AirPollutionUseCase

    @Binds
    fun bindsDataStoreUseCase(
        dataStoreUseCaseImpl: DataStoreUseCaseImpl
    ): DataStoreUseCase

    @Binds
    fun bindsWeatherUseCase(
        weatherUseCaseImpl: WeatherUseCaseImpl
    ): WeatherUseCase
}