package com.knichu.data.dto.request

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.request.ApiKeys.APIS_DATA_WEATHER_API_KEY
import com.knichu.data.dto.request.ApiKeys.JSON

data class WeatherForecastTextRequestDTO(
    @field:SerializedName("serviceKey") val serviceKey: String? = APIS_DATA_WEATHER_API_KEY,
    @field:SerializedName("pageNo") val pageNo: Long? = 1,
    @field:SerializedName("numOfRows") val numOfRows: Long? = 1,
    @field:SerializedName("dataType") val dataType: String? = JSON,
    @field:SerializedName("stnId") val stnId: String? = null,
    @field:SerializedName("tmFc") val tmFc: Long? = null
)