package com.knichu.domain.param

data class LiveWeatherRequestParam(
    val baseDate: Long? = null,
    val baseTime: Long? = null,
    val nx: Long? = null,
    val ny: Long? = null
)
