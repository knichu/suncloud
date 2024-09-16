package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.WeatherForecastTextVO

data class WeatherForecastTextResponseDTO(
    @field:SerializedName("response") val response: WeatherForecastTextResponse? = null
) {
    fun toDomain(): WeatherForecastTextVO {
        return WeatherForecastTextVO(
            weatherForecastString = requireNotNull(response?.body?.items?.item?.firstOrNull()?.wfSv)
        )
    }
}

data class WeatherForecastTextResponse(
    @field:SerializedName("header") val header: WeatherForecastTextHeader? = null,
    @field:SerializedName("body") val body: WeatherForecastTextBody? = null
)

data class WeatherForecastTextHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

data class WeatherForecastTextBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val items: WeatherForecastTextItems? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class WeatherForecastTextItems(
    @field:SerializedName("item") val item: List<WeatherForecastTextItem>? = null,
)

data class WeatherForecastTextItem(
    @field:SerializedName("wfSv") val wfSv: String? = null
)