package com.knichu.domain.vo

data class MidWeatherVO(
    val item :List<MidWeatherItemVO>? = null
)

data class MidWeatherItemVO(
    val category: String? = null,
    val forecastDate: String? = null,
    val forecastTime: String? = null,
    val forecastValue: String? = null,
)
