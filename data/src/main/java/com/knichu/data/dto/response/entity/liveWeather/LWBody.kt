package com.knichu.data.dto.response.entity.liveWeather

import com.google.gson.annotations.SerializedName

data class LWBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val lWItem: List<LWItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)
