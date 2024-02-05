package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.MidWeatherItemVO
import com.knichu.domain.vo.MidWeatherVO

data class MidWeatherResponseDTO(
    @field:SerializedName("response") val response: MidWeatherResponse? = null
) {
    fun toDomain(): MidWeatherVO {
        return MidWeatherVO(
            item = response?.body?.items?.map {
                MidWeatherItemVO(
                    category = requireNotNull(it.category),
                    forecastDate = requireNotNull(it.fcstDate),
                    forecastTime = requireNotNull(it.fcstTime),
                    forecastValue = requireNotNull(it.fcstValue),
                )
            } ?: emptyList()
        )
    }
}

data class MidWeatherResponse(
    @field:SerializedName("header") val header: MidWeatherHeader? = null,
    @field:SerializedName("body") val body: MidWeatherBody? = null
)

data class MidWeatherHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

data class MidWeatherBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val items: List<MidWeatherItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class MidWeatherItem(
    @field:SerializedName("baseDate") val baseDate: String? = null,
    @field:SerializedName("baseTime") val baseTime: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("fcstDate") val fcstDate: String? = null,
    @field:SerializedName("fcstTime") val fcstTime: String? = null,
    @field:SerializedName("fcstValue") val fcstValue: String? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null
)
