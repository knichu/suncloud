package com.knichu.domain.param

data class ShortWeatherRequestParam(
    val baseDate: Long? = null,
    val baseTime: Long? = null,
    val nx: Long? = null,
    val ny: Long? = null
)
