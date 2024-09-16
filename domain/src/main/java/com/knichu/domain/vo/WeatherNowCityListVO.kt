package com.knichu.domain.vo

data class WeatherNowCityListVO(
    val item :List<WeatherNowCityListItemVO>? = null
)

data class WeatherNowCityListItemVO(
    val temperature: String? = null,
    val city: String? = null,
    val weatherCondition: String? = null
)
