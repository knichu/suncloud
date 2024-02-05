package com.knichu.domain.vo

data class WeatherWeeklyVO(
    val item :List<WeatherWeeklyItemVO>? = null
)

data class WeatherWeeklyItemVO(
    val dayOfTheWeek: String? = null,
    val rainProbability: String? = null,
    val weatherConditionAM: String? = null,
    val weatherConditionPM: String? = null,
    val maxTemperature: String? = null,
    val minTemperature: String? = null,
)
