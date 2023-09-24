package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.ShortWeatherVO

data class ShortWeatherResponseDTO(
    @field:SerializedName("response") val response: ShortWeatherResponse? = null
) {
    fun toDomain(): ShortWeatherVO {
        return ShortWeatherVO(
            baseDate = requireNotNull(response?.body?.items?.firstOrNull()?.baseDate),
            baseTime = requireNotNull(response?.body?.items?.firstOrNull()?.baseTime),
            category = requireNotNull(response?.body?.items?.firstOrNull()?.category),
            forecastDate = requireNotNull(response?.body?.items?.firstOrNull()?.fcstDate),
            forecastTime = requireNotNull(response?.body?.items?.firstOrNull()?.fcstTime),
            forecastValue = requireNotNull(response?.body?.items?.firstOrNull()?.fcstValue),
            nx = requireNotNull(response?.body?.items?.firstOrNull()?.nx),
            ny = requireNotNull(response?.body?.items?.firstOrNull()?.ny)
        )
    }
}

data class ShortWeatherResponse(
    @field:SerializedName("header") val header: ShortWeatherHeader? = null,
    @field:SerializedName("body") val body: ShortWeatherBody? = null
)

data class ShortWeatherHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

data class ShortWeatherBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val items: List<ShortWeatherItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class ShortWeatherItem(
    @field:SerializedName("baseDate") val baseDate: String? = null,
    @field:SerializedName("baseTime") val baseTime: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("fcstDate") val fcstDate: String? = null,
    @field:SerializedName("fcstTime") val fcstTime: String? = null,
    @field:SerializedName("fcstValue") val fcstValue: String? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null
)
