package com.knichu.data.dto.response.entity.live

import com.google.gson.annotations.SerializedName

data class LWItem(
    @field:SerializedName("baseDate") val baseDate: String? = null,
    @field:SerializedName("baseTime") val baseTime: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null,
    @field:SerializedName("obsrValue") val obsrValue: String? = null
)
