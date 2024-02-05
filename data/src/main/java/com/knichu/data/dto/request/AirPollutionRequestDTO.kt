package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.request.ApiKeys.OPEN_WEATHER_MAP_API_KEY

data class AirPollutionRequestDTO(
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("lat") val lat: Double? = null,
    @field:SerializedName("appid") val appid: String? = OPEN_WEATHER_MAP_API_KEY
)
