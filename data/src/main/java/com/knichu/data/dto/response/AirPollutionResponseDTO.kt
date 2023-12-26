package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.AirPollutionVO

data class AirPollutionResponseDTO(
    @field:SerializedName("coord") val coord: LonLatDTO? = null,
    @field:SerializedName("list") val list: List<AirPollutionListDTO>? = null
) {
    fun toDomain(): AirPollutionVO {
        return AirPollutionVO(
            lon = requireNotNull(coord?.lon),
            lat = requireNotNull(coord?.lat),
            datetime = requireNotNull(list?.firstOrNull()?.dt),
            airQualityIndex = requireNotNull(list?.firstOrNull()?.main?.aqi),
            co = requireNotNull(list?.firstOrNull()?.components?.co),
            no = requireNotNull(list?.firstOrNull()?.components?.no),
            no2 = requireNotNull(list?.firstOrNull()?.components?.no2),
            o3 = requireNotNull(list?.firstOrNull()?.components?.o3),
            so2 = requireNotNull(list?.firstOrNull()?.components?.so2),
            pm2_5 = requireNotNull(list?.firstOrNull()?.components?.pm2_5),
            pm10 = requireNotNull(list?.firstOrNull()?.components?.pm10),
            nh3 = requireNotNull(list?.firstOrNull()?.components?.nh3)
        )
    }
}

data class LonLatDTO(
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("lat") val lat: Double? = null
)

data class AirPollutionListDTO(
    @field:SerializedName("main") val main: MainDTO? = null,
    @field:SerializedName("components") val components: ComponentsDTO? = null,
    @field:SerializedName("dt") val dt: Long? = null
)

data class MainDTO(
    @field:SerializedName("aqi") val aqi: Long? = null,
)

data class ComponentsDTO(
    @field:SerializedName("co") val co: Double? = null,
    @field:SerializedName("no") val no: Double? = null,
    @field:SerializedName("no2") val no2: Double? = null,
    @field:SerializedName("o3") val o3: Double? = null,
    @field:SerializedName("so2") val so2: Double? = null,
    @field:SerializedName("pm2_5") val pm2_5: Double? = null,
    @field:SerializedName("pm10") val pm10: Double? = null,
    @field:SerializedName("nh3") val nh3: Double? = null
)