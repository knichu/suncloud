package com.knichu.domain.param

data class WeatherForecastTextRequestParam(
    val serviceKey: String,
    val pageNo: Long,
    val numOfRows: Long,
    val dataType: String,
    val stnId: String,
    val tmFc: Long
)
