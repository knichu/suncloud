package com.knichu.domain.param

data class OpenWeatherRequestParam(
    val lon: Double? = null,
    val lat: Double? = null,
    val appid: String? = null,
    val units: String? = null
)
