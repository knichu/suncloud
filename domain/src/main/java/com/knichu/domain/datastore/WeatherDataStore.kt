package com.knichu.domain.datastore

import androidx.datastore.preferences.core.Preferences
import com.knichu.domain.constants.WeatherTempUnit
import io.reactivex.rxjava3.core.Single

interface WeatherDataStore {

    // 사용자가 선택한 기온단위를 관리하는 기능
    fun storeUserTempUnit(unit: WeatherTempUnit): Single<Preferences>
    fun getUserTempUnit(): Single<WeatherTempUnit>

    // 사용자가 즐겨찾기한 도시 관리하는 기능
    fun initStoreDefaultCity(): Single<Preferences>
    fun storeCity(cityName: String): Single<Preferences>
    fun getCityList(): Single<List<String>>
    fun deleteCity(cityName: String): Single<Preferences>
}