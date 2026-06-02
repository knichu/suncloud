package com.knichu.setting.ui

import com.knichu.domain.constants.WeatherTempUnit

data class SettingUiState(
    val tempUnit: WeatherTempUnit = WeatherTempUnit.CELSIUS
)

sealed class SettingUiIntent {
    data class OnTempUnitChanged(val unit: WeatherTempUnit) : SettingUiIntent()
}

sealed class SettingUiEffect
