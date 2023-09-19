package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName

data class WeatherForecastTextRequestDTO(
    @field:SerializedName("serviceKey") val serviceKey: String? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("stnId") val stnId: String? = null,
    @field:SerializedName("tmFc") val tmFc: Long? = null
)