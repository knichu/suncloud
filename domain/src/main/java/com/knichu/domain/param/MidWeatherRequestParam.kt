package com.knichu.domain.param

data class MidWeatherRequestParam(
    val baseDate: String? = null,
    val nx: Long? = null,
    val ny: Long? = null
)