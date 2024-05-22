package com.knichu.domain.useCase

import com.knichu.domain.constants.WeatherTempUnit
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface DataStoreUseCase {
    fun storeUserTempUnit(unit: WeatherTempUnit): Completable

    fun getUserTempUnit(): Single<WeatherTempUnit>

    fun initStoreDefaultCity(): Completable

    fun storeCity(cityName: String): Completable

    fun getCityList(): Single<List<String>>

    fun deleteCity(cityName: String): Completable
}