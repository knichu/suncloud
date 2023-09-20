package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName

data class AirPollutionRequestDTO(
    @field:SerializedName("lat") val lat: Double? = null,
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("appid") val appid: String? = null
)
