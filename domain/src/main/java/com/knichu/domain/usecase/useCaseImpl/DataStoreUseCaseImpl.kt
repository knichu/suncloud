package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.datastore.WeatherDataStore
import com.knichu.domain.useCase.DataStoreUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreUseCaseImpl @Inject constructor(
    private val weatherDataStore: WeatherDataStore
) : DataStoreUseCase {

    override suspend fun storeUserTempUnit(unit: WeatherTempUnit) {
        weatherDataStore.storeUserTempUnit(unit)
    }

    override suspend fun getUserTempUnit(): WeatherTempUnit {
        return weatherDataStore.getUserTempUnit()
    }

    override suspend fun storeCity(cityName: String) {
        val cityList = weatherDataStore.getCityList().first()
        if (cityList.contains(cityName)) {
            throw IllegalArgumentException("이미 도시가 추가되어 있습니다")
        }
        weatherDataStore.storeCity(cityName)
    }

    override fun getCityList(): Flow<List<String>> {
        return weatherDataStore.getCityList()
    }

    override suspend fun deleteCity(selectedCitySet: MutableSet<String>) {
        val cityList = weatherDataStore.getCityList().first()
        val missingCities = selectedCitySet.filterNot { cityList.contains(it) }
        if (missingCities.isNotEmpty()) {
            throw IllegalArgumentException("도시를 찾을 수 없습니다: $missingCities")
        }
        weatherDataStore.deleteCity(selectedCitySet)
    }
}
