package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.LiveWeatherItemVO
import com.knichu.domain.vo.LiveWeatherVO

data class LiveWeatherResponseDTO(
    @field:SerializedName("response") val response: LiveWeatherResponse? = null
) {
    fun toDomain(): LiveWeatherVO {
        return LiveWeatherVO(
            item = response?.body?.items?.item?.map {
                LiveWeatherItemVO(
                    category = requireNotNull(it.category),
                    observeValue = requireNotNull(it.obsrValue)
                )
            } ?: emptyList()
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
    @field:SerializedName("items") val items: LiveWeatherItems? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class LiveWeatherItems(
    @field:SerializedName("item") val item: List<LiveWeatherItem>? = null,
)

data class LiveWeatherItem(
    @field:SerializedName("baseDate") val baseDate: String? = null,
    @field:SerializedName("baseTime") val baseTime: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null,
    @field:SerializedName("obsrValue") val obsrValue: String? = null
)