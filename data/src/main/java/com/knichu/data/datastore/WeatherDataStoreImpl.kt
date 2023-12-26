package com.knichu.data.datastore

import io.reactivex.rxjava3.core.Single
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.rxjava3.RxDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.datastore.WeatherDataStore
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class WeatherDataStoreImpl @Inject constructor(
    private val rxDataStore: RxDataStore<Preferences>
) : WeatherDataStore {

    // 사용자의 temp unit을 저장
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun storeUserTempUnit(unit: WeatherTempUnit): Single<Preferences> {
        return rxDataStore.updateDataAsync { pref ->
            val mutablePreferences = pref.toMutablePreferences()
            mutablePreferences[TEMP_UNIT_KEY] = unit.name
            Single.just(mutablePreferences)
        }
    }

    // 사용자의 temp unit을 검색
    @OptIn(ExperimentalCoroutinesApi::class)
    private val userTempUnitFlowable: Flowable<WeatherTempUnit> =
        rxDataStore.data().map { pref ->
            val unitStr = pref[TEMP_UNIT_KEY] ?: EMPTY_STRING
            if (unitStr.isEmpty()) WeatherTempUnit.CELSIUS else WeatherTempUnit.valueOf(unitStr.uppercase())
        }
    override fun getUserTempUnit(): Single<WeatherTempUnit> {
        return userTempUnitFlowable.firstOrError()
    }

    // 사용자가 즐겨찾기한 도시 저장
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun storeCity(cityName: String): Single<Preferences> {
        return rxDataStore.updateDataAsync { pref ->
            val mutablePreferences = pref.toMutablePreferences()
            val existingData = mutablePreferences[CITY_LIST_KEY]?.toListOfString() ?: listOf(DEFAULT_CITY)
            val updatedData = existingData.plus(cityName)
            mutablePreferences[CITY_LIST_KEY] = updatedData.toJsonString()
            Single.just(mutablePreferences)
        }
    }

    // 사용자가 즐겨찾기한 도시 불러오기
    @OptIn(ExperimentalCoroutinesApi::class)
    private val getCityListFlowable: Flowable<List<String>> =
        rxDataStore.data().map { pref ->
            val cityListString = pref[CITY_LIST_KEY] ?: listOf(DEFAULT_CITY).toJsonString()
            val cityList = cityListString.toListOfString()
            cityList
        }
    override fun getCityList(): Single<List<String>> {
        return getCityListFlowable.firstOrError()
    }

    // 사용자가 즐겨찾기한 도시 삭제
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun deleteCity(cityName: String): Single<Preferences> {
        return rxDataStore.updateDataAsync { pref ->
            val mutablePreferences = pref.toMutablePreferences()
            val existingData = mutablePreferences[CITY_LIST_KEY]?.toListOfString()
            val updatedData = existingData?.filter { it != cityName }
            mutablePreferences[CITY_LIST_KEY] = updatedData?.toJsonString() ?: listOf(DEFAULT_CITY).toJsonString()
            Single.just(mutablePreferences)
        }
    }

    // 직렬화
    private fun List<String>.toJsonString(): String = Gson().toJson(this)

    // 역직렬화
    private fun String.toListOfString(): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(this, listType)
    }

//    private fun String.toListOfStringTest(): List<*> = Gson().fromJson(this, List::class.java)

    /**
     * 중요한 정보를 저장할 경우 BuildConfig를 통해 Key를 관리해야 합니다.
     * 이 프로젝트의 경우 예시 프로젝트 임의로 해당 파일에 key를 관리했습니다.
     */
    companion object {
        private val TEMP_UNIT_KEY = stringPreferencesKey("temp_unit_key")
        private val CITY_LIST_KEY = stringPreferencesKey("city_list_key")
        private const val EMPTY_STRING = ""
        private const val DEFAULT_CITY = "Seoul"
    }
}