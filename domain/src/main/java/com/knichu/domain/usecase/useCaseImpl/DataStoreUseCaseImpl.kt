package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.datastore.WeatherDataStore
import com.knichu.domain.useCase.DataStoreUseCase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DataStoreUseCaseImpl @Inject constructor(
    private val weatherDataStore: WeatherDataStore
): DataStoreUseCase {
    override fun storeUserTempUnit(unit: WeatherTempUnit): Completable {
        return weatherDataStore.storeUserTempUnit(unit).ignoreElement()
    }

    override fun getUserTempUnit(): Single<WeatherTempUnit> {
        return weatherDataStore.getUserTempUnit()
    }

    override fun initStoreDefaultCity(): Completable {
        return weatherDataStore.initStoreDefaultCity().ignoreElement()
    }

    override fun storeCity(cityName: String): Completable {
        return weatherDataStore.getCityList()
            .flatMapCompletable { cityList ->
                if (cityList.contains(cityName)) {
                    Completable.error(IllegalStateException("이미 도시가 추가되어 있습니다"))
                } else {
                    Completable.fromAction { weatherDataStore.storeCity(cityName) }
                }
            }
    }

    override fun getCityList(): Single<List<String>> {
        return weatherDataStore.getCityList()
    }

    override fun deleteCity(cityName: String): Completable {
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