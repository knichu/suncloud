package com.knichu.nationwide.ui

import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.nationwide.model.CityInfo

data class NationwideUiState(
    val tempUnit: WeatherTempUnit = WeatherTempUnit.CELSIUS,
    val allCities: List<CityInfo> = emptyList(),
    val cityWeatherMap: Map<String, CityWeatherState> = emptyMap(),
    val error: String? = null
)

data class CityWeatherState(
    val temperature: String? = null,
    val weatherCondition: String? = null,
    val isLoading: Boolean = false
)

sealed class NationwideUiIntent {
    object LoadInitialData : NationwideUiIntent()
    data class OnCameraIdle(val zoom: Double) : NationwideUiIntent()
}

sealed class NationwideUiEffect
