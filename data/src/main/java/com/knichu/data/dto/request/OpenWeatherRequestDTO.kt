package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName

data class OpenWeatherRequestDTO(
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("lat") val lat: Double? = null,
    @field:SerializedName("appid") val appid: String? = null,
    @field:SerializedName("units") val units: String? = null
)
