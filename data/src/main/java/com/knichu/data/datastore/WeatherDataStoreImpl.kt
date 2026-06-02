package com.knichu.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.datastore.WeatherDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : WeatherDataStore {

    override suspend fun storeUserTempUnit(unit: WeatherTempUnit) {
        dataStore.updateData { pref ->
            pref.toMutablePreferences().also { it[TEMP_UNIT_KEY] = unit.name }
        }
    }

    override suspend fun getUserTempUnit(): WeatherTempUnit {
        val pref = dataStore.data.first()
        val unitStr = pref[TEMP_UNIT_KEY] ?: ""
        return if (unitStr.isEmpty()) WeatherTempUnit.CELSIUS
        else WeatherTempUnit.valueOf(unitStr.uppercase())
    }

    override fun getTempUnitFlow(): Flow<WeatherTempUnit> {
        return dataStore.data.map { pref ->
            val unitStr = pref[TEMP_UNIT_KEY] ?: ""
            if (unitStr.isEmpty()) WeatherTempUnit.CELSIUS
            else WeatherTempUnit.valueOf(unitStr.uppercase())
        }
    }

    override suspend fun storeCity(cityName: String) {
        dataStore.updateData { pref ->
            pref.toMutablePreferences().also {
                val existing = it[CITY_LIST_KEY]?.toListOfString() ?: emptyList()
                it[CITY_LIST_KEY] = existing.plus(cityName).toJsonString()
            }
        }
    }

    override fun getCityList(): Flow<List<String>> {
        return dataStore.data.map { pref ->
            pref[CITY_LIST_KEY]?.toListOfString() ?: emptyList()
        }
    }

    override suspend fun deleteCity(selectedCityList: MutableSet<String>) {
        dataStore.updateData { pref ->
            pref.toMutablePreferences().also {
                val existing = it[CITY_LIST_KEY]?.toListOfString() ?: emptyList()
                it[CITY_LIST_KEY] = existing.filter { city -> city !in selectedCityList }.toJsonString()
            }
        }
    }

    private fun List<String>.toJsonString(): String = Gson().toJson(this)

    private fun String.toListOfString(): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(this, listType)
    }

    companion object {
        private val TEMP_UNIT_KEY = stringPreferencesKey("temp_unit_key")
        private val CITY_LIST_KEY = stringPreferencesKey("city_list_key")
    }
}
