package com.knichu.forecast.ui.citymanage

data class CityManageUiState(
    val cityList: List<String> = emptyList(),
    val isInSelectionMode: Boolean = false,
    val selectedCitySet: Set<String> = emptySet()
)

sealed class CityManageUiIntent {
    object ToggleSelectionMode : CityManageUiIntent()
    data class ToggleCitySelection(val city: String) : CityManageUiIntent()
    object DeleteSelectedCities : CityManageUiIntent()
    object NavigateBack : CityManageUiIntent()
}

sealed class CityManageUiEffect {
    object NavigateBack : CityManageUiEffect()
}
