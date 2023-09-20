package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName

data class ShortWeatherRequestDTO(
    @field:SerializedName("serviceKey") val serviceKey: String? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("base_date") val baseDate: Long? = null,
    @field:SerializedName("base_time") val baseTime: Long? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null
)
