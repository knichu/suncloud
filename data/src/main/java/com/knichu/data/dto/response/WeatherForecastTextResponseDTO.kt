package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class WeatherForecastTextResponseDTO(
    @field:SerializedName("response") val wFTResponse: WFTResponse? = null
) {
    data class WFTResponse(
        @field:SerializedName("header") val wFTHeader: WFTHeader? = null,
        @field:SerializedName("body") val wFTBody: WFTBody? = null
    )

    data class WFTHeader(
        @field:SerializedName("resultCode") val resultCode: String,
        @field:SerializedName("resultMsg") val resultMsg: String
    )

    data class WFTBody(
        @field:SerializedName("dataType") val dataType: String? = null,
        @field:SerializedName("items") val wFTItem: List<WFTItem>? = null,
        @field:SerializedName("pageNo") val pageNo: Long? = null,
        @field:SerializedName("numOfRows") val numOfRows: Long? = null,
        @field:SerializedName("totalCount") val totalCount: Long? = null
    )

    data class WFTItem(
        @field:SerializedName("wfSv") val wfSv: String? = null
    )
}