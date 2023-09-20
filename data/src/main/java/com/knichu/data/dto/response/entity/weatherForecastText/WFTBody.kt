package com.knichu.data.dto.response.entity.weatherForecastText

import com.google.gson.annotations.SerializedName

data class WFTBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val wFTItem: List<WFTItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)
