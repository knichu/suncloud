package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class ShortWeatherResponseDTO(
    @field:SerializedName("response") val sWResponse: SWResponse? = null
) {
    data class SWResponse(
        @field:SerializedName("header") val sWHeader: SWHeader? = null,
        @field:SerializedName("body") val sWBody: SWBody? = null
    )

    data class SWHeader(
        @field:SerializedName("resultCode") val resultCode: String? = null,
        @field:SerializedName("resultMsg") val resultMsg: String? = null
    )

    data class SWBody(
        @field:SerializedName("dataType") val dataType: String? = null,
        @field:SerializedName("items") val sWItem: List<SWItem>? = null,
        @field:SerializedName("pageNo") val pageNo: Long? = null,
        @field:SerializedName("numOfRows") val numOfRows: Long? = null,
        @field:SerializedName("totalCount") val totalCount: Long? = null
    )

    data class SWItem(
        @field:SerializedName("baseDate") val baseDate: String? = null,
        @field:SerializedName("baseTime") val baseTime: String? = null,
        @field:SerializedName("category") val category: String? = null,
        @field:SerializedName("fcstDate") val fcstDate: String? = null,
        @field:SerializedName("fcstTime") val fcstTime: String? = null,
        @field:SerializedName("fcstValue") val fcstValue: String? = null,
        @field:SerializedName("nx") val nx: Long? = null,
        @field:SerializedName("ny") val ny: Long? = null
    )
}
