package com.knichu.domain.vo

data class AirPollutionVO(
    val lon: Double? = null,
    val lat: Double? = null,
    val datetime: Long? = null,
    val airQualityIndex: Long? = null,
    val co: Double? = null,
    val no: Double? = null,
    val no2: Double? = null,
    val o3: Double? = null,
    val so2: Double? = null,
    val pm2_5: Double? = null,
    val pm10: Double? = null,
    val nh3: Double? = null
)