package com.knichu.domain.vo

data class LiveWeatherVO(
    val item :List<LiveWeatherItemVO>
)

data class LiveWeatherItemVO(
    val baseDate: String? = null,
    val baseTime: String? = null,
    val category: String? = null,
    val nx: Long? = null,
    val ny: Long? = null,
    val observeValue: String? = null
)