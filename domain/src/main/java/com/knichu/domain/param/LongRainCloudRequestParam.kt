package com.knichu.domain.param

data class LongRainCloudRequestParam(
    val serviceKey: String? = null,
    val pageNo: Long? = null,
    val numOfRows: Long? = null,
    val dataType: String? = null,
    val regId: String? = null,
    val tmFc: Long? = null
)
