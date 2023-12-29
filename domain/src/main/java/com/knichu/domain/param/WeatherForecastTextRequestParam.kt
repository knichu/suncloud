package com.knichu.domain.param

data class WeatherForecastTextRequestParam(
    val serviceKey: String? = null,
    val pageNo: Long? = null,
    val numOfRows: Long? = null,
    val dataType: String? = null,
    val stnId: String? = null,
    val tmFc: Long? = null
)
