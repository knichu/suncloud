package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO

data class ShortWeatherResponseDTO(
    @field:SerializedName("response") val response: ShortWeatherResponse? = null
) {
    fun toDomain(): ShortWeatherVO {
        return ShortWeatherVO(
            item = response?.body?.items?.map {
                ShortWeatherItemVO(
                    baseDate = requireNotNull(it.baseDate),
                    baseTime = requireNotNull(it.baseTime),
                    category = requireNotNull(it.category),
                    forecastDate = requireNotNull(it.fcstDate),
                    forecastTime = requireNotNull(it.fcstTime),
                    forecastValue = requireNotNull(it.fcstValue),
                    nx = requireNotNull(it.nx),
                    ny = requireNotNull(it.ny)
                )
            } ?: emptyList()
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
