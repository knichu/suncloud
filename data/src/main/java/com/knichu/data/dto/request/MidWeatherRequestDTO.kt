package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.request.ApiKeys.APIS_DATA_WEATHER_API_KEY
import com.knichu.data.dto.request.ApiKeys.JSON

data class MidWeatherRequestDTO(
    @field:SerializedName("serviceKey") val serviceKey: String? = APIS_DATA_WEATHER_API_KEY,
    @field:SerializedName("pageNo") val pageNo: Long? = 1,
    @field:SerializedName("numOfRows") val numOfRows: Long? = 880,
    @field:SerializedName("dataType") val dataType: String? = JSON,
    @field:SerializedName("base_date") val baseDate: Long? = null,
    @field:SerializedName("base_time") val baseTime: Long? = 2300,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null
)
