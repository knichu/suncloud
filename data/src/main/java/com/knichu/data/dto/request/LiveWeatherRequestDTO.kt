package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.request.ApiKeys.APIS_DATA_WEATHER_API_KEY
import com.knichu.data.dto.request.ApiKeys.JSON

data class LiveWeatherRequestDTO(
    @field:SerializedName("serviceKey") val serviceKey: String? = APIS_DATA_WEATHER_API_KEY,
    @field:SerializedName("pageNo") val pageNo: Long? = 1,
    @field:SerializedName("numOfRows") val numOfRows: Long? = 8,
    @field:SerializedName("dataType") val dataType: String? = JSON,
    @field:SerializedName("base_date") val baseDate: String? = null,
    @field:SerializedName("base_time") val baseTime: String? = null,
    @field:SerializedName("nx") val nx: Long? = null,
    @field:SerializedName("ny") val ny: Long? = null
)