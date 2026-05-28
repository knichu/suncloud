package com.knichu.setting.ui

data class SettingUiState(
    val dummy: Unit = Unit
)

sealed class SettingUiIntent

sealed class SettingUiEffect
