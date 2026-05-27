package com.knichu.domain.useCase

import com.knichu.domain.constants.WeatherTempUnit
import kotlinx.coroutines.flow.Flow

interface DataStoreUseCase {
    suspend fun storeUserTempUnit(unit: WeatherTempUnit)

    suspend fun getUserTempUnit(): WeatherTempUnit

    suspend fun storeCity(cityName: String)

    fun getCityList(): Flow<List<String>>

    suspend fun deleteCity(selectedCitySet: MutableSet<String>)
}
