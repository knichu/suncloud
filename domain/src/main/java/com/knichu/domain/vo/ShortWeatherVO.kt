package com.knichu.domain.vo

data class ShortWeatherVO(
    val baseDate: String? = null,
    val baseTime: String? = null,
    val category: String? = null,
    val forecastDate: String? = null,
    val forecastTime: String? = null,
    val forecastValue: String? = null,
    val nx: Long? = null,
    val ny: Long? = null
)
