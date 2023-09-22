package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class WeatherForecastTextResponseDTO(
    @field:SerializedName("response") val response: WeatherForecastTextResponse? = null
) {
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
        @field:SerializedName("items") val items: List<WeatherForecastTextItem>? = null,
        @field:SerializedName("pageNo") val pageNo: Long? = null,
        @field:SerializedName("numOfRows") val numOfRows: Long? = null,
        @field:SerializedName("totalCount") val totalCount: Long? = null
    )

    data class WeatherForecastTextItem(
        @field:SerializedName("wfSv") val wfSv: String? = null
    )
}