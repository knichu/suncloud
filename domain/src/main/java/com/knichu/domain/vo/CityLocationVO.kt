package com.knichu.domain.vo

data class CityLocationVO(
    val item: List<CityLocationItemVO>? = null
)

data class CityLocationItemVO(
    val cityName: String? = null,
    val longRainCloudParam: String? = null,
    val longTemperatureParam: String? = null,
    val weatherForecastTextParam: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
)