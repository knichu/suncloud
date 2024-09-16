package com.knichu.domain.vo

data class LiveWeatherVO(
    val item :List<LiveWeatherItemVO>? = null
)

data class LiveWeatherItemVO(
    val category: String? = null,
    val observeValue: String? = null
)