package com.knichu.data.dto.response.entity.airPollution

import com.google.gson.annotations.SerializedName

data class Components(
    @field:SerializedName("co") val co: Double? = null,
    @field:SerializedName("no") val no: Double? = null,
    @field:SerializedName("no2") val no2: Double? = null,
    @field:SerializedName("o3") val o3: Double? = null,
    @field:SerializedName("so2") val so2: Double? = null,
    @field:SerializedName("pm2_5") val pm2_5: Double? = null,
    @field:SerializedName("pm10") val pm10: Double? = null,
    @field:SerializedName("nh3") val nh3: Double? = null
)
