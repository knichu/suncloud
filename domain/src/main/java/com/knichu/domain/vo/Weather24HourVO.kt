package com.knichu.domain.vo

data class Weather24HourVO(
    val item :List<Weather24HourItemVO>? = null
)

data class Weather24HourItemVO(
    val time: String? = null,
    val weatherCondition: String? = null,
    val temperature: String? = null,
    val rainProbability: String? = null,
)
