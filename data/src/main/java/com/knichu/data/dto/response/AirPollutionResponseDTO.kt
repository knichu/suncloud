package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class AirPollutionResponseDTO(
    @field:SerializedName("coord") val coord: LonLat? = null,
    @field:SerializedName("list") val list: List<AirPollutionList>? = null
) {
    data class LonLat(
        @field:SerializedName("lon") val lon: Double? = null,
        @field:SerializedName("lat") val lat: Double? = null
    )

    data class AirPollutionList(
        @field:SerializedName("main") val main: Main? = null,
        @field:SerializedName("components") val components: Components? = null,
        @field:SerializedName("dt") val dt: Long? = null
    )

    data class Main(
        @field:SerializedName("aqi") val aqi: Long? = null,
    )

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
}