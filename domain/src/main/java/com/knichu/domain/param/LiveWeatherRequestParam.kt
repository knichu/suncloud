package com.knichu.domain.param

data class LiveWeatherRequestParam(
    val serviceKey: String,
    val pageNo: Long,
    val numOfRows: Long,
    val dataType: String,
    val baseDate: Long,
    val baseTime: Long,
    val nx: Long,
    val ny: Long
)
