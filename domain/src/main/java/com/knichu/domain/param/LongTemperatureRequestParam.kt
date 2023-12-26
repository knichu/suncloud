package com.knichu.domain.param

data class LongTemperatureRequestParam(
    val serviceKey: String,
    val pageNo: Long,
    val numOfRows: Long,
    val dataType: String,
    val regId: String,
    val tmFc: Long
)
