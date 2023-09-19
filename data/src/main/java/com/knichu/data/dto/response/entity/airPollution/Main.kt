package com.knichu.data.dto.response.entity.airPollution

import com.google.gson.annotations.SerializedName

data class Main(
    @field:SerializedName("aqi") val aqi: Long? = null,
)
