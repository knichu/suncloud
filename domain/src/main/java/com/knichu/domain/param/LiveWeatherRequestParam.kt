package com.knichu.domain.param

data class LiveWeatherRequestParam(
    val serviceKey: String? = null,
    val pageNo: Long? = null,
    val numOfRows: Long? = null,
    val dataType: String? = null,
    val baseDate: Long? = null,
    val baseTime: Long? = null,
    val nx: Long? = null,
    val ny: Long? = null
)
