package com.knichu.domain.usecase

import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.datastore.WeatherDataStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DataStoreUseCase @Inject constructor(
    private val weatherDataStore: WeatherDataStore
) {
    fun storeUserTempUnit(unit: WeatherTempUnit): Completable {
        return weatherDataStore.storeUserTempUnit(unit).ignoreElement()
    }

    fun getUserTempUnit(): Single<WeatherTempUnit>{
        return weatherDataStore.getUserTempUnit()
    }

    fun initStoreDefaultCity(): Completable {
        return weatherDataStore.initStoreDefaultCity().ignoreElement()
    }

    fun storeCity(cityName: String): Completable {
        return weatherDataStore.getCityList()
            .flatMapCompletable { cityList ->
                if (cityList.contains(cityName)) {
                    Completable.error(IllegalStateException("이미 도시가 추가되어 있습니다"))
                } else {
                    Completable.fromAction { weatherDataStore.storeCity(cityName) }
                }
            }
    }

    fun getCityList(): Single<List<String>> {
        return weatherDataStore.getCityList()
    }

    fun deleteCity(cityName: String): Completable {
        return weatherDataStore.getCityList()
            .flatMapCompletable { cityList ->
                if (cityList.contains(cityName)) {
                    Completable.error(IllegalArgumentException("도시를 찾을 수 없습니다"))
                } else {
                    Completable.fromAction { weatherDataStore.deleteCity(cityName) }
                }
            }
    }
}