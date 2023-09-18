package com.knichu.data.dto.response.entity.shortWeather

import com.google.gson.annotations.SerializedName

data class SWBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val sWItem: List<SWItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)
