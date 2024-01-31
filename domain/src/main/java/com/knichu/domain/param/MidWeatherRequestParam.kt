package com.knichu.domain.param

data class MidWeatherRequestParam(
    val baseDate: Long? = null,
    val nx: Long? = null,
    val ny: Long? = null
)