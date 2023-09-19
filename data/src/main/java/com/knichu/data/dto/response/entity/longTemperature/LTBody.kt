package com.knichu.data.dto.response.entity.longTemperature

import com.google.gson.annotations.SerializedName

data class LTBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val lTItem: List<LTItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)
