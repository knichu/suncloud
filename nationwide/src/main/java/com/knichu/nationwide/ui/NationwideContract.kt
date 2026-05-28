package com.knichu.nationwide.ui

data class NationwideUiState(
    val dummy: Unit = Unit
)

sealed class NationwideUiIntent

sealed class NationwideUiEffect
