package com.knichu.data.dto.response.entity.short

import com.google.gson.annotations.SerializedName

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
