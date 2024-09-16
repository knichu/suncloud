package com.knichu.domain.param

data class LiveWeatherRequestParam(
    val baseDate: String? = null,
    val baseTime: String? = null,
    val nx: Long? = null,
    val ny: Long? = null
)
