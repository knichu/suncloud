package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.LiveWeatherVO

data class LiveWeatherResponseDTO(
    @field:SerializedName("response") val response: LiveWeatherResponse? = null
) {
    fun toDomain(): LiveWeatherVO {
        return LiveWeatherVO(
            baseDate = requireNotNull(response?.body?.items?.firstOrNull()?.baseDate),
            baseTime = requireNotNull(response?.body?.items?.firstOrNull()?.baseTime),
            category = requireNotNull(response?.body?.items?.firstOrNull()?.category),
            nx = requireNotNull(response?.body?.items?.firstOrNull()?.nx),
            ny = requireNotNull(response?.body?.items?.firstOrNull()?.ny),
            observeValue = requireNotNull(response?.body?.items?.firstOrNull()?.obsrValue)
        )
    }
}

data class LiveWeatherResponse(
    @field:SerializedName("header") val header: LiveWeatherHeader? = null,
    @field:SerializedName("body") val body: LiveWeatherBody? = null
)

data class LiveWeatherHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

data class LiveWeatherBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val items: List<LiveWeatherItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class LiveWeatherItem(
    @field:SerializedName("baseDate") val baseDate: String? = null,
    @field:SerializedName("baseTime") val baseTime: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null,
    @field:SerializedName("obsrValue") val obsrValue: String? = null
)