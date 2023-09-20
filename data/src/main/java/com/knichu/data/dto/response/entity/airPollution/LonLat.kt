package com.knichu.data.dto.response.entity.airPollution

import com.google.gson.annotations.SerializedName

data class LonLat(
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("lat") val lat: Double? = null
)
