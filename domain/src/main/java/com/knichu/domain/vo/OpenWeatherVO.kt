package com.knichu.domain.vo

data class OpenWeatherVO(
    val weatherMain: String? = null,
    val weatherDescription: String? = null,
    val weatherIcon: String? = null,
    val temperature: Double? = null,
    val feelsLike: Double? = null,
    val temperatureMin: Double? = null,
    val temperatureMax: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
    val windDirection: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
)