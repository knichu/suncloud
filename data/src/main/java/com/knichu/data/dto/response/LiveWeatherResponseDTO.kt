package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class LiveWeatherResponseDTO(
    @field:SerializedName("response") val lWResponse: LWResponse? = null
) {
    data class LWResponse(
        @field:SerializedName("header") val lWHeader: LWHeader,
        @field:SerializedName("body") val lWBody: LWBody? = null
    )

    data class LWHeader(
        @field:SerializedName("resultCode") val resultCode: String,
        @field:SerializedName("resultMsg") val resultMsg: String
    )

    data class LWBody(
        @field:SerializedName("dataType") val dataType: String? = null,
        @field:SerializedName("items") val lWItem: List<LWItem>? = null,
        @field:SerializedName("pageNo") val pageNo: Long? = null,
        @field:SerializedName("numOfRows") val numOfRows: Long? = null,
        @field:SerializedName("totalCount") val totalCount: Long? = null
    )

    data class LWItem(
        @field:SerializedName("baseDate") val baseDate: String? = null,
        @field:SerializedName("baseTime") val baseTime: String? = null,
        @field:SerializedName("category") val category: String? = null,
        @field:SerializedName("nx") val nx: Long? = null,
        @field:SerializedName("ny") val ny: Long? = null,
        @field:SerializedName("obsrValue") val obsrValue: String? = null
    )
}
