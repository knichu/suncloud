package com.knichu.nationwide.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.useCase.WeatherUseCase
import com.knichu.nationwide.model.CITY_TIER_MAP
import com.knichu.nationwide.model.CityInfo
import com.knichu.nationwide.model.ZOOM_TIER1_ONLY
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class NationwideViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherUseCase: WeatherUseCase
) : BaseMviViewModel<NationwideUiIntent, NationwideUiState, NationwideUiEffect>() {

    override fun initialState() = NationwideUiState()

    override fun handleIntent(intent: NationwideUiIntent) {
        when (intent) {
            is NationwideUiIntent.LoadInitialData -> loadInitialData()
            is NationwideUiIntent.OnCameraIdle -> onCameraIdle(intent.zoom)
        }
    }

    private fun loadInitialData() {
        if (uiState.value.allCities.isNotEmpty()) return
        viewModelScope.launch {
            val cities = withContext(Dispatchers.IO) { parseCitiesFromAssets() }
            setState { copy(allCities = cities) }
            loadWeatherForCities(cities.filter { it.tier == 1 })
        }
    }

    private fun onCameraIdle(zoom: Double) {
        val maxTier = if (zoom <= ZOOM_TIER1_ONLY) 1 else 2
        val cities = uiState.value.allCities.filter { it.tier <= maxTier }
        loadWeatherForCities(cities)
    }

    private fun loadWeatherForCities(cities: List<CityInfo>) {
        val cached = uiState.value.cityWeatherMap
        val toLoad = cities.filter { !cached.containsKey(it.name) }
        if (toLoad.isEmpty()) return

        setState {
            copy(
                cityWeatherMap = cityWeatherMap + toLoad.associate {
                    it.name to CityWeatherState(isLoading = true)
                }
            )
        }

        viewModelScope.launch {
            val results = toLoad.map { city ->
                async {
                    runCatching {
                        weatherUseCase.getCityPositionWeatherNow(city.name)
                    }.getOrNull()?.let { vo ->
                        city.name to CityWeatherState(
                            temperature = vo.temperature,
                            weatherCondition = vo.weatherCondition
                        )
                    } ?: (city.name to CityWeatherState())
                }
            }.awaitAll()

            setState { copy(cityWeatherMap = cityWeatherMap + results.toMap()) }
        }
    }

    private fun parseCitiesFromAssets(): List<CityInfo> {
        return try {
            val json = context.assets.open("json/cityCode.json").bufferedReader().use { it.readText() }
            val array = JSONObject(json).getJSONArray("cityList")
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val name = obj.getString("cityName")
                    val tier = CITY_TIER_MAP[name] ?: continue
                    add(
                        CityInfo(
                            name = name,
                            lat = obj.getString("latitude").toDouble(),
                            lon = obj.getString("longitude").toDouble(),
                            tier = tier
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
