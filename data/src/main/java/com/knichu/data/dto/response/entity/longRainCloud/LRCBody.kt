package com.knichu.data.dto.response.entity.longRainCloud

import com.google.gson.annotations.SerializedName

data class LRCBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val lRCItem: List<LRCItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)