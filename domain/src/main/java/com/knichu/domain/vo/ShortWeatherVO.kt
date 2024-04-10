package com.knichu.domain.vo

data class ShortWeatherVO(
    val item :List<ShortWeatherItemVO>? = null
)

data class ShortWeatherItemVO(
    val category: String? = null,
    val forecastDate: String? = null,
    val forecastTime: String? = null,
    val forecastValue: String? = null,
)